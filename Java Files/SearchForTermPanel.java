import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class SearchForTermPanel implements PanelInterface {

	private InvertedIndicesRunner viewModel;
	private JPanel panel;
	private SpringLayout springLayout;
	private JTextField EnterWordTextField;
	private JButton SearchButton;

	public SearchForTermPanel(InvertedIndicesRunner viewModel, JFrame frame) {
		this.viewModel = viewModel;
		initialize();
	}

	private void initialize() {
		panel = new JPanel();
		springLayout = new SpringLayout();
		panel.setLayout(springLayout);

		JLabel SearchLabel = new JLabel("Enter Your Search Term");
		SearchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, SearchLabel, 50, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, SearchLabel, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, SearchLabel, 0, SpringLayout.EAST, panel);
		panel.add(SearchLabel);

		EnterWordTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, EnterWordTextField, 150, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, EnterWordTextField, -150, SpringLayout.EAST, panel);
		EnterWordTextField.setToolTipText("Enter your text here");
		springLayout.putConstraint(SpringLayout.NORTH, EnterWordTextField, 44, SpringLayout.SOUTH, SearchLabel);
		panel.add(EnterWordTextField);
		EnterWordTextField.setColumns(10);

		SearchButton = new JButton("Search");
		SearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchForTerm();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, SearchButton, 53, SpringLayout.SOUTH, EnterWordTextField);
		springLayout.putConstraint(SpringLayout.WEST, SearchButton, 165, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, SearchButton, 103, SpringLayout.SOUTH, EnterWordTextField);
		springLayout.putConstraint(SpringLayout.EAST, SearchButton, -165, SpringLayout.EAST, panel);
		panel.add(SearchButton);
	}

	private void searchForTerm() {
		viewModel.handleSearchForTerm(EnterWordTextField.getText());
	}

	/**
	 * Interface methods
	 */
	public JPanel getPanel() {
		return panel;
	}
}
