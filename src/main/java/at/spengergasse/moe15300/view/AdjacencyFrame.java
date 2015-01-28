package at.spengergasse.moe15300.view;


import at.spengergasse.moe15300.model.graph.Graph;
import at.spengergasse.moe15300.util.AppContextProvider;
import at.spengergasse.moe15300.util.Loggable;
import at.spengergasse.moe15300.view.components.AdjacencyPanel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AdjacencyFrame extends JFrame {
    private static final Pattern INPUT_PATTERN = Pattern.compile("[^\\d]*(\\d+)[^\\d]+(\\d+)[^\\d]*");

    @Loggable
    private Logger log;

    private JPanel mainPanel, controlPanel, directedPanel, nodeControlPanel, computePanel, computeAutoPanel;
    private JButton addNodeButton, removeNodeButton, computeResultsButton;
    @Autowired
    private AdjacencyPanel checkboxPanel;
    private JLabel directedLabel, autoComputeLabel;
    private JCheckBox directedBox, autoCompute;
    private JTextField quickInput;

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
        quickInput = new JTextField();

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
        controlPanel.add(quickInput, BorderLayout.NORTH);

        nodeControlPanel.add(addNodeButton);
        nodeControlPanel.add(removeNodeButton);

        // controlPanel.add(directedPanel, BorderLayout.EAST);
        // directedPanel.add(directedLabel);
        // directedPanel.add(directedBox);

        computePanel.add(computeResultsButton, BorderLayout.CENTER);
        computePanel.add(computeAutoPanel, BorderLayout.EAST);

        computeAutoPanel.add(autoComputeLabel);
        computeAutoPanel.add(autoCompute);
    }

    private void addListeners() {
        addNodeButton.addActionListener(e -> {
            checkboxPanel.addNode();
            recomputeIfEnabled();
        });
        removeNodeButton.addActionListener(e -> {
            checkboxPanel.removeNode();
            recomputeIfEnabled();
        });
        computeResultsButton.addActionListener(e -> computeResults());

        quickInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    final String input = quickInput.getText();
                    log.debug("Text input: {}", input);


                    Matcher m = INPUT_PATTERN.matcher(input);
                    if (m.matches()) {
                        int from = Integer.valueOf(m.group(1)) - 1;
                        int to = Integer.valueOf(m.group(2)) - 1;

                        try {
                            checkboxPanel.changeEdge(from, to);
                            quickInput.setText("");
                        } catch (IllegalArgumentException ex) {
                            log.warn("Illegal input! {}", ex);
                        }
                    } else {
                        log.warn("Enter two numbers!");
                    }
                }
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
