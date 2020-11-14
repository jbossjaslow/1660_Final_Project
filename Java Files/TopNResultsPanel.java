import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

public class TopNResultsPanel implements PanelInterface {

	private InvertedIndicesRunner viewModel;
	private JPanel panel;
	private SpringLayout springLayout;
	private JTable table;
	private JLabel TopNTermsLabel;

	private String[] columnNames = { "Term", "Total Frequencies" };
	private Object[][] tableData = { { "KING", 5000 }, { "HENRY", 4500 }, { "THE", 4000 }, { "FOURTH", 3500 }, { "SIR", 3000 }, { "WALTER", 2500 }, { "BLUNT", 2000 }, { "OWEN", 1500 }, { "GELNDOWER", 1000 }, { "RICHARD", 500 }, };

	public TopNResultsPanel(InvertedIndicesRunner viewModel, JFrame frame) {
		this.viewModel = viewModel;
		initialize();
	}

	private void initialize() {
		panel = new JPanel();
		springLayout = new SpringLayout();
		panel.setLayout(springLayout);

		JButton BackButton = new JButton("Back");
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, BackButton, 10, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, BackButton, -10, SpringLayout.EAST, panel);
		panel.add(BackButton);

		table = new JTable(tableData, columnNames);
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);

		JScrollPane scrollPane = new JScrollPane(table);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 100, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, panel);
		panel.add(scrollPane);

		TopNTermsLabel = new JLabel("Top-N Frequent Terms");
		springLayout.putConstraint(SpringLayout.WEST, TopNTermsLabel, 24, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, TopNTermsLabel, -19, SpringLayout.NORTH, scrollPane);
		panel.add(TopNTermsLabel);
	}

	public void constructTableData(String[] terms, int[] frequencies) {
		if (terms.length != frequencies.length)
			return;

		tableData = new Object[terms.length][2];
		for (int i = 0; i < terms.length; i++) {
			tableData[i][0] = terms[i];
			tableData[i][1] = frequencies[i];
		}
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
