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
import com.google.api.gax.paging.Page;
import com.google.api.services.dataproc.Dataproc;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
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
	private GoogleCredentials credentials;
	private JobPlacement jobPlacement;
	private int invertedRandom;

	private String projectId = "our-audio-292023";
	private String clusterName = "cluster-ebba";
	private String hdfsURL = "hdfs:///user/jbossjaslow/";
	private String region = "us-central1";

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
		String appName = "wordCountAndTopN";

		Random rand = new Random();
		invertedRandom = rand.nextInt(1000000);
		System.out.println("Current random: " + invertedRandom);
		String inputDir = hdfsURL + "input"; // input
		String outputDir = hdfsURL + "invertedIndicesOutput" + invertedRandom; // output

		ArrayList<String> argsList = new ArrayList<String>();
		argsList.add(inputDir); // add input path
		argsList.add(outputDir); // add output path
		argsList.add(fileList); // files white list

		try {
			// Send job
			InputStream stream = this.getClass().getResourceAsStream("/dataproc_creds.json");
			credentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
			HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
			dataproc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), requestInitializer).setApplicationName(appName).build();

			if (executeJob("IIDriver", hdfsURL + "invertedIndices.jar", argsList))
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
		String storageBucket = "dataproc-staging-us-central1-834767494839-8vpuiylu";

		Random rand = new Random();
		int randy = rand.nextInt(1000000);
		String inputDir = hdfsURL + "invertedIndicesOutput" + invertedRandom; // input
		String outputDir = "gs://" + storageBucket + "/" + "output" + randy; // input

		ArrayList<String> argsList = new ArrayList<String>();
		argsList.add(inputDir); // add input path
		argsList.add(outputDir); // add output path
		argsList.add(term);

		long startTime = System.currentTimeMillis();

		if (!executeJob("WordSearchDriver", hdfsURL + "wordSearch.jar", argsList))
			return;

		long endTime = System.currentTimeMillis();

		String outputBucket = "output" + randy;
		String result = collectResults(storageBucket, outputBucket);

		// print out inverted indices on UI using output generated above
		if (result.isEmpty()) {
			System.out.println("The output folder is missing or empty");
		} else {
			String[] tokens = result.split("\t"); // occurrence
			String word = tokens[0];
			String[] wordOccurrences = tokens[1].split(","); // length is total # of results
			int numResults = wordOccurrences.length - 1;

			ArrayList<WordSearchResult> resultList = new ArrayList<WordSearchResult>();
			for (int i = 0; i < numResults; i++) {
				String[] components = wordOccurrences[i].split("/");
				resultList.add(new WordSearchResult(components[0], components[1], Integer.parseInt(components[2])));
			}

			searchTermResultsPanel.constructTableData(resultList);
			searchTermResultsPanel.updateSearchTerm(word);
			searchTermResultsPanel.updateTimeTaken(endTime - startTime);
			changeViewTo(searchTermResultsPanel);
		}
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

	private boolean executeJob(String mainClass, String jarFileURI, ArrayList<String> argsList) {
		try {
			jobPlacement = new JobPlacement().setClusterName(clusterName);
			HadoopJob hadoopJob = new HadoopJob().setMainClass(mainClass).setJarFileUris(ImmutableList.of(jarFileURI)).setArgs(argsList);
			SubmitJobRequest jobRequest = new SubmitJobRequest().setJob(new Job().setPlacement(jobPlacement).setHadoopJob(hadoopJob));
			Job submittedJob = dataproc.projects().regions().jobs().submit(projectId, region, jobRequest).execute();

			String jobId = submittedJob.getReference().getJobId();
			Job job = dataproc.projects().regions().jobs().get(projectId, region, jobId).execute();
			String status = job.getStatus().getState();
			while (!(status.equals("DONE") || status.equals("CANCELLED") || status.equals("ERROR"))) {
				System.out.println("Status: " + status);
				Thread.sleep(500); // sleep for 0.5 seconds
				job = dataproc.projects().regions().jobs().get(projectId, region, jobId).execute();
				status = job.getStatus().getState();
			}

			System.out.println("Finished with status: " + status);
			return status.equals("DONE");

		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}

	private String collectResults(String storageBucket, String outputBucket) {
		String result = ""; // several results come back, most of which are empty
		try {
			Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
			Bucket bucket = storage.get(storageBucket);
			Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(outputBucket));
			for (Blob blob : blobs.iterateAll()) {
				String blobContent = new String(blob.getContent());
				if (!blobContent.isEmpty())
					result = blobContent;
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return result;
	}
}
