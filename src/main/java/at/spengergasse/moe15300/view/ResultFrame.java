package at.spengergasse.moe15300.view;


import at.spengergasse.moe15300.model.graph.Graph;
import at.spengergasse.moe15300.model.graph.Node;
import at.spengergasse.moe15300.util.Loggable;
import at.spengergasse.moe15300.view.components.ResultPanel;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.StringJoiner;

@Component
public class ResultFrame extends JFrame {
    @Loggable
    private Logger log;

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

        final boolean connected = graph.isConnected();
        log.info("Zusammenhängend: {}", connected ? "ja" : "nein");

        if (connected) {
            log.info("Radius: {}", graph.getRadius());
            log.info("Durchmesser: {}", graph.getDiameter());
            logHeader("Zentren");
            graph.getCenters().forEach(node -> log.info(node.toString()));

            logHeader("E(x)");
            graph.getNodes().forEach(node -> log.info("E({}) = {}", node, graph.getEccentricity(node)));
        }

        logHeader("Komponenten");
        for (List<Node> component : graph.getComponents()) {
            StringJoiner joiner = new StringJoiner(", ");
            component.forEach(n -> joiner.add(n.toString()));
            log.info("Komponente: {}", joiner.toString());
        }

        logHeader("Artikulationen");
        graph.getArticulations().forEach(node -> log.info(node.toString()));

        logHeader("Brücken");
        graph.getBridges().forEach(log::info);

    }

    private void logHeader(String text) {
        final int length = 50;
        final char headChar = '=';

        text = " " + text + " ";

        final int textStart = (length - text.length()) / 2;

        StringBuilder sb = new StringBuilder(length);

        for(int i = 0; i < textStart; i++) {
            sb.append(headChar);
        }
        sb.append(text);
        while (sb.length() < length) {
            sb.append(headChar);
        }


        log.info(sb.toString());
    }
}
