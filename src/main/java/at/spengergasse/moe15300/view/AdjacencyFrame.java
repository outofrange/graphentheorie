package at.spengergasse.moe15300.view;


import at.spengergasse.moe15300.util.AppContextProvider;
import at.spengergasse.moe15300.util.Loggable;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdjacencyFrame extends JFrame {
    @Loggable
    private Logger log;

    private final JPanel mainPanel, controlPanel, directedPanel, nodeControlPanel;
    private final JButton addNodeButton, removeNodeButton, computeResultsButton;
    private final AdjacencyPanel checkboxPanel;
    private final JLabel directedLabel;
    private final JCheckBox directedBox;


    public AdjacencyFrame() {
        mainPanel = new JPanel(new BorderLayout());
        checkboxPanel = AppContextProvider.getContext().getBean("adjacencyPanel", AdjacencyPanel.class);
        controlPanel = new JPanel(new BorderLayout());
        directedPanel = new JPanel(new FlowLayout());
        nodeControlPanel = new JPanel(new GridLayout(1, 2));

        addNodeButton = new JButton("Add Node");
        removeNodeButton = new JButton("Remove Node");
        computeResultsButton = new JButton("Compute Results");
        directedLabel = new JLabel("Directed? ");
        directedBox = new JCheckBox();

        createLayout();
        addListeners();
        setWindowDefaults();

        setVisible(true);
    }

    private void setWindowDefaults() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Adjacencies");
        setSize(300,300);
        setLocationRelativeTo(null);
    }

    private void createLayout() {
        add(mainPanel);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(checkboxPanel, BorderLayout.CENTER);
        mainPanel.add(computeResultsButton, BorderLayout.SOUTH);

        controlPanel.add(nodeControlPanel, BorderLayout.CENTER);
        controlPanel.add(directedPanel, BorderLayout.EAST);

        nodeControlPanel.add(addNodeButton);
        nodeControlPanel.add(removeNodeButton);

        directedPanel.add(directedLabel);
        directedPanel.add(directedBox);
    }

    private void addListeners() {
        addNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkboxPanel.addNode();
            }
        });
        removeNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkboxPanel.removeNode();
            }
        });
    }

    public boolean isGraphDirected() {
        return directedBox.isSelected();
    }
}
