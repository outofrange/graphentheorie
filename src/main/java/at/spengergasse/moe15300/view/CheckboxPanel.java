package at.spengergasse.moe15300.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class CheckboxPanel extends JPanel {
    private int boxes;
    private List<List<NodeBox>> edges;
    private final GridLayout gridLayout;


    public CheckboxPanel(int initialBoxes) {
        this.boxes = initialBoxes;
        gridLayout = new GridLayout(initialBoxes, initialBoxes);

        setLayout(gridLayout);

        edges = new ArrayList<>();

        for (int i = 0; i < initialBoxes; i++) {
            edges.add(new ArrayList<NodeBox>());

            for (int j = 0; j < initialBoxes; j++) {
                final NodeBox checkBox = new NodeBox(i, j);

                checkBox.setMargin(new Insets(1, 1, 1, 1));

                edges.get(i).add(checkBox);
                add(checkBox);
            }
        }
    }

    public Dimension getMinimumDimension() {
        return gridLayout.minimumLayoutSize(this);
    }
}
