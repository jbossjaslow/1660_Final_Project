import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class IndicesConstructedPanel implements PanelInterface {

	private InvertedIndicesRunner viewModel;
	private JPanel panel;
	private SpringLayout springLayout;

	public IndicesConstructedPanel(InvertedIndicesRunner viewModel, JFrame frame) {
		this.viewModel = viewModel;
		initialize();
	}

	private void initialize() {
		panel = new JPanel();
		springLayout = new SpringLayout();
		panel.setLayout(springLayout);

		JLabel EngineLoadedLabel = new JLabel("Engine was loaded");
		springLayout.putConstraint(SpringLayout.NORTH, EngineLoadedLabel, 50, SpringLayout.NORTH, panel);
		EngineLoadedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.WEST, EngineLoadedLabel, 100, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, EngineLoadedLabel, -100, SpringLayout.EAST, panel);
		panel.add(EngineLoadedLabel);

		JLabel AmpersandLabel = new JLabel("&");
		springLayout.putConstraint(SpringLayout.NORTH, AmpersandLabel, 7, SpringLayout.SOUTH, EngineLoadedLabel);
		springLayout.putConstraint(SpringLayout.WEST, AmpersandLabel, 0, SpringLayout.WEST, EngineLoadedLabel);
		springLayout.putConstraint(SpringLayout.EAST, AmpersandLabel, 0, SpringLayout.EAST, EngineLoadedLabel);
		AmpersandLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(AmpersandLabel);

		JLabel SuccessfullyLabel = new JLabel("Inverted indices were constructed successfully!");
		springLayout.putConstraint(SpringLayout.NORTH, SuccessfullyLabel, 7, SpringLayout.SOUTH, AmpersandLabel);
		SuccessfullyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.WEST, SuccessfullyLabel, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, SuccessfullyLabel, 0, SpringLayout.EAST, panel);
		panel.add(SuccessfullyLabel);

		JLabel SelectActionLabel = new JLabel("Please select action");
		springLayout.putConstraint(SpringLayout.NORTH, SelectActionLabel, 40, SpringLayout.SOUTH, SuccessfullyLabel);
		SelectActionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.WEST, SelectActionLabel, 0, SpringLayout.WEST, SuccessfullyLabel);
		springLayout.putConstraint(SpringLayout.EAST, SelectActionLabel, 0, SpringLayout.EAST, SuccessfullyLabel);
		panel.add(SelectActionLabel);

		JButton SearchButton = new JButton("Search for Term");
		SearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchSelected(e);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, SearchButton, 27, SpringLayout.SOUTH, SelectActionLabel);
		springLayout.putConstraint(SpringLayout.WEST, SearchButton, 125, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, SearchButton, -125, SpringLayout.EAST, panel);
		panel.add(SearchButton);

		JButton TopNButton = new JButton("Top-N");
		TopNButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				topNSelected(e);
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, TopNButton, 20, SpringLayout.SOUTH, SearchButton);
		springLayout.putConstraint(SpringLayout.WEST, TopNButton, 125, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.EAST, TopNButton, -125, SpringLayout.EAST, panel);
		panel.add(TopNButton);
	}

	private void searchSelected(ActionEvent e) {
		viewModel.handleChangeToSearchTermPanel();
	}

	private void topNSelected(ActionEvent e) {
		viewModel.handleChangeToSearchTopNPanel();
	}

	/**
	 * Interface methods
	 */
	public JPanel getPanel() {
		return panel;
	}
}
