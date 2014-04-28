package at.spengergasse.moe15300.view;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Mainframe extends JFrame {
    private static final int INITIAL_NODES = 5;
    private static final Logger log = LogManager.getLogger(Mainframe.class);
    private JPanel panel;
    private JButton buttonAddNode;



    public Mainframe() {
        init();
        panel = new JPanel(new BorderLayout());
        buttonAddNode = new JButton("Add Node");
        panel.add(buttonAddNode, BorderLayout.NORTH);
        final CheckboxPanel checkboxPanel = new CheckboxPanel(INITIAL_NODES);
        panel.add(checkboxPanel, BorderLayout.CENTER);

        add(panel);

        int width = checkboxPanel.getMinimumDimension().width;
        int height = checkboxPanel.getMinimumDimension().height + buttonAddNode.getHeight();
        Dimension d = new Dimension(width, height);
        setSize(d);
        setMinimumSize(d);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init() {


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            log.warn("Couldn't set look and feel to system default.", e);
        }

        new Mainframe();
    }
}
