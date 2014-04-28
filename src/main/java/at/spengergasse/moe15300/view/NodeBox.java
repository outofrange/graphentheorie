package at.spengergasse.moe15300.view;

import javax.swing.*;

public class NodeBox extends JCheckBox {
    private final int row;
    private final int column;

    public NodeBox(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
