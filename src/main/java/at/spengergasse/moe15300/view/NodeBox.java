package at.spengergasse.moe15300.view;

import at.spengergasse.moe15300.util.Loggable;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;

final class NodeBox extends JButton {
    @Loggable
    private Logger log;

    private final Color bgNotSelected;
    private final int posX;
    private final int posY;
    private boolean marked;

    public NodeBox(int posX, int posY) {
        log.trace("Creating new node " + posX + " / " + posY);

        this.posX = posX;
        this.posY = posY;
        this.marked = false;
        this.bgNotSelected = getBackground();
    }

    void toggleButton() {
        log.debug("Toggling " + toString());
        marked = !marked;
        setBackground(marked ? Color.BLACK : bgNotSelected);
    }

    @Override
    public String toString() {
        return "Button " + posX + " / " + posY + " (" + (!marked ? "not " : "") + "selected)";
    }
}
