package at.spengergasse.moe15300;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

import at.spengergasse.moe15300.model.Graph;

public class AddEdgeActionListener implements ActionListener {
	private Graph graph;
	private int from;
	private int to;
	private boolean undirected;
	private JButton button;
	private JTextArea targetLabel;

	public AddEdgeActionListener(Graph graph, JTextArea targetLabel,
			JButton button, int from, int to, boolean undirected) {
		this.graph = graph;
		this.from = from;
		this.to = to;
		this.undirected = undirected;
		this.button = button;
		this.targetLabel = targetLabel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		graph.addEdge(from, to, undirected);
		if (button.getText().equals("1")) {
			button.setText("0");
		} else {
			button.setText("1");
		}

		targetLabel.setText(graph.getDistanzMatrix().toString());
	}

}
