import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class LoadEnginePanel implements PanelInterface {

	private InvertedIndicesRunner viewModel;
	private JTextPane filesList;
	private JPanel panel;

	private String files = "";

	/**
	 * Create the panel.
	 */
	public LoadEnginePanel(InvertedIndicesRunner viewModel, JFrame frame) {
		this.viewModel = viewModel;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panel = new JPanel();
		SpringLayout springLayout = new SpringLayout();
		panel.setLayout(springLayout);

		JLabel LoadButtonLabel = new JLabel("Load My Engine");
		LoadButtonLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		LoadButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, LoadButtonLabel, 50, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, LoadButtonLabel, 150, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, LoadButtonLabel, -150, SpringLayout.EAST, panel);
		panel.add(LoadButtonLabel);

		JButton ChooseFilesButton = new JButton("Choose Files");
		ChooseFilesButton.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		springLayout.putConstraint(SpringLayout.NORTH, ChooseFilesButton, 50, SpringLayout.NORTH, LoadButtonLabel);
		springLayout.putConstraint(SpringLayout.WEST, ChooseFilesButton, 150, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, ChooseFilesButton, -150, SpringLayout.EAST, panel);
		panel.add(ChooseFilesButton);
		ChooseFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFilesSelected(e);
			}
		});

		JButton ConstructIndicesButton = new JButton("Construct Inverted Indices");
		springLayout.putConstraint(SpringLayout.WEST, ConstructIndicesButton, 100, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, ConstructIndicesButton, -100, SpringLayout.EAST, panel);
		panel.add(ConstructIndicesButton);
		ConstructIndicesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				constructInvertedIndicesSelected(e);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, ConstructIndicesButton, 25, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 25, SpringLayout.SOUTH, ChooseFilesButton);
		ScrollPaneLayout scrollPaneLayout = new ScrollPaneLayout();
		scrollPane.setLayout(scrollPaneLayout);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 100, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -100, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -100, SpringLayout.EAST, panel);
		panel.add(scrollPane);

		JTextPane FilesList = new JTextPane();
		scrollPane.setViewportView(FilesList);
		FilesList.setEditable(false);
		filesList = FilesList;
	}

	/**
	 * Private functions
	 */
	private void chooseFilesSelected(ActionEvent e) {
		viewModel.handleChooseFiles();
	}

	private void constructInvertedIndicesSelected(ActionEvent e) {
		if (!files.equals(""))
			viewModel.handleConstructIndices(files);
	}

	/**
	 * Public functions
	 */
	public void updateFilesSelectedList(String fileList) {
		filesList.setText(fileList);
		files = fileList;
	}

	/**
	 * Interface methods
	 */
	public JPanel getPanel() {
		return panel;
	}
}
