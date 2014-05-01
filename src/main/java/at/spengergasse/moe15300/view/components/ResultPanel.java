package at.spengergasse.moe15300.view.components;

import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@Scope("prototype")
public class ResultPanel extends JPanel {
    private IntegerSquareMatrix matrix;
    private String title;

    public void setMatrix(IntegerSquareMatrix matrix, String title) {
        this.matrix = matrix;
        this.title = title;
        createLayout();
    }

    private void createLayout() {
        removeAll();
        setLayout(new BorderLayout());
        add(new JLabel(title), BorderLayout.NORTH);


        int side = matrix.side();
        JPanel results = new JPanel(new GridLayout(side, side));

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                results.add(new JLabel(String.valueOf(matrix.get(i, j))));
            }
        }

        add(results, BorderLayout.CENTER);
    }


}
