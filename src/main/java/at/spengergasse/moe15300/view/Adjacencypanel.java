package at.spengergasse.moe15300.view;

import at.spengergasse.moe15300.util.Loggable;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


final class AdjacencyPanel extends JPanel {
    @Loggable
    private Logger log;
    private static final int GAP = 5;

    private final AdjacencyFrame parent;
    private final List<List<NodeBox>> boxes;
    private final GridLayout gridLayout;
    
    private int nodeCount;


    public AdjacencyPanel(final AdjacencyFrame parent, final int initialNodes) {
        this.parent = parent;
        this.nodeCount = 0;

        gridLayout = new GridLayout();
        gridLayout.setHgap(GAP);
        gridLayout.setVgap(GAP);

        setLayout(gridLayout);

        boxes = new ArrayList<>();
        for (int i = 0; i < initialNodes; i++) {
            addNode();
        }
    }

    public void addNode() {
        log.debug("Adding node. Currently  " + nodeCount + " nodes displayed.");

        for (int row = 0; row < nodeCount; row++) {
            boxes.get(row).add(createNewNodeBox(nodeCount, row));
        }

        boxes.add(new ArrayList<NodeBox>());
        for (int i = 0; i <= nodeCount; i++) {
            boxes.get(nodeCount).add(createNewNodeBox(i, nodeCount));
        }

        nodeCount++;
        createLayoutWithNodes();
    }

    public void removeNode() {
        if (nodeCount > 2) {
            boxes.remove(boxes.size() - 1);
            for (int i = 0; i < boxes.size(); i++) {
                boxes.get(i).remove(boxes.get(i).size() - 1);
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
            for (NodeBox box : row) {
                add(box);
            }
        }

        validate();
        repaint();
    }

    private NodeBox createNewNodeBox(final int x, final int y) {
        final NodeBox checkBox = new NodeBox(x, y);

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleButton(x, y);
                if (!parent.isGraphDirected() && x != y) {
                    toggleButton(y, x);
                }
            }
        });

        return checkBox;
    }

    public void toggleButton(final int x, final int y) {
        boxes.get(y).get(x).toggleButton();
    }
}