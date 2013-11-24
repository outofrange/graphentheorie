package at.spengergasse.moe15300;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import at.spengergasse.moe15300.model.Graph;

public class Graphentheorie {
	private static final String INITIAL_BUTTON_TEXT = "0";
	private static final int INITIAL_VERTICES = 5;
	private Graph graph = new Graph(INITIAL_VERTICES);

	private List<List<JButton>> buttons;

	private JFrame frmGraphentheorie;
	private JPanel panel_1;
	private JTextArea distantMatrixLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graphentheorie window = new Graphentheorie();
					window.frmGraphentheorie.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Graphentheorie() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGraphentheorie = new JFrame();
		frmGraphentheorie.setTitle("Graphentheorie");
		frmGraphentheorie.setBounds(100, 100, 450, 300);
		frmGraphentheorie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel_1 = new JPanel();
		frmGraphentheorie.getContentPane().add(panel_1, BorderLayout.EAST);
		distantMatrixLabel = new JTextArea("");
		panel_1.add(distantMatrixLabel);

		frmGraphentheorie.getContentPane().add(generateButtonPanel(),
				BorderLayout.CENTER);

	}

	private JPanel generateButtonPanel() {
		final int vertices = graph.getVertices();

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(vertices, vertices, 0, 0));

		buttons = new ArrayList<List<JButton>>(vertices);

		List<List<JButton>> matrix = new ArrayList<List<JButton>>(vertices);
		for (int i = 0; i < vertices; i++) {
			matrix.add(new ArrayList<JButton>(vertices));
			for (int j = 0; j < vertices; j++) {
				JButton newButton = new JButton(INITIAL_BUTTON_TEXT);

				newButton.addActionListener(new AddEdgeActionListener(graph,
						distantMatrixLabel, newButton, i, j, true));

				panel.add(newButton);
				matrix.get(i).add(newButton);
			}
		}

		return panel;
	}
}
