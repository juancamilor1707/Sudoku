package com.example.demosudoku.model.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates a 6x6 Sudoku-like board divided into 2x3 blocks.
 * Each block initially gets one number randomly placed.
 * Also generates a complete valid solution board to be used for hints or checking answers.
 */
public class Board implements IBoard {
    private final int SIZE = 6;
    private final int BLOCK_ROWS = 2;
    private final int BLOCK_COLS = 3;

    private final int TOTAL_BLOCK_ROWS = SIZE / BLOCK_ROWS; // 6/2 = 3
    private final int TOTAL_BLOCK_COLS = SIZE / BLOCK_COLS; // 6/3 = 2
    private final int TOTAL_BLOCKS = TOTAL_BLOCK_ROWS * TOTAL_BLOCK_COLS; // 3 * 2 = 6

    private final List<List<Integer>> board;
    private List<List<Integer>> solution;

    private final Random random = new Random();


    public Board() {
        board = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < SIZE; j++) {
                board.get(i).add(0);
            }
        }

        fill(0, 0);

        // Guardar solución antes de remover
        solution = new ArrayList<>();
        for (List<Integer> row : board) {
            solution.add(new ArrayList<>(row));
        }

        Random rand = new Random();
        int removed = 0;
        while (removed < 28) {
            int row = rand.nextInt(6);
            int col = rand.nextInt(6);
            if (board.get(row).get(col) != 0) {
                board.get(row).set(col, 0);
                removed++;
            }
        }
    }

    private boolean fill(int row, int col) {
        if (row == 6) return true;
        if (col == 6) return fill(row + 1, 0);

        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 6; i++) nums.add(i);
        Collections.shuffle(nums);

        for (int n : nums) {
            if (isValid(row, col, n)) {
                board.get(row).set(col, n);
                if (fill(row, col + 1)) return true;
                board.get(row).set(col, 0);
            }
        }
        return false;
    }

    @Override
    public boolean fillBlocks(int blockIndex) {
        if (blockIndex == TOTAL_BLOCKS) {
            return true;
        }

        int blockRow = blockIndex / TOTAL_BLOCK_COLS;
        int blockCol = blockIndex % TOTAL_BLOCK_COLS;
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                for (Integer number : numbers) {
                    if (isValid(i, j, number)) {
                        board.get(i).set(j, number);
                        if (fillBlocks(blockIndex + 1)) {
                            return true;
                        }
                        board.get(i).set(j, 0);
                    }
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isValid(int row, int col, int candidate) {
        for (int j = 0; j < SIZE; j++) {
            if (board.get(row).get(j) == candidate) {
                return false;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == candidate) {
                return false;
            }
        }

        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                if (board.get(i).get(j) == candidate) {
                    return false;
                }
            }
        }

        return true;
    }
    public List<List<Integer>> getBoard() {
        return board;
    }

    public List<List<Integer>> getSolution() {
        return solution;
    }

    /**
     * Generates a full valid Sudoku 6x6 solution (no zeros).
     */
    private List<List<Integer>> generateFullSolution() {
        List<List<Integer>> full = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < SIZE; j++) {
                row.add(0);
            }
            full.add(row);
        }

        fillSolution(full, 0, 0);
        return full;
    }

    /**
     * Backtracking algorithm to fill the entire board with a valid Sudoku solution.
     */
    private boolean fillSolution(List<List<Integer>> full, int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return fillSolution(full, row + 1, 0);

        List<Integer> numbers = new ArrayList<>();
        for (int n = 1; n <= SIZE; n++) numbers.add(n);
        Collections.shuffle(numbers, random);

        for (int num : numbers) {
            if (isValidSolution(full, row, col, num)) {
                full.get(row).set(col, num);
                if (fillSolution(full, row, col + 1)) {
                    return true;
                }
                full.get(row).set(col, 0);
            }
        }
        return false;
    }

    /**
     * Checks validity for the full solution (rows, cols, and 2x3 blocks).
     */
    private boolean isValidSolution(List<List<Integer>> full, int row, int col, int num) {
        // Row and column
        for (int i = 0; i < SIZE; i++) {
            if (full.get(row).get(i) == num || full.get(i).get(col) == num) {
                return false;
            }
        }

        // Block check
        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                if (full.get(i).get(j) == num) {
                    return false;
                }
            }
        }

        return true;
    }
}
