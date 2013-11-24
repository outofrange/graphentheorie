package at.spengergasse.moe15300.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;
import at.spengergasse.moe15300.model.matrix.implementation.SquareMatrixHashMap;

public class Graph {
	private static final Logger log = LogManager.getLogger(Graph.class);
	public static final int INFINITE = -1;

	private final IntegerSquareMatrix adjazentsMatrix;
	private IntegerSquareMatrix wegMatrix;
	private IntegerSquareMatrix distanceMatrix;

	private boolean needToRebuildMatrices = true;

	public Graph(int vertices) {
		adjazentsMatrix = new SquareMatrixHashMap(vertices);
		wegMatrix = new SquareMatrixHashMap(vertices);
		distanceMatrix = new SquareMatrixHashMap(vertices);
	}

	public void addEdge(int from, int to, boolean undirected) {
		if (adjazentsMatrix.get(from, to) == 0) {
			adjazentsMatrix.set(from, to, 1);
			if (undirected) {
				adjazentsMatrix.set(to, from, 1);
			}
		} else {
			adjazentsMatrix.set(from, to, 0);
			if (undirected) {
				adjazentsMatrix.set(to, from, 0);
			}
		}

		needToRebuildMatrices = true;
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
}
