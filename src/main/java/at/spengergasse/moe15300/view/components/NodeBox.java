package at.spengergasse.moe15300.view.components;

import at.spengergasse.moe15300.util.Loggable;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Scope("prototype")
final class NodeBox extends JButton {
    @Loggable
    private Logger log;

    private final Color bgNotSelected;
    private int posX;
    private int posY;
    private boolean marked;

    public NodeBox() {
        this.marked = false;
        this.bgNotSelected = getBackground();
    }

	public void setX(int x) {
		this.posX = x;
	}

	public void setY(int y) {
		this.posY = y;
	}

    void toggleButton() {
        if (posX != posY) {
            log.debug("Toggling " + toString());
            marked = !marked;
            setBackground(marked ? Color.BLACK : bgNotSelected);
            setText(marked ? "1" : "");
        }
    }

    @Override
    public String toString() {
        return "Button " + posX + " / " + posY + " (" + (!marked ? "not " : "") + "selected)";
    }
}
