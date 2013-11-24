package at.spengergasse.moe15300.model.matrix;

public abstract class IntegerMatrix implements Cloneable {
	public IntegerMatrix() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IntegerMatrix))
			return false;

		IntegerMatrix other = (IntegerMatrix) obj;

		if (width() != other.width() || height() != other.height())
			return false;

		for (int row = 0; row < height(); row++) {
			for (int column = 0; column < width(); column++) {
				if (get(row, column) != other.get(row, column))
					return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		final String linebreak = "\n";

		StringBuilder sb = new StringBuilder(height() * width() * 2);
		for (int row = 0; row < height(); row++) {
			for (int column = 0; column < width(); column++) {
				sb.append(String.format("%-3d", get(row, column)));
			}
			sb.append(linebreak);
		}

		return sb.toString();
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
			if (matrix[row].length != lengthFirstRow) {
				return false;
			}
		}

		return true;
	}

	public abstract void multiply(IntegerMatrix otherMatrix);

	public abstract int get(int row, int column);

	public abstract void set(int row, int column, int value);

	public abstract void setAll(int value);

	public abstract int width();

	public abstract int height();

	public abstract Object clone();
}
