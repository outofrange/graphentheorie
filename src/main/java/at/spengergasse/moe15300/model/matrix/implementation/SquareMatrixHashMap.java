package at.spengergasse.moe15300.model.matrix.implementation;

import java.util.HashMap;
import java.util.Map;

import at.spengergasse.moe15300.model.matrix.IntegerMatrix;
import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;

public class SquareMatrixHashMap extends IntegerSquareMatrix {
	private Map<Element, Integer> elements;
	private int defaultValue = 0;
	private int side;

	public SquareMatrixHashMap(int side) {
		elements = new HashMap<Element, Integer>();
		this.side = side;
	}

	private SquareMatrixHashMap(Map<Element, Integer> elements,
			int defaultValue, int side) {
		this.elements = new HashMap<Element, Integer>(elements);
		this.defaultValue = defaultValue;
		this.side = side;
	}

	private void validateRowAndColumn(int row, int column) {
		if (row < 0 || row >= side()) {
			throw new IllegalArgumentException(
					"Given row has to be greater or equal to zero and smaller than side: "
							+ side());
		}
		if (column < 0 || column >= side()) {
			throw new IllegalArgumentException(
					"Given column has to be greater or equal to zero and smaller than side: "
							+ side());
		}
	}

	@Override
	public void multiply(IntegerMatrix otherMatrix) {
		if (width() != otherMatrix.height())
			throw new IllegalArgumentException(
					"otherMatrix' height has to be as big as this matrix' width.");

		Map<Element, Integer> resultElements = new HashMap<Element, Integer>();

		for (int zeile = 0; zeile < side(); zeile++) {
			for (int spalte = 0; spalte < side(); spalte++) {
				int sum = 0;
				for (int i = 0; i < side(); i++) {
					sum += get(zeile, i) * otherMatrix.get(i, spalte);
				}

				if (sum != defaultValue) {
					resultElements.put(new Element(zeile, spalte), sum);
				}
			}
		}

		elements = resultElements;
	}

	@Override
	public int get(int row, int column) {
		validateRowAndColumn(row, column);

		final Integer returnValue = elements.get(new Element(row, column));
		return returnValue != null ? returnValue : defaultValue;
	}

	@Override
	public void set(int row, int column, int value) {
		validateRowAndColumn(row, column);

		final Element element = new Element(row, column);
		if (value != defaultValue) {
			elements.put(element, value);
		} else {
			// setting default value? --> trying to delete node
			elements.remove(element);
		}
	}

	@Override
	public void setAll(int value) {
		defaultValue = value;
		elements.clear();
	}

	@Override
	public int side() {
		return side;
	}

	@Override
	public Object clone() {
		return new SquareMatrixHashMap(elements, defaultValue, side());
	}

	private class Element implements Comparable<Element> {
		final private int row;
		final private int column;

		public Element(int row, int column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + column;
			result = prime * result + row;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;

			Element other = (Element) obj;
			return column == other.column && row == other.row;
		}

		@Override
		public int compareTo(Element o) {
			if (row < o.row) {
				return -1;
			} else if (row > o.row) {
				return 1;
			} else if (column < o.column) {
				return -1;
			} else if (column > o.column) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public void expand(int nodesToExpand) {
		if (nodesToExpand < 0)
			throw new IllegalArgumentException("Can't expand a negative value!");

		side += nodesToExpand;
	}
}
