package at.spengergasse.moe15300.model;

import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;
import at.spengergasse.moe15300.model.matrix.implementation.SquareMatrixArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Graph {
    private static final Logger log = LogManager.getLogger(Graph.class);
    public static final int INFINITE = -1;

    private IntegerSquareMatrix adjazentsMatrix;
    private IntegerSquareMatrix wegMatrix;
    private IntegerSquareMatrix distanceMatrix;

    private boolean needToRebuildMatrices = true;

    public void init() {
        adjazentsMatrix = new SquareMatrixArrayList();
        wegMatrix = new SquareMatrixArrayList();
        distanceMatrix = new SquareMatrixArrayList();
    }

    public void toggleEdge(int from, int to) {
        toggleCell(from, to);
        needToRebuildMatrices = true;
    }

    private void toggleCell(int row, int column) {
        adjazentsMatrix.set(row, column, adjazentsMatrix.get(row, column) == 0 ? 1 : 0);
    }

    public IntegerSquareMatrix getWegMatrix() {
        if (needToRebuildMatrices) {
            renewMatrices();
        }

        return (IntegerSquareMatrix) wegMatrix.clone();
    }

    public IntegerSquareMatrix getDistanzMatrix() {
        if (needToRebuildMatrices) {
            renewMatrices();
        }

        return (IntegerSquareMatrix) distanceMatrix.clone();
    }

    public int getVertices() {
        return adjazentsMatrix.side();
    }

    public void addVertice() {
        adjazentsMatrix.expand(1);
        wegMatrix.expand(1);
        distanceMatrix.expand(1);
        needToRebuildMatrices = true;
    }

    public void removeVertice() {
        adjazentsMatrix.shrink(1);
        wegMatrix.shrink(1);
        distanceMatrix.shrink(1);
        needToRebuildMatrices = true;
    }

    private void renewMatrices() {
        IntegerSquareMatrix multiplyMatrix = (IntegerSquareMatrix) adjazentsMatrix
                .clone();
        IntegerSquareMatrix multiplyMatrixFromBefore = null;
        IntegerSquareMatrix multiplyMatrixFromBeforeBefore = null;

        distanceMatrix.setAll(INFINITE);
        distanceMatrix.setWholeDiagonal(0);

        wegMatrix.setAll(0);
        wegMatrix.setWholeDiagonal(1);

        final int vertices = adjazentsMatrix.side();

        for (int distance = 1; distance < vertices
                && !multiplyMatrix.equals(multiplyMatrixFromBeforeBefore); distance++) {
            log.trace("Trying with distance set to " + distance);
            for (int row = 0; row < vertices; row++) {
                for (int column = 0; column < vertices; column++) {
                    if (distanceMatrix.get(row, column) == INFINITE
                            && multiplyMatrix.get(row, column) != 0) {
                        distanceMatrix.set(row, column, distance);
                        wegMatrix.set(row, column, 1);
                    }
                }
            }

            multiplyMatrixFromBeforeBefore = multiplyMatrixFromBefore;
            multiplyMatrixFromBefore = (IntegerSquareMatrix) multiplyMatrix
                    .clone();
            multiplyMatrix.multiply(adjazentsMatrix);
        }

        needToRebuildMatrices = false;
    }

    public void iterateThroughTheGraph() {
        for (int i = 0; i < adjazentsMatrix.side(); i++) {
            for (int j = 0; j < adjazentsMatrix.side(); j++) {
                adjazentsMatrix.get(i, j);
            }
        }
    }

    public boolean isDirected() {
        int side = getVertices();
        for (int i = 0; i < side; i++) {
            for (int j = i+1; j < side; j++) {
                if (adjazentsMatrix.get(i, j) != adjazentsMatrix.get(j, i)) {
                    return true;
                }
            }
        }

        return false;
    }
}
