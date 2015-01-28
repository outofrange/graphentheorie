package at.spengergasse.moe15300.model.matrix.implementation;

import at.spengergasse.moe15300.model.matrix.IntegerMatrix;
import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;

import java.util.ArrayList;
import java.util.List;

public class SquareMatrixArrayList extends IntegerSquareMatrix {
    private List<List<Integer>> matrix;

    public SquareMatrixArrayList() {
        super();
        this.matrix = generateMatrix(0, 0);
    }

    /**
     * Creates a new square matrix
     *
     * @param side The size of each side of the two dimensional matrix
     */
    public SquareMatrixArrayList(int side) {
        this.matrix = generateMatrix(side, 0);
    }

    public SquareMatrixArrayList(int side, int initialValue) {
        this.matrix = generateMatrix(side, initialValue);
    }

    public SquareMatrixArrayList(int[][] matrix) {
        if (!isValid(matrix)) {
            throw new IllegalArgumentException("This isn't a valid matrix!");
        }

        this.matrix = generateMatrix(matrix.length, 0);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                this.matrix.get(i).set(j, matrix[i][j]);
            }
        }
    }

    private SquareMatrixArrayList(List<List<Integer>> matrix) {
        setMatrix(matrix);
    }

    @Override
    public Object clone() {
        return new SquareMatrixArrayList(matrix);
    }

    @Override
    public void expand(int nodesToExpand) {
        for (int i = 0; i < nodesToExpand; i++) {
            ArrayList<Integer> newRow = new ArrayList<>();
            matrix.add(newRow);
            final int side = side();

            for (int row = 0; row < side - 1; row++) {
                matrix.get(row).add(0);
            }

            for (int column = 0; column < side; column++) {
                newRow.add(0);
            }
        }

    }

    @Override
    public void shrink(int nodesToShrink) {
        if (side() > 1) {
            for (int i = 0; i < nodesToShrink; i++) {
                int side = side();
                for (int row = 0; row < side - 1; row++) {
                    matrix.get(row).remove(side - 1);
                }
                matrix.remove(side - 1);
            }
        }
    }

    protected List<List<Integer>> generateMatrix(int side, int value) {
        // TODO refactor
        if (side != 0) {
            List<List<Integer>> matrix = new ArrayList<>(side);
            for (int i = 0; i < side; i++) {
                matrix.add(i, new ArrayList<Integer>(side));
                for (int j = 0; j < side; j++) {
                    matrix.get(i).add(value);
                }
            }

            return matrix;
        } else {
            return new ArrayList<>();
        }
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
        if (!isValid(matrix)) {
            throw new IllegalArgumentException("This isn't a valid matrix!");
        }

        if (width() != otherMatrix.height())
            throw new IllegalArgumentException(
                    "otherMatrix' height has to be as big as this matrix' width.");

        List<List<Integer>> resultMatrix = generateMatrix(side(), 0);

        for (int row = 0; row < matrix.size(); row++) {
            for (int column = 0; column < matrix.get(row).size(); column++) {
                int sum = 0;
                for (int i = 0; i < matrix.size(); i++) {
                    sum += get(row, i) * otherMatrix.get(i, column);
                }

                resultMatrix.get(row).set(column, sum);
            }
        }

        matrix = resultMatrix;
    }

    @Override
    public void set(int row, int column, int value) {
        matrix.get(row).set(column, value);
    }

    public void setMatrix(List<List<Integer>> matrix) {
        if (!isValid(matrix)) {
            throw new IllegalArgumentException("This isn't a valid matrix");
        }

        this.matrix = generateMatrix(matrix.size(), 0);
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(0).size(); j++) {
                this.matrix.get(i).set(j, matrix.get(i).get(j));
            }
        }
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

    @Override
    public void deleteRowAndColumn(int rowAndColumn) {
        matrix.remove(rowAndColumn);

        for (List<Integer> row : matrix) {
            row.remove(rowAndColumn);
        }
    }
}