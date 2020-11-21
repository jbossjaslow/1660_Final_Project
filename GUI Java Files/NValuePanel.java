import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class NValuePanel implements PanelInterface {

	private InvertedIndicesRunner viewModel;
	private JPanel panel;
	private SpringLayout springLayout;
	private JTextField EnterTopNTextField;
	private JButton SearchButton;

	public NValuePanel(InvertedIndicesRunner viewModel, JFrame frame) {
		this.viewModel = viewModel;
		initialize();
	}

	private void initialize() {
		panel = new JPanel();
		springLayout = new SpringLayout();
		panel.setLayout(springLayout);

		JLabel SearchLabel = new JLabel("Enter Your N Value");
		SearchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, SearchLabel, 50, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, SearchLabel, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, SearchLabel, 0, SpringLayout.EAST, panel);
		panel.add(SearchLabel);

		EnterTopNTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, EnterTopNTextField, 150, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, EnterTopNTextField, -150, SpringLayout.EAST, panel);
		EnterTopNTextField.setToolTipText("Type Your N");
		springLayout.putConstraint(SpringLayout.NORTH, EnterTopNTextField, 44, SpringLayout.SOUTH, SearchLabel);
		panel.add(EnterTopNTextField);
		EnterTopNTextField.setColumns(10);

		SearchButton = new JButton("Search");
		SearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchForTopN();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, SearchButton, 53, SpringLayout.SOUTH, EnterTopNTextField);
		springLayout.putConstraint(SpringLayout.WEST, SearchButton, 165, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, SearchButton, 103, SpringLayout.SOUTH, EnterTopNTextField);
		springLayout.putConstraint(SpringLayout.EAST, SearchButton, -165, SpringLayout.EAST, panel);
		panel.add(SearchButton);
	}

	private void searchForTopN() {
		int num;
		try {
			num = Integer.parseInt(EnterTopNTextField.getText());
		} catch (NumberFormatException e) {
			System.out.println(e);
			return;
		}

		viewModel.handleSearchForTopN(num);
	}

	/**
	 * Interface methods
	 */
	public JPanel getPanel() {
		return panel;
	}
}
