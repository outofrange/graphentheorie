package at.spengergasse.moe15300.model.matrix.implementation;

import java.util.ArrayList;
import java.util.List;

import at.spengergasse.moe15300.model.matrix.IntegerMatrix;
import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;

public class SquareMatrixArrayList extends IntegerSquareMatrix {
	private List<List<Integer>> matrix;

	public SquareMatrixArrayList() {
		super();
	}

	/**
	 * Creates a new square matrix
	 * 
	 * @param side
	 *            The size of each side of the two dimensional matrix
	 */
	public SquareMatrixArrayList(int side) {
		this.matrix = generateZeroMatrix(side, side);
	}

	public SquareMatrixArrayList(int[][] matrix) {
		this.matrix = generateZeroMatrix(matrix.length, matrix[0].length);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				this.matrix.get(i).set(j, matrix[i][j]);
			}
		}
	}

	private SquareMatrixArrayList(List<List<Integer>> matrix) {
		this.matrix = generateZeroMatrix(matrix.size(), matrix.get(0).size());

		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(0).size(); j++) {
				this.matrix.get(i).set(j, matrix.get(i).get(j));
			}
		}
	}

	@Override
	public Object clone() {
		return new SquareMatrixArrayList(matrix);
	}

	@Override
	public void expand(int nodesToExpand) {
		for (int i = 0; i < nodesToExpand; i++) {
			final int side = side();
			matrix.add(new ArrayList<Integer>());
			for (int row = 0; row < side; row++) {
				matrix.get(row).add(0);
			}
		}

	}

	protected List<List<Integer>> generateZeroMatrix(int height, int width) {
		List<List<Integer>> matrix = new ArrayList<List<Integer>>(height);
		for (int i = 0; i < height; i++) {
			matrix.add(i, new ArrayList<Integer>(width));
			for (int j = 0; j < width; j++) {
				matrix.get(i).add(0);
			}
		}

		return matrix;
	}

	@Override
	public int get(int row, int column) {
		return matrix.get(row).get(column);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matrix == null) ? 0 : matrix.hashCode());
		return result;
	}

	@Override
	public void multiply(IntegerMatrix otherMatrix) {
		if (width() != otherMatrix.height())
			throw new IllegalArgumentException(
					"otherMatrix' height has to be as big as this matrix' width.");

		List<List<Integer>> resultMatrix = generateZeroMatrix(height(), width());

		for (int zeile = 0; zeile < matrix.size(); zeile++) {
			for (int spalte = 0; spalte < matrix.get(zeile).size(); spalte++) {
				int sum = 0;
				for (int i = 0; i < matrix.size(); i++) {
					sum += get(zeile, i) * otherMatrix.get(i, spalte);
				}

				resultMatrix.get(zeile).set(spalte, sum);
			}
		}

		matrix = resultMatrix;
	}

	@Override
	public void set(int row, int column, int value) {
		matrix.get(row).set(column, value);
	}

	@Override
	public void setAll(int value) {
		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				set(i, j, value);
			}
		}
	}

	@Override
	public void setWholeDiagonal(int value) {
		for (int i = 0; i < matrix.size(); i++) {
			matrix.get(i).set(i, value);
		}
	}

	@Override
	public int side() {
		return matrix.size();
	}

}