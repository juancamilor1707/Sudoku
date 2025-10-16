package com.example.demosudoku.model.game;

import com.example.demosudoku.model.board.Board;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Represents the concrete implementation of the Sudoku game logic.
 * This class extends GameAbstract and provides a complete, playable Sudoku game experience
 * with full implementation of game rules, player interaction handling, hint provision,
 * and win condition detection.
 *
 * Game features implemented:
 * - Initialization of a 6x6 Sudoku board with TextFields for user input
 * - Real-time input validation and move legality checking
 * - Visual feedback for valid moves, invalid entries, and repeated numbers
 * - Hint system that provides correct values for empty cells
 * - Win condition detection and victory screen display
 * - Integration with user information and game state management
 *
 * The game validates all player moves according to Sudoku rules (no duplicates in rows,
 * columns, or blocks) and provides immediate visual feedback through color highlights
 * that automatically clear after a brief delay.
 */
public class Game extends GameAbstract {

    /**
     * Constructs a Game instance with the specified GridPane container.
     * This constructor calls the parent class constructor (GameAbstract) which initializes
     * the board, initializes the ArrayList for TextFields, and sets up other game components.
     * The GridPane provided will serve as the container for all the game board cells.
     *
     * @param boardGridpane the GridPane that will contain the game board's UI cells
     */
    public Game(GridPane boardGridpane) {
        super(boardGridpane);
    }

    /**
     * Initializes and starts the game by populating the GridPane with TextFields and clue values.
     * This method is called to set up the initial game state and make the game playable.
     *
     * The method performs the following operations:
     * 1. Iterates through all 36 cells of the 6x6 Sudoku board
     * 2. Retrieves the initial value from the board for each cell
     * 3. Creates a TextField for each cell and configures it:
     *    - Sets alignment to center for better visual presentation
     * 4. Applies default styling to all cells (font size 30px, black text)
     * 5. For cells with clue values (number != 0):
     *    - Displays the clue value
     *    - Makes the field non-editable to prevent changing initial clues
     * 6. For empty cells (number == 0):
     *    - Leaves the field empty and ready for player input
     * 7. Sets up event handling for each field using handleNumberField
     * 8. Adds the TextField to the GridPane at the correct position
     * 9. Prints the board state to the console for debugging
     *
     * The resulting UI presents a playable Sudoku puzzle where players can interact
     * with empty cells to enter their answers.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int number = board.getValueAt(i, j);
                System.out.print(number + " ");

                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                textField.setText(String.valueOf(number));
                textField.setBackground(Background.EMPTY);
                textField.setStyle("-fx-font-size: 30px; -fx-text-fill: black;");

                if (number != 0) {
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                }

                handleNumberField(textField, i, j);
                boardGridpane.add(textField, j, i);
            }
            System.out.println();
        }
    }

    /**
     * Sets up event handling for a TextField to process player input and validate moves.
     * This method is called during startGame for each cell and configures the TextField
     * to respond to keyboard input with real-time validation and visual feedback.
     *
     * The event handling logic:
     * 1. Listens for key release events on the TextField
     * 2. Retrieves and trims the input text
     * 3. If input is provided:
     *    a. Attempts to parse the input as an integer
     *    b. Validates that the number is between 1 and 6 (valid Sudoku values)
     *    c. If invalid range, clears the field and highlights it red as an error
     *    d. If valid range, checks the move's legality using board.isValid()
     *    e. If move is invalid (duplicate in row/column/block):
     *       - Clears the field
     *       - Highlights in yellow to indicate a repeated number
     *    f. If move is valid:
     *       - Updates the board with the new value
     *       - Checks if the puzzle is now complete
     *       - If complete, displays the win screen
     *    g. If input is not a valid integer, highlights red and clears the field
     * 4. If input is empty (player cleared the field):
     *    - Removes the value from the board (sets to 0)
     *
     * Visual feedback mechanisms:
     * - Red highlight for invalid number ranges or non-integer input
     * - Yellow highlight for repeated numbers (duplicate in row/column/block)
     * - Automatic reset to default style after 1 second
     *
     * @param txt the TextField associated with a specific board cell
     * @param row the row index of the cell (0-based)
     * @param col the column index of the cell (0-based)
     */
    private void handleNumberField(TextField txt, int row, int col) {
        txt.setOnKeyReleased(event -> {
            String input = txt.getText().trim();
            if(input.length() > 0){
                try {
                    int num = Integer.parseInt(input);
                    if(num < 1 || num > 6) {
                        txt.clear();
                        highlightError(txt);
                        return;
                    }

                    boolean result = board.isValid(row, col, num);
                    if (!result) {
                        txt.clear();
                        highlightRepeatNumber(txt);
                    } else {
                        board.setValueAt(row, col, num);

                        if (isBoardComplete()) {
                            showWinScreen();
                        }
                    }

                } catch (NumberFormatException e) {
                    highlightError(txt);
                    txt.clear();
                }
            } else {
                board.setValueAt(row, col, 0);
            }
        });
    }

    /**
     * Provides a hint to the player by filling in a correct value in the first empty cell.
     * This method is called when the player clicks the hint button and implements
     * the hint system for assisting players during gameplay.
     *
     * The hint provision process:
     * 1. Prints a debug message to indicate hint search is starting
     * 2. Iterates through all cells on the 6x6 board in row-major order
     * 3. For each cell:
     *    a. Prints the cell's current value for debugging
     *    b. Checks if the cell is empty (value == 0)
     *    c. If empty, searches the GridPane for the corresponding TextField
     *    d. Retrieves the node at the calculated GridPane position
     *    e. Handles null GridPane indices (defaults to 0)
     * 4. When an empty TextField is found:
     *    a. Retrieves the correct solution value from the solution set
     *    b. Validates that this value is still valid in the current board state
     *    c. If valid:
     *       - Updates the board with the correct value
     *       - Sets the TextField to display this value
     *       - Makes the TextField non-editable to lock the hint
     *       - Applies blue text styling to distinguish hint values from player entries
     *       - Prints a debug message indicating the hint was provided
     *       - Sets hintGiven to true to exit the loop
     *    d. If not valid (solution conflicts with current board):
     *       - Prints a debug message explaining why the hint couldn't be applied
     * 5. After providing a hint, checks if the board is now complete:
     *    a. If complete, prints a completion message and displays the win screen
     *    b. If not complete, continues normally
     * 6. If no empty cells remain and no hint was given:
     *    a. Prints a message indicating no more empty cells for hints
     *
     * Visual indication:
     * - Hint values are displayed in blue text to distinguish them from player entries
     * - TextField is made non-editable to prevent modification of hints
     * - Minimum height is set to 100 for better visibility
     */
    public void provideHint() {
        System.out.println("=== Buscando celda vacía ===");
        boolean hintGiven = false;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.println("Celda [" + i + "][" + j + "] = " + board.getValueAt(i, j));
                if (board.getValueAt(i, j) == 0) {

                    TextField targetField = null;
                    for (javafx.scene.Node node : boardGridpane.getChildren()) {
                        Integer rowIndex = GridPane.getRowIndex(node);
                        Integer colIndex = GridPane.getColumnIndex(node);

                        int actualRow = (rowIndex == null) ? 0 : rowIndex;
                        int actualCol = (colIndex == null) ? 0 : colIndex;

                        if (actualRow == i && actualCol == j && node instanceof TextField) {
                            targetField = (TextField) node;
                            break;
                        }
                    }

                    if (targetField != null && targetField.getText().trim().isEmpty()) {
                        int correctValue = board.getSolutionValueAt(i, j);

                        if (board.isValid(i, j, correctValue)) {
                            board.setValueAt(i, j, correctValue);
                            targetField.setText(String.valueOf(correctValue));
                            targetField.setEditable(false);
                            targetField.setStyle("-fx-font-size: 30px; -fx-text-fill: blue;-fx-min-height: 100;");
                            System.out.println("Pista en [" + i + "][" + j + "] = " + correctValue);
                            hintGiven = true;
                            break;
                        } else {
                            System.out.println("La solución original ya no es válida en [" + i + "][" + j + "]");
                        }
                    }
                }
            }
            if (hintGiven) break;
        }

        if (isBoardComplete()) {
            System.out.println("¡Tablero completo! Mostrando pantalla de victoria...");
            showWinScreen();
        } else if (!hintGiven) {
            System.out.println("No hay más celdas vacías para dar pista");
        }
    }

    /**
     * Sets the user for the current game session.
     * This method stores a reference to the User object that represents the current player,
     * allowing the game to access player information such as the nickname for display
     * in the victory screen and other player-related features.
     *
     * @param user the User object representing the current player
     */
    public void setUser(com.example.demosudoku.model.user.User user) {
        this.user = user;
    }

    /**
     * Checks if the Sudoku board is completely filled and solved.
     * This method iterates through all 36 cells of the 6x6 board and verifies that
     * no empty cells (cells with value 0) remain. If any empty cell is found, the
     * method immediately returns false. Only when all cells contain non-zero values
     * does the method return true, indicating the puzzle is complete.
     *
     * This method is used to:
     * - Determine if the player has successfully completed the puzzle
     * - Trigger the win condition and display the victory screen
     * - Check if no more hints can be provided
     *
     * @return {@code true} if all cells on the board contain non-zero values (board is complete),
     *         {@code false} if at least one empty cell (value 0) is found
     */
    private boolean isBoardComplete() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board.getValueAt(i, j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Displays the victory screen when the player successfully completes the puzzle.
     * This method creates and shows a new Stage containing the win screen UI,
     * personalizes it with the player's nickname, and manages the transition from
     * the game screen to the victory screen.
     *
     * The win screen display process:
     * 1. Prints a victory message to the console
     * 2. Uses Platform.runLater to ensure UI operations occur on the JavaFX thread
     * 3. Retrieves the current game Stage from the boardGridpane
     * 4. Creates a new Stage for the victory screen
     * 5. Loads the sudoku-win-view.fxml file using FXMLLoader
     * 6. Retrieves the SudokuWinController from the loaded FXML
     * 7. If a user exists, sets the victory message with the player's nickname
     * 8. Creates a Scene with the loaded root node
     * 9. Configures the win Stage:
     *    - Sets the title to "Sudoku - Victory!"
     *    - Disables window resizing
     * 10. Hides the game Stage when the win Stage is closed
     * 11. Hides the game screen and displays the victory screen
     * 12. Handles any exceptions that occur and prints error information
     *
     * The method uses Platform.runLater to handle thread safety concerns and ensure
     * that all UI updates occur on the JavaFX Application Thread.
     *
     * Error handling:
     * - If any exception occurs (file not found, reflection errors, etc.), it is caught
     * - The error message and stack trace are printed to the error stream
     * - The game continues to run despite the error
     */
    private void showWinScreen() {
        System.out.println("¡GANASTE!");
        javafx.application.Platform.runLater(() -> {
            try {
                // Obtenemos la ventana del juego
                javafx.stage.Stage gameStage = (javafx.stage.Stage) boardGridpane.getScene().getWindow();

                // Creamos el WinStage
                javafx.stage.Stage winStage = new javafx.stage.Stage();
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                        getClass().getResource("/com/example/demosudoku/sudoku-win-view.fxml")
                );
                javafx.scene.Parent root = loader.load();

                // Pasamos el nickname al controlador
                com.example.demosudoku.controller.SudokuWinController winController = loader.getController();
                if (user != null) {
                    winController.setVictoryMessage(user.getNickname());
                }

                winStage.setScene(new javafx.scene.Scene(root));
                winStage.setTitle("Sudoku - Victory!");
                winStage.setResizable(false);

                // IMPORTANTE: Cuando se cierra WinStage, cerramos GameStage
                winStage.setOnHidden(e -> {
                    gameStage.close();
                });

                // Ocultamos el juego y mostramos la victoria
                gameStage.hide();
                winStage.show();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Applies an error highlight (red background) to a TextField and automatically resets it after 1 second.
     * This method is used to provide visual feedback when a player enters an invalid value,
     * such as a number outside the valid range (1-6) or a non-integer input.
     *
     * The highlighting process:
     * 1. Applies a red border and light red background to the TextField
     * 2. Sets the border width to 3px for prominent visibility
     * 3. Maintains the font size at 30px
     * 4. Creates a PauseTransition animation for a 1-second delay
     * 5. After 1 second, calls resetStyle to restore the default appearance
     *
     * The temporary highlight helps the player understand that their input was invalid
     * and encourages them to try a different value.
     *
     * @param txt the TextField to highlight with error styling
     */
    private void highlightError(TextField txt) {
        txt.setStyle("-fx-border-color: red; -fx-background-color: #F75270; -fx-border-width: 3px; -fx-font-size: 30px;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> resetStyle(txt));
        pause.play();
    }

    /**
     * Applies a warning highlight (yellow background) to a TextField and automatically resets it after 1 second.
     * This method is used to provide visual feedback when a player attempts to enter a number that
     * already exists in the same row, column, or block (a repeated number violation of Sudoku rules).
     *
     * The highlighting process:
     * 1. Applies a yellow border and light yellow background to the TextField
     * 2. Sets the border width to 3px for prominent visibility
     * 3. Maintains the font size at 30px
     * 4. Creates a PauseTransition animation for a 1-second delay
     * 5. After 1 second, calls resetStyle to restore the default appearance
     *
     * The yellow highlight is more subtle than the red error highlight, distinguishing
     * this type of error (repeated number) from invalid input (out of range or non-integer).
     *
     * @param txt the TextField to highlight with repeated number warning styling
     */
    private void highlightRepeatNumber(TextField txt) {
        txt.setStyle("-fx-border-color: yellow; -fx-background-color: #FBF3D1; -fx-border-width: 3px; -fx-font-size: 30px;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> resetStyle(txt));
        pause.play();
    }

    /**
     * Resets a TextField to its default styling after temporary error or warning highlights have been displayed.
     * This method removes the colored borders and backgrounds and restores the normal appearance
     * of the TextField so it blends in with the rest of the board.
     *
     * The reset process:
     * 1. Removes any colored border by setting it to transparent
     * 2. Restores the default font size to 30px
     * 3. Sets the text color back to black
     *
     * This method is called automatically by the PauseTransition timers in highlightError
     * and highlightRepeatNumber after 1 second of highlighting has elapsed.
     *
     * @param txt the TextField whose styling should be reset to default
     */
    private void resetStyle(TextField txt) {
        txt.setStyle("-fx-border-color: transparent; -fx-font-size: 30px; -fx-text-fill: black;");
    }
}