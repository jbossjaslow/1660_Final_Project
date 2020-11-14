import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

public class SearchTermResultsPanel implements PanelInterface {

	private InvertedIndicesRunner viewModel;
	private JPanel panel;
	private SpringLayout springLayout;
	private JTable table;

	private String[] columnNames = { "Doc ID", "Doc Folder", "Doc Name", "Frequencies" };
	private Object[][] tableData = { { 1, "histories", "1kinghenryiv", 169 }, { 2, "histories", "1kinghenryiv", 160 }, { 3, "histories", "2kinghenryiv", 179 }, { 4, "histories", "2kinghenryiv", 340 } };

	public SearchTermResultsPanel(InvertedIndicesRunner viewModel, JFrame frame) {
		this.viewModel = viewModel;
		initialize();
	}

	public void constructTableData(int[] docIDs, String[] docFolders, String[] docNames, int[] frequencies) {
		double totalLength = docIDs.length + docFolders.length + docNames.length + frequencies.length;
		if (totalLength / 4 != frequencies.length)
			return;

		tableData = new Object[frequencies.length][4];
		for (int i = 0; i < frequencies.length; i++) {
			tableData[i][0] = docIDs[i];
			tableData[i][1] = docFolders[i];
			tableData[i][2] = docNames[i];
			tableData[i][3] = frequencies[i];
		}
	}

	private void initialize() {
		panel = new JPanel();
		springLayout = new SpringLayout();
		panel.setLayout(springLayout);

		JButton BackButton = new JButton("Back to Search");
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, BackButton, 10, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, BackButton, -10, SpringLayout.EAST, panel);
		panel.add(BackButton);

		JLabel TermSearchedForLabel = new JLabel("You searched for the term: KING");
		springLayout.putConstraint(SpringLayout.NORTH, TermSearchedForLabel, 62, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, TermSearchedForLabel, 50, SpringLayout.WEST, panel);
		panel.add(TermSearchedForLabel);

		JLabel TimeTakenLabel = new JLabel("Your search was executed in XXX ms");
		springLayout.putConstraint(SpringLayout.NORTH, TimeTakenLabel, 22, SpringLayout.SOUTH, TermSearchedForLabel);
		springLayout.putConstraint(SpringLayout.WEST, TimeTakenLabel, 50, SpringLayout.WEST, panel);
		panel.add(TimeTakenLabel);

		table = new JTable(tableData, columnNames);
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);

		JScrollPane scrollPane = new JScrollPane(table);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 24, SpringLayout.SOUTH, TimeTakenLabel);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, panel);
		panel.add(scrollPane);
	}

	private void goBack() {
		viewModel.goBack();
	}

	/**
	 * Interface methods
	 */
	public JPanel getPanel() {
		return panel;
	}
}
