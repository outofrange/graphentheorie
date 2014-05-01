package at.spengergasse.moe15300.view;


import at.spengergasse.moe15300.model.Graph;
import at.spengergasse.moe15300.util.AppContextProvider;
import at.spengergasse.moe15300.view.components.ResultPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;

@Component
public class ResultFrame extends JFrame {
    @Resource
    private Graph graph;
    @Resource
    private ResultPanel wegPanel;
    @Resource
    private ResultPanel distanzPanel;

    public void init() {
        setLayout(new GridLayout(2, 1));
        add(wegPanel);
        add(distanzPanel);
        setTitle("Results");
        setSize(500, 500);
    }

    public void update() {
        wegPanel.setMatrix(graph.getWegMatrix(), "Wegmatrix");
        distanzPanel.setMatrix(graph.getDistanzMatrix(), "Distanzmatrix");
    }
}
