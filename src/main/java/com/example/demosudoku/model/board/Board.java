package com.example.demosudoku.model.board;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements the IBoard interface to provide a complete Sudoku board implementation.
 * This class is responsible for generating valid Sudoku puzzles, managing the game board state,
 * storing the solution, and validating moves according to Sudoku rules.
 * The board uses a 6x6 grid with 2x3 blocks for this specific Sudoku variant.
 *
 * The Board class handles:
 * - Automatic puzzle generation during construction using backtracking algorithm
 * - Storage of both the puzzle clues and the complete solution
 * - Validation of player moves according to Sudoku rules
 * - Management of cell values and state throughout gameplay
 */
public class Board implements IBoard {

    /**
     * The size of the Sudoku board (6x6 for this implementation).
     * This constant defines both the number of rows and columns in the puzzle.
     */
    private static final int SIZE = 6;

    /**
     * The number of rows in each block (2 rows per block).
     * Combined with BLOCK_COLS, this defines the 2x3 block structure of the board.
     */
    private static final int BLOCK_ROWS = 2;

    /**
     * The number of columns in each block (3 columns per block).
     * Combined with BLOCK_ROWS, this defines the 2x3 block structure of the board.
     */
    private static final int BLOCK_COLS = 3;

    /**
     * A set containing all cells currently on the game board.
     * Each cell in this set represents a filled position during gameplay.
     * This set can be modified as the player enters values and progresses through the puzzle.
     * Empty cells are represented by the absence of a Cell object at that position.
     */
    private final Set<Cell> board;

    /**
     * A set containing all cells of the complete solution.
     * This set stores the full solved puzzle for reference and validation purposes.
     * It remains unchanged throughout gameplay and is used to verify player moves and provide hints.
     */
    private final Set<Cell> solution;

    /**
     * A Random instance used for shuffling numbers and positions during board generation.
     * This ensures that different puzzles are generated on each run of the application.
     * The randomization is applied during the backtracking fill process and clue selection phase.
     */
    private final Random random = new Random();

    /**
     * Constructs a Board instance and automatically generates a valid Sudoku puzzle.
     * The constructor initializes empty sets for both the game board and solution,
     * then attempts to generate a valid board up to 1000 times.
     *
     * The generation process:
     * 1. Clears the board and solution sets
     * 2. Generates a complete valid Sudoku puzzle using backtracking
     * 3. Saves the complete solution
     * 4. Selects exactly 2 clues per block for the puzzle
     * 5. Populates the game board with these clues
     *
     * If a valid board cannot be generated after all attempts, an error message is printed
     * to the error stream. Upon successful generation, a success message is printed to the
     * standard output indicating the number of attempts required.
     */
    public Board() {
        this.board = new HashSet<>();
        this.solution = new HashSet<>();
        boolean validBoard = false;
        int attempts = 0;

        while (!validBoard && attempts < 1000) {
            validBoard = generateBoard();
            attempts++;
            if (validBoard) {
                System.out.println("Tablero válido generado en intento " + attempts);
            }
        }

        if (!validBoard) {
            System.err.println("No se pudo generar un tablero válido después de " + attempts + " intentos");
        }
    }

    /**
     * Generates a complete and valid Sudoku board along with its solution.
     *
     * This method performs the following steps:
     * 1. Clears any existing board and solution data
     * 2. Creates a temporary 2D array to hold the complete filled board
     * 3. Uses the fillBoard method with backtracking to generate a valid solution
     * 4. Stores the complete solution in the solution set
     * 5. Creates a game board by selecting exactly 2 clues per block
     * 6. Uses selectTwoInBlockSafe to ensure no conflicts between clues
     * 7. Populates the board set with the selected clues
     *
     * The clue placement strategy ensures that:
     * - Each block has exactly 2 initial clues
     * - No duplicate numbers exist in any row, column, or block
     * - The puzzle remains solvable and unique
     *
     * @return {@code true} if board generation was successful and all blocks have 2 valid clues,
     *         {@code false} if generation failed due to inability to find valid placements or
     *         if the backtracking algorithm could not complete a full solution
     */
    private boolean generateBoard() {
        board.clear();
        solution.clear();

        int[][] tempBoard = new int[SIZE][SIZE];

        if (!fillBoard(tempBoard, 0, 0)) {
            return false;
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                solution.add(new Cell(row, col, tempBoard[row][col]));
            }
        }

        int[][] gameBoard = new int[SIZE][SIZE];

        for (int blockRow = 0; blockRow < SIZE / BLOCK_ROWS; blockRow++) {
            for (int blockCol = 0; blockCol < SIZE / BLOCK_COLS; blockCol++) {
                if (!selectTwoInBlockSafe(tempBoard, gameBoard, blockRow, blockCol)) {
                    return false; // Si no se pueden seleccionar 2 válidos, reintentar
                }
            }
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int value = gameBoard[row][col];
                if (value != 0) {
                    board.add(new Cell(row, col, value));
                }
            }
        }

        return true;
    }

    /**
     * Safely selects exactly two clue numbers from a specific block without creating conflicts.
     *
     * This method implements the core logic for clue selection during puzzle generation:
     * 1. Calculates the starting row and column indices for the specified block
     * 2. Creates a list of all possible positions within that block (6 positions for 2x3 blocks)
     * 3. Randomly shuffles the positions to ensure variety in clue placement
     * 4. Iterates through positions and validates each placement using isValidPlacement
     * 5. Adds valid placements to the target board and tracks selected positions
     * 6. Returns true once exactly 2 valid positions are found
     * 7. If fewer than 2 valid positions are found, rolls back all changes to the target board
     *
     * This ensures that:
     * - No duplicate numbers exist within the block
     * - No conflicts with previously placed clues in the same row, column, or block
     * - Each block receives exactly the required number of clues
     * - The puzzle remains valid and solvable
     *
     * @param source the complete filled solution board containing all valid numbers
     * @param target the board being constructed with clues for the puzzle; will be modified by this method
     * @param blockRow the block row index indicating which horizontal block band (0-based)
     * @param blockCol the block column index indicating which vertical block band (0-based)
     * @return {@code true} if exactly two valid clue positions were successfully selected and placed,
     *         {@code false} if fewer than two valid positions could be found in the block
     */
    private boolean selectTwoInBlockSafe(int[][] source, int[][] target, int blockRow, int blockCol) {
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;

        List<int[]> positions = new ArrayList<>();
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                positions.add(new int[]{i, j});
            }
        }

        Collections.shuffle(positions, random);

        List<int[]> selectedPositions = new ArrayList<>();

        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            int value = source[row][col];

            if (isValidPlacement(target, row, col, value)) {
                target[row][col] = value;
                selectedPositions.add(pos);

                if (selectedPositions.size() == 2) {
                    return true;
                }
            }
        }

        for (int[] pos : selectedPositions) {
            target[pos[0]][pos[1]] = 0;
        }

        return false;
    }

    /**
     * Validates whether placing a number at a specific position in the game board is legal.
     * This method checks all three Sudoku constraints:
     * - Row constraint: The number must not already exist in the same row
     * - Column constraint: The number must not already exist in the same column
     * - Block constraint: The number must not already exist in the same 2x3 block
     *
     * The method is used during the clue selection phase to ensure that the initial puzzle
     * configuration is valid and that subsequent clues do not conflict with previously placed clues.
     *
     * Process:
     * 1. Checks entire row for duplicate numbers (excluding current position)
     * 2. Checks entire column for duplicate numbers (excluding current position)
     * 3. Identifies the block containing the position
     * 4. Checks all cells in that block for duplicate numbers (excluding current position)
     * 5. Returns true only if the number is valid in all three contexts
     *
     * @param gameBoard the board being validated during the generation process
     * @param row the row index where the number would be placed (0-based)
     * @param col the column index where the number would be placed (0-based)
     * @param num the number to validate (must be between 1 and SIZE)
     * @return {@code true} if placement is valid according to Sudoku rules, {@code false} if
     *         the number already exists in the same row, column, or block
     */
    private boolean isValidPlacement(int[][] gameBoard, int row, int col, int num) {
        for (int j = 0; j < SIZE; j++) {
            if (j != col && gameBoard[row][j] == num) {
                return false;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (i != row && gameBoard[i][col] == num) {
                return false;
            }
        }

        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                if ((i != row || j != col) && gameBoard[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Recursively fills the board using the backtracking algorithm to generate a valid Sudoku puzzle.
     *
     * This method implements the core backtracking logic:
     * 1. Base cases: Returns true if all rows have been processed (row == SIZE)
     * 2. If a row is complete, advances to the next row
     * 3. For each cell, creates a shuffled list of candidate numbers (1 through SIZE)
     * 4. Attempts to place each candidate at the current position
     * 5. If a candidate is valid (checked by isValidTemp), places it and recursively processes the next cell
     * 6. If recursion succeeds, returns true (puzzle is complete)
     * 7. If recursion fails, removes the candidate (backtracks) and tries the next one
     * 8. If no candidates work, returns false to trigger backtracking at a higher level
     *
     * The randomization of candidates ensures that different valid puzzles are generated
     * on successive calls to this method.
     *
     * Time complexity: Exponential in the worst case, but with Sudoku constraints,
     * typically completes in reasonable time.
     *
     * @param tempBoard the 2D array representing the board being filled during generation
     * @param row the current row index being processed (0-based)
     * @param col the current column index being processed (0-based)
     * @return {@code true} if the board was successfully filled from this position onward,
     *         {@code false} if backtracking is needed or no valid solution exists from this state
     */
    private boolean fillBoard(int[][] tempBoard, int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return fillBoard(tempBoard, row + 1, 0);

        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) nums.add(i);
        Collections.shuffle(nums, random);

        for (int num : nums) {
            if (isValidTemp(tempBoard, row, col, num)) {
                tempBoard[row][col] = num;
                if (fillBoard(tempBoard, row, col + 1)) return true;
                tempBoard[row][col] = 0;
            }
        }
        return false;
    }

    /**
     * Validates whether placing a number at a specific position is legal during the backtracking fill process.
     * This method checks the temporary board for Sudoku rule violations during puzzle generation.
     *
     * The method enforces three Sudoku constraints:
     * - Row constraint: The number must not already exist in the same row
     * - Column constraint: The number must not already exist in the same column
     * - Block constraint: The number must not already exist in the same 2x3 block
     *
     * This method is similar to isValidPlacement but operates on the temporary board during
     * the initial puzzle generation phase rather than on the final puzzle configuration.
     *
     * Process:
     * 1. Iterates through all columns in the row to check for duplicates (excluding current column)
     * 2. Iterates through all rows in the column to check for duplicates (excluding current row)
     * 3. Calculates the block boundaries containing the current position
     * 4. Checks all cells in that block for duplicates (excluding current position)
     * 5. Returns true only if the number is valid in all three contexts
     *
     * @param tempBoard the temporary board being filled during generation
     * @param row the row index where the number would be placed (0-based)
     * @param col the column index where the number would be placed (0-based)
     * @param num the number to validate (must be between 1 and SIZE)
     * @return {@code true} if placement is valid according to Sudoku rules, {@code false} if
     *         the number already exists in the same row, column, or block of the temporary board
     */
    private boolean isValidTemp(int[][] tempBoard, int row, int col, int num) {
        // Check row
        for (int j = 0; j < SIZE; j++) {
            if (j != col && tempBoard[row][j] == num) return false;
        }

        // Check column
        for (int i = 0; i < SIZE; i++) {
            if (i != row && tempBoard[i][col] == num) return false;
        }

        // Check block
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                if ((i != row || j != col) && tempBoard[i][j] == num) return false;
            }
        }

        return true;
    }

    /**
     * Implements the fillBlocks method from the IBoard interface.
     * In this implementation, the method returns true as the board filling is handled
     * entirely during construction through the generateBoard method.
     *
     * The blockIndex parameter is not used in this implementation because the complete
     * board generation is performed in the constructor, making separate block filling unnecessary.
     * This method exists to satisfy the IBoard interface contract.
     *
     * @param blockIndex The starting index for the filling process (not used in this implementation).
     * @return {@code true} always, as block filling is performed during board initialization.
     */
    @Override
    public boolean fillBlocks(int blockIndex) {
        return true;
    }

    /**
     * Implements the isValid method from the IBoard interface.
     * Validates whether placing a candidate number at the specified position conforms to Sudoku rules
     * by checking the current game board state.
     *
     * This method is called during gameplay to verify that a player's move is valid according to
     * the Sudoku rules. Unlike isValidTemp and isValidPlacement which operate on temporary or
     * construction boards, this method operates on the actual game board set.
     *
     * The method enforces three Sudoku constraints:
     * - Row constraint: The candidate must not already exist in the same row on the game board
     * - Column constraint: The candidate must not already exist in the same column on the game board
     * - Block constraint: The candidate must not already exist in the same 2x3 block on the game board
     *
     * Process:
     * 1. Searches the board set for cells in the same row with the candidate value
     * 2. Searches the board set for cells in the same column with the candidate value
     * 3. Calculates the block boundaries containing the position
     * 4. Searches the board set for cells in that block with the candidate value
     * 5. Returns false if any conflict is found, true if the move is completely valid
     *
     * @param row the row index of the cell (0-based)
     * @param col the column index of the cell (0-based)
     * @param candidate the number to validate for placement (must be between 1 and SIZE)
     * @return {@code true} if the placement is valid according to Sudoku rules and no conflicts
     *         exist in the current game board, {@code false} if the candidate already exists
     *         in the same row, column, or block on the game board
     */
    @Override
    public boolean isValid(int row, int col, int candidate) {
        // Check row
        for (Cell c : board) {
            if (c.row == row && c.col != col && c.value == candidate) {
                return false;
            }
        }

        // Check column
        for (Cell c : board) {
            if (c.col == col && c.row != row && c.value == candidate) {
                return false;
            }
        }

        // Check block
        int blockRow = row / BLOCK_ROWS;
        int blockCol = col / BLOCK_COLS;
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;

        for (Cell c : board) {
            if (c.row >= startRow && c.row < startRow + BLOCK_ROWS &&
                    c.col >= startCol && c.col < startCol + BLOCK_COLS &&
                    (c.row != row || c.col != col) && c.value == candidate) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retrieves the value at a specific cell position on the game board.
     * This method searches the board set for a cell at the given position and returns its value.
     * If no cell exists at the given position (i.e., the cell is empty), returns 0.
     *
     * The method is used during gameplay to display the current state of the puzzle
     * and to check what values the player has entered.
     *
     * @param row the row index of the cell (0-based)
     * @param col the column index of the cell (0-based)
     * @return the value at the specified position, or 0 if the cell is empty or does not exist
     */
    public int getValueAt(int row, int col) {
        for (Cell c : board) {
            if (c.row == row && c.col == col) {
                return c.value;
            }
        }
        return 0;
    }

    /**
     * Retrieves the solution value at a specific cell position.
     * This method queries the solution set to obtain the correct answer for a given position.
     * The solution set is immutable and represents the completely solved puzzle.
     * If no cell exists at the given position in the solution set, returns 0 (should never happen).
     *
     * This method is used to:
     * - Verify whether the player's move is correct
     * - Provide hints to the player
     * - Determine if the puzzle is completely solved
     * - Validate the puzzle upon completion
     *
     * @param row the row index of the cell (0-based)
     * @param col the column index of the cell (0-based)
     * @return the solution value at the specified position, or 0 if not found
     */
    public int getSolutionValueAt(int row, int col) {
        for (Cell c : solution) {
            if (c.row == row && c.col == col) {
                return c.value;
            }
        }
        return 0;
    }

    /**
     * Sets a value at a specific cell position on the game board.
     * This method first removes any existing cell at that position, then adds a new cell
     * with the provided value (if the value is not 0).
     *
     * The operation effectively:
     * 1. Removes the old cell from the board set (if it exists)
     * 2. If the new value is 0, leaves the position empty
     * 3. If the new value is non-zero, adds a new Cell object to the board set
     *
     * This method is used during gameplay to:
     * - Update the board when a player enters a value
     * - Clear a cell when a player removes a value
     * - Apply hints provided by the game logic
     * - Manage the puzzle state throughout gameplay
     *
     * @param row the row index of the cell (0-based)
     * @param col the column index of the cell (0-based)
     * @param value the value to set at the specified position (0 for empty)
     */
    public void setValueAt(int row, int col, int value) {
        board.removeIf(c -> c.row == row && c.col == col);
        if (value != 0) {
            board.add(new Cell(row, col, value));
        }
    }

    /**
     * Returns the complete game board as a set of Cell objects.
     * The set contains all currently filled cells on the puzzle during gameplay.
     * Empty cells are represented by the absence of a Cell object at that position.
     *
     * The returned set directly reflects the current state of the puzzle and can be modified
     * by external code. Changes to the returned set will affect the game board state.
     *
     * This method is used to:
     * - Display the current state of the puzzle in the UI
     * - Iterate through all filled cells for validation
     * - Export or save the puzzle state
     * - Perform game logic operations that require access to all filled cells
     *
     * @return a Set containing all Cell objects currently on the game board
     */
    public Set<Cell> getBoard() {
        return board;
    }

    /**
     * Returns the complete solution as a set of Cell objects.
     * The set contains all correctly filled cells representing the completely solved puzzle.
     * This set remains unchanged throughout gameplay after the initial generation.
     *
     * The returned set contains exactly SIZE * SIZE cells, with one cell for each position
     * on the board, each containing the correct solution value.
     *
     * This method is used to:
     * - Verify whether the player has completed the puzzle correctly
     * - Check if a player's move is correct
     * - Provide hints by revealing solution values
     * - Validate puzzle completion
     * - Export the solution for reference or debugging
     *
     * @return a Set containing all Cell objects in the complete solution
     */
    public Set<Cell> getSolution() {
        return solution;
    }

    /**
     * Represents a single cell in the Sudoku board.
     * A Cell contains the row and column coordinates and the numeric value it holds.
     *
     * Key characteristics:
     * - Row and column are 0-based indices
     * - Value is a number between 0 (empty) and SIZE (6 for this implementation)
     * - Equality is determined solely by row and column position, not by value
     * - Hash code is also based only on row and column
     * - This design allows cells at the same position to be compared regardless of their values
     *
     * This Cell class is used throughout the Board class to represent positions and values
     * in both the game board and the solution set.
     */
    public static class Cell {
        /**
         * The row index of the cell (0-based).
         * Valid values are from 0 to SIZE-1 (0 to 5 for this implementation).
         */
        public int row;

        /**
         * The column index of the cell (0-based).
         * Valid values are from 0 to SIZE-1 (0 to 5 for this implementation).
         */
        public int col;

        /**
         * The numeric value stored in this cell.
         * Valid values are from 0 (empty cell) to SIZE (6 for this implementation).
         * A value of 0 indicates an empty cell with no clue or player entry.
         */
        public int value;

        /**
         * Constructs a Cell with the specified row, column, and value.
         * This constructor initializes a new cell at the given position with the provided value.
         *
         * @param row the row index of the cell (0-based, should be 0 to SIZE-1)
         * @param col the column index of the cell (0-based, should be 0 to SIZE-1)
         * @param value the numeric value to store in the cell (0 for empty, 1-SIZE for filled)
         */
        public Cell(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        /**
         * Compares this Cell with another object for equality.
         * Two cells are considered equal if they have the same row and column indices,
         * regardless of their values. This design allows cells to be looked up and compared
         * by position alone, making it easy to check if a position exists on the board.
         *
         * The comparison process:
         * 1. Returns true if comparing with itself (same object reference)
         * 2. Returns false if the object is not an instance of Cell
         * 3. Casts the object to Cell and compares row and column values
         * 4. Returns true only if both row and column match
         *
         * @param o the object to compare with this Cell
         * @return {@code true} if the object is a Cell with the same row and column indices,
         *         {@code false} otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cell)) return false;
            Cell cell = (Cell) o;
            return row == cell.row && col == cell.col;
        }

        /**
         * Returns the hash code for this Cell based on its row and column indices.
         * Cells with the same row and column will have the same hash code, even if their
         * values differ. This is consistent with the equals method and allows Cell objects
         * to be used effectively in hash-based collections like HashSet and HashMap.
         *
         * The hash code is computed using Objects.hash(row, col), which combines the
         * individual hash codes of the row and column values into a single hash code.
         *
         * @return the hash code based on row and column coordinates
         */
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        /**
         * Returns a string representation of this Cell in the format "(row,col)=value".
         * This format provides a human-readable description of the cell's position and value.
         *
         * For example:
         * - A cell at row 2, column 3 with value 4 would return "(2,3)=4"
         * - An empty cell at row 0, column 0 with value 0 would return "(0,0)=0"
         *
         * This representation is useful for debugging, logging, and displaying cell information.
         *
         * @return a string representation of the cell in the format "(row,col)=value"
         */
        @Override
        public String toString() {
            return "(" + row + "," + col + ")=" + value;
        }
    }
}