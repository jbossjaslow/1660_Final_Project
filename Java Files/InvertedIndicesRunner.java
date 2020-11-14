import java.awt.EventQueue;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dataproc.Dataproc;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class InvertedIndicesRunner {

	private JFrame frame;
	private LoadEnginePanel loadEnginePanel;
	private IndicesConstructedPanel indicesConstructedPanel;
	private SearchForTermPanel searchForTermPanel;
	private NValuePanel nValuePanel;
	private SearchTermResultsPanel searchTermResultsPanel;
	private TopNResultsPanel topNResultsPanel;

	private Dataproc dataproc;

	int currentRandom = 0;

	public InvertedIndicesRunner() {
		//
	}

	public static void main(String[] args) {
		InvertedIndicesRunner viewModel = new InvertedIndicesRunner();
		viewModel.runFrame(viewModel);
	}

	/**
	 * Run frame the contains application
	 * 
	 * @param viewModel
	 */
	private void runFrame(InvertedIndicesRunner viewModel) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					viewModel.frame = constructFrame();
					constructAllPanels();

					viewModel.changeViewTo(loadEnginePanel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructors
	 */
	private JFrame constructFrame() {
		JFrame frame = new JFrame();
		frame.setTitle("MyName Search Engine");
		frame.setBounds(100, 100, 568, 442);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		return frame;
	}

	private void constructAllPanels() {
		loadEnginePanel = constructLoadEnginePanel();
		indicesConstructedPanel = constructIndicesConstructedPanel();
		searchForTermPanel = constructSearchForTermPanel();
		nValuePanel = constructNValuePanel();
		searchTermResultsPanel = constructSearchTermResultsPanel();
		topNResultsPanel = constructTopNResultsPanel();
	}

	private LoadEnginePanel constructLoadEnginePanel() {
		return new LoadEnginePanel(this, frame);
	}

	private IndicesConstructedPanel constructIndicesConstructedPanel() {
		return new IndicesConstructedPanel(this, frame);
	}

	private SearchForTermPanel constructSearchForTermPanel() {
		return new SearchForTermPanel(this, frame);
	}

	private NValuePanel constructNValuePanel() {
		return new NValuePanel(this, frame);
	}

	private SearchTermResultsPanel constructSearchTermResultsPanel() {
		return new SearchTermResultsPanel(this, frame);
	}

	private TopNResultsPanel constructTopNResultsPanel() {
		return new TopNResultsPanel(this, frame);
	}

	/**
	 * Delegate functions
	 */
	public void handleChooseFiles() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files only", "txt");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			String fileList = "";
			for (File f : files) {
				// System.out.println(f.getName());
				fileList += f.getName() + "\n";
			}
			loadEnginePanel.updateFilesSelectedList(fileList);
		} else {
			System.out.println("You did not open any files");
		}
	}

	/**
	 * Go to indices constructed panel
	 */
	public void handleConstructIndices(String files) {
		System.out.println("Constructing inverted indices");
		String fileList = files.replaceAll("\n", ","); // use commas instead of newline characters just in case new lines break call
		// gcp project info
		String projectId = "our-audio-292023";
		String clusterName = "cluster-ebba";
		String hdfsURL = "hdfs:///user/jbossjaslow/";
		String appName = "wordCountAndTopN";
		String region = "us-central1";

		Random rand = new Random();
		currentRandom = rand.nextInt(1000000);
		String inputDir = hdfsURL + "input"; // input
		String outputDir = hdfsURL + "invertedIndicesOutput" + currentRandom; // output

		ArrayList<String> argsList = new ArrayList<String>();
		argsList.add(inputDir); // add input path
		argsList.add(outputDir); // add output path
		argsList.add(fileList); // files white list

		try {
			// Send job
			InputStream stream = this.getClass().getResourceAsStream("/dataproc_creds.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
			HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
			dataproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), requestInitializer).setApplicationName(appName).build();
			JobPlacement jobPlacement = new JobPlacement().setClusterName(clusterName);
			HadoopJob hadoopJob = new HadoopJob().setMainClass("IIDriver").setJarFileUris(ImmutableList.of(hdfsURL + "invertedIndices.jar")).setArgs(argsList);
			SubmitJobRequest jobRequest = new SubmitJobRequest().setJob(new Job().setPlacement(jobPlacement).setHadoopJob(hadoopJob));
			Job submittedJob = dataproc.projects().regions().jobs().submit(projectId, region, jobRequest).execute();

			// Wait for job to execute to move on
			String jobId = submittedJob.getReference().getJobId();
			Job job = dataproc.projects().regions().jobs().get(projectId, region, jobId).execute();

			String status = job.getStatus().getState();
			while (!status.equalsIgnoreCase("DONE") && !status.equalsIgnoreCase("CANCELLED") && !status.equalsIgnoreCase("ERROR")) {
				System.out.println("Job not done yet; current state: " + job.getStatus().getState());
				Thread.sleep(500); // sleep for 0.5 seconds
				job = dataproc.projects().regions().jobs().get(projectId, region, jobId).execute();
				status = job.getStatus().getState();
			}

			System.out.println("Job terminated in state: " + job.getStatus().getState());

			if (status.equalsIgnoreCase("DONE"))
				changeViewTo(indicesConstructedPanel); // change view when successful
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * Go to search for term panel
	 */
	public void handleChangeToSearchTermPanel() {
		System.out.println("Searching for term");
		changeViewTo(searchForTermPanel);
	}

	/**
	 * Go to top n search panel
	 */
	public void handleChangeToSearchTopNPanel() {
		System.out.println("Top N");
		changeViewTo(nValuePanel);
	}

	/**
	 * Search the files for the specified term
	 * 
	 * @param term
	 */
	public void handleSearchForTerm(String term) {
		System.out.println(term);

		// gcp project info
		String projectId = "our-audio-292023";
		String clusterName = "cluster-ebba";
		String hdfsURL = "hdfs:///user/jbossjaslow/";
		String region = "us-central1";

		Random rand = new Random();
		currentRandom = rand.nextInt(1000000);
		String inputDir = hdfsURL + "input"; // input
		String outputDir = hdfsURL + "invertedIndices" + currentRandom; // input

		// modify args to change input

		try {
			// Send job
			JobPlacement jobPlacement = new JobPlacement().setClusterName(clusterName);
			HadoopJob hadoopJob = new HadoopJob().setMainClass("IIDriver").setJarFileUris(ImmutableList.of(hdfsURL + "invertedIndices.jar")).setArgs(ImmutableList.of(inputDir, outputDir));
			SubmitJobRequest jobRequest = new SubmitJobRequest().setJob(new Job().setPlacement(jobPlacement).setHadoopJob(hadoopJob));
			Job submittedJob = dataproc.projects().regions().jobs().submit(projectId, region, jobRequest).execute();

			// Wait for job to execute to move on
			String jobId = submittedJob.getReference().getJobId();
			Job job = dataproc.projects().regions().jobs().get(projectId, region, jobId).execute();

			String status = job.getStatus().getState();
			while (!status.equalsIgnoreCase("DONE") && !status.equalsIgnoreCase("CANCELLED") && !status.equalsIgnoreCase("ERROR")) {
				System.out.println("Job not done yet; current state: " + job.getStatus().getState());
				Thread.sleep(500); // sleep for 0.5 seconds
				job = dataproc.projects().regions().jobs().get(projectId, region, jobId).execute();
				status = job.getStatus().getState();
			}

			System.out.println("Job terminated in state: " + job.getStatus().getState());

			if (status.equalsIgnoreCase("DONE"))
				changeViewTo(indicesConstructedPanel); // change view when successful
		} catch (Exception err) {
			err.printStackTrace();
		}

		changeViewTo(searchTermResultsPanel);
	}

	/**
	 * Perform a search for top n
	 * 
	 * @param num
	 */
	public void handleSearchForTopN(int num) {
		System.out.println(num);
		changeViewTo(topNResultsPanel);
	}

	/**
	 * Go back to the previous screen
	 */
	public void goBack() {
		changeViewTo(indicesConstructedPanel);
	}

	/**
	 * Switch current frame
	 */
	private void changeViewTo(PanelInterface interfaceClass) {
		frame.setContentPane(interfaceClass.getPanel());
		frame.repaint();
		frame.revalidate();
	}

}
