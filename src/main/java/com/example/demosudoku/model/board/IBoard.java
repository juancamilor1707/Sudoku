package com.example.demosudoku.model.board;

/**
 * Defines the contract for a Sudoku board. Implementations must provide
 * methods for filling blocks and validating number placements.
 * This interface establishes the core operations required for managing a Sudoku puzzle board.
 */
public interface IBoard {
    /**
     * Fills the board's blocks according to the implementing class's logic.
     * This method is responsible for populating the Sudoku board with initial values
     * using backtracking or other valid algorithms to ensure the puzzle is solvable.
     *
     * @param blockIndex The starting index for the filling process. This parameter indicates
     *                   which block or position to start filling from.
     * @return {@code true} if the filling was successful and the board is valid, {@code false} otherwise.
     */
    boolean fillBlocks(int blockIndex);

    /**
     * Checks if placing a candidate number at a given position is valid.
     * This method validates whether a number can be legally placed at the specified row and column
     * according to Sudoku rules (no duplicates in row, column, or block).
     *
     * @param row       The row index of the cell where the number would be placed (0-based).
     * @param col       The column index of the cell where the number would be placed (0-based).
     * @param candidate The number to validate for placement at the specified position.
     * @return {@code true} if the move is valid according to Sudoku rules, {@code false} otherwise.
     */
    boolean isValid(int row, int col, int candidate);
}