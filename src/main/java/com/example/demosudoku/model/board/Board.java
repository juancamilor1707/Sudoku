package com.example.demosudoku.model.board;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board implements IBoard {

    private static final int SIZE = 6;
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLS = 3;

    private final Set<Cell> board;
    private final Set<Cell> solution;
    private final Random random = new Random();

    public Board() {
        this.board = new HashSet<>();
        this.solution = new HashSet<>();
        boolean validBoard = false;
        int attempts = 0;

        // Intentar hasta generar un tablero válido
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

    private boolean generateBoard() {
        board.clear();
        solution.clear();

        // Generar tablero válido usando backtracking
        int[][] tempBoard = new int[SIZE][SIZE];

        if (!fillBoard(tempBoard, 0, 0)) {
            return false;
        }

        // Guardar la solución completa
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                solution.add(new Cell(row, col, tempBoard[row][col]));
            }
        }

        // Crear tablero de juego dejando exactamente 2 números por bloque
        int[][] gameBoard = new int[SIZE][SIZE];

        // Para cada bloque, seleccionar 2 posiciones aleatorias que no generen conflictos
        for (int blockRow = 0; blockRow < SIZE / BLOCK_ROWS; blockRow++) {
            for (int blockCol = 0; blockCol < SIZE / BLOCK_COLS; blockCol++) {
                if (!selectTwoInBlockSafe(tempBoard, gameBoard, blockRow, blockCol)) {
                    return false; // Si no se pueden seleccionar 2 válidos, reintentar
                }
            }
        }

        // Copiar a board
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

    private boolean selectTwoInBlockSafe(int[][] source, int[][] target, int blockRow, int blockCol) {
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;

        // Crear lista de todas las posiciones en este bloque
        List<int[]> positions = new ArrayList<>();
        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                positions.add(new int[]{i, j});
            }
        }

        // Mezclar las posiciones
        Collections.shuffle(positions, random);

        // Intentar encontrar 2 posiciones que no creen conflictos
        List<int[]> selectedPositions = new ArrayList<>();

        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            int value = source[row][col];

            // Verificar si agregar este valor crea conflicto
            if (isValidPlacement(target, row, col, value)) {
                target[row][col] = value;
                selectedPositions.add(pos);

                if (selectedPositions.size() == 2) {
                    return true; // Encontramos 2 válidos
                }
            }
        }

        // Si no pudimos encontrar 2 válidos, limpiar lo que pusimos y reintentar
        for (int[] pos : selectedPositions) {
            target[pos[0]][pos[1]] = 0;
        }

        return false;
    }

    private boolean isValidPlacement(int[][] gameBoard, int row, int col, int num) {
        // Verificar fila
        for (int j = 0; j < SIZE; j++) {
            if (j != col && gameBoard[row][j] == num) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < SIZE; i++) {
            if (i != row && gameBoard[i][col] == num) {
                return false;
            }
        }

        // Verificar bloque
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

    @Override
    public boolean fillBlocks(int blockIndex) {
        return true;
    }

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

    public int getValueAt(int row, int col) {
        for (Cell c : board) {
            if (c.row == row && c.col == col) {
                return c.value;
            }
        }
        return 0;
    }

    public int getSolutionValueAt(int row, int col) {
        for (Cell c : solution) {
            if (c.row == row && c.col == col) {
                return c.value;
            }
        }
        return 0;
    }

    public void setValueAt(int row, int col, int value) {
        board.removeIf(c -> c.row == row && c.col == col);
        if (value != 0) {
            board.add(new Cell(row, col, value));
        }
    }

    public Set<Cell> getBoard() {
        return board;
    }

    public Set<Cell> getSolution() {
        return solution;
    }

    public static class Cell {
        public int row;
        public int col;
        public int value;

        public Cell(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cell)) return false;
            Cell cell = (Cell) o;
            return row == cell.row && col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")=" + value;
        }
    }
}