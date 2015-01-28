package at.spengergasse.moe15300.view.components;

import at.spengergasse.moe15300.model.graph.Graph;
import at.spengergasse.moe15300.model.graph.Node;
import at.spengergasse.moe15300.util.AppContextProvider;
import at.spengergasse.moe15300.util.Loggable;
import at.spengergasse.moe15300.view.AdjacencyFrame;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdjacencyPanel extends JPanel {
    public static final int INITIAL_NODES = 5;

    @Loggable
    private Logger log;
    private static final int GAP = 5;

    @Resource
    private AdjacencyFrame parent;

    @Resource
    private Graph graph;

    private List<List<NodeBox>> boxes;
    private GridLayout gridLayout;

    private int nodeCount;


    public void init() {
        this.nodeCount = 0;

        gridLayout = new GridLayout();
        gridLayout.setHgap(GAP);
        gridLayout.setVgap(GAP);

        setLayout(gridLayout);

        boxes = new ArrayList<>();
        for (int i = 0; i < INITIAL_NODES; i++) {
            addNode();
        }
    }

    public void addNode() {
        log.debug("Adding node. Currently {} nodes displayed.", nodeCount + 1);

        graph.addNode();

        for (int row = 0; row < nodeCount; row++) {
            boxes.get(row).add(createNewNodeBox(nodeCount, row));
        }

        boxes.add(new ArrayList<>());
        for (int i = 0; i <= nodeCount; i++) {
            boxes.get(nodeCount).add(createNewNodeBox(i, nodeCount));
        }

        nodeCount++;
        createLayoutWithNodes();
    }

    public void removeNode() {
        if (nodeCount > 2) {
            graph.removeLastNode();

            boxes.remove(boxes.size() - 1);
            for (List<NodeBox> box : boxes) {
                box.remove(box.size() - 1);
            }

            nodeCount--;
            createLayoutWithNodes();
        }
    }

    private void createLayoutWithNodes() {
        final int nodes = this.nodeCount;
        removeAll();

        gridLayout.setColumns(nodes);
        gridLayout.setRows(nodes);

        for (List<NodeBox> row : boxes) {
            row.forEach(this::add);
        }

        validate();
        repaint();
    }

    private NodeBox createNewNodeBox(final int x, final int y) {
        final NodeBox checkBox = AppContextProvider.getContext().getBean("nodeBox", NodeBox.class);
        checkBox.setX(x);
        checkBox.setY(y);

        checkBox.addActionListener(e -> changeEdge(x, y));

        return checkBox;
    }

    public void changeEdge(final int x, final int y) {
        if (x >= nodeCount || y >= nodeCount || x < 0 || y < 0) {
            throw new IllegalArgumentException("x and y have to be between 0 and " + nodeCount);
        }

        if (x != y) {
            if (! boxes.get(x).get(y).isMarked()) {
                graph.addEdge(new Node(x), new Node(y));
            } else {
                graph.removeEdge(new Node(x), new Node(y));
            }
            boxes.get(x).get(y).toggleButton();
            boxes.get(y).get(x).toggleButton();

            if (graph.isDirected()) {

                parent.getDirectedBox().setSelected(true);
                parent.getDirectedBox().setEnabled(false);
            } else {
                parent.getDirectedBox().setEnabled(true);
            }

            parent.recomputeIfEnabled();
        }
    }

    public int getNodeCount() {
        return nodeCount;
    }
}
