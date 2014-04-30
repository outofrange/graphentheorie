package at.spengergasse.moe15300.model.matrix;

import java.util.List;

public abstract class IntegerSquareMatrix extends IntegerMatrix {
	public IntegerSquareMatrix() {
		super();
	}

	public void setWholeDiagonal(int value) {
		final int side = side();
		for (int i = 0; i < side; i++) {
			set(i, i, value);
		}
	}

	public abstract int side();

	@Override
	public int height() {
		return side();
	}

	@Override
	public int width() {
		return side();
	}

	public static boolean isValid(int[][] matrix) {
		final int rows = matrix.length;
		if (rows == 0) {
			return false;
		}

		final int lengthFirstRow = matrix[0].length;
		if (lengthFirstRow == 0) {
			return false;
		}

		for (int row = 1; row < rows; row++) {
			if (matrix[row].length != rows) {
				return false;
			}
		}

		return true;
	}

    public static boolean isValid(List<List<Integer>> matrix) {
        final int rows = matrix.size();
        if (rows == 0) {
            return false;
        }

        final int lengthFirstRow = matrix.get(0).size();
        if (lengthFirstRow == 0) {
            return false;
        }

        for (int row = 1; row < rows; row++) {
            if (matrix.get(row).size() != rows) {
                return false;
            }
        }

        return true;
    }

	public abstract void expand(int nodesToExpand);
}
