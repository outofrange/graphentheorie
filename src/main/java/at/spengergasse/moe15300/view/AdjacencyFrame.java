package at.spengergasse.moe15300.view;


import at.spengergasse.moe15300.model.Graph;
import at.spengergasse.moe15300.util.AppContextProvider;
import at.spengergasse.moe15300.util.Loggable;
import at.spengergasse.moe15300.view.components.AdjacencyPanel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class AdjacencyFrame extends JFrame {
    @Loggable
    private Logger log;

    private JPanel mainPanel, controlPanel, directedPanel, nodeControlPanel, computePanel, computeAutoPanel;
    private JButton addNodeButton, removeNodeButton, computeResultsButton;
    @Autowired
    private AdjacencyPanel checkboxPanel;
    private JLabel directedLabel, autoComputeLabel;
    private JCheckBox directedBox, autoCompute;

    public void init() {
        mainPanel = new JPanel(new BorderLayout());
        controlPanel = new JPanel(new BorderLayout());
        computePanel = new JPanel(new BorderLayout());
        directedPanel = new JPanel(new FlowLayout());
        nodeControlPanel = new JPanel(new GridLayout(1, 2));
        computeAutoPanel = new JPanel(new GridLayout(1, 2));

        addNodeButton = new JButton("Add Node");
        removeNodeButton = new JButton("Remove Node");
        computeResultsButton = new JButton("Compute Results");
        directedLabel = new JLabel("Directed?");
        autoComputeLabel = new JLabel("Auto?");
        directedBox = new JCheckBox();
        autoCompute = new JCheckBox();

        createLayout();
        addListeners();
        setWindowDefaults();

        setVisible(true);
    }

    private void setWindowDefaults() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Adjacencies");
        setSize(300, 300);
        setLocationRelativeTo(null);
    }

    private void createLayout() {
        add(mainPanel);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(checkboxPanel, BorderLayout.CENTER);
        mainPanel.add(computePanel, BorderLayout.SOUTH);

        controlPanel.add(nodeControlPanel, BorderLayout.CENTER);
        controlPanel.add(directedPanel, BorderLayout.EAST);

        nodeControlPanel.add(addNodeButton);
        nodeControlPanel.add(removeNodeButton);

        directedPanel.add(directedLabel);
        directedPanel.add(directedBox);

        computePanel.add(computeResultsButton, BorderLayout.CENTER);
        computePanel.add(computeAutoPanel, BorderLayout.EAST);

        computeAutoPanel.add(autoComputeLabel);
        computeAutoPanel.add(autoCompute);
    }

    private void addListeners() {
        addNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkboxPanel.addNode();
                recomputeIfEnabled();
            }
        });
        removeNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkboxPanel.removeNode();
                recomputeIfEnabled();
            }
        });
        computeResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computeResults();
            }
        });
    }

    public boolean isEdgeDirected() {
        return directedBox.isSelected();
    }

    public JCheckBox getDirectedBox() {
        return directedBox;
    }

    public boolean isAutoComputeEnabled() {
        return autoCompute.isSelected();
    }

    private void computeResults() {
        Graph graph = AppContextProvider.getContext().getBean("graph", Graph.class);
        log.info("Wegmatrix:\n" + graph.getWegMatrix().toString());
        log.info("Distanzmatrix:\n" + graph.getDistanzMatrix().toString());

        ResultFrame results = AppContextProvider.getContext().getBean("resultFrame", ResultFrame.class);
        results.update();
        results.setVisible(true);
    }

    public void recomputeIfEnabled() {
        if (isAutoComputeEnabled()) {
            computeResults();
        }
    }
}
