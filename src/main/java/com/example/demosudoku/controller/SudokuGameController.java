package com.example.demosudoku.controller;

import com.example.demosudoku.model.game.Game;
import com.example.demosudoku.model.user.User;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the main Sudoku game view (sudoku-game-view.fxml).
 * This class is responsible for initializing and managing the game board's UI.
 */
public class SudokuGameController implements Initializable {

    /**
     * The GridPane that serves as the container for the Sudoku game board.
     * This pane is populated with cells that represent the 9x9 Sudoku grid.
     */
    @FXML
    private GridPane boardGridPane;

    /**
     * The Button that allows the player to request a hint during gameplay.
     * When clicked, it triggers the game to provide a hint to the player.
     */
    @FXML
    private Button hintButton;

    /**
     * The Game instance that manages the core game logic, state, and board interactions.
     * This object handles the Sudoku puzzle generation, validation, and hint provision.
     */
    private Game game;

    /**
     * The User instance that represents the current player.
     * This object stores information about the player, such as their nickname and statistics.
     */
    private User user;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * This method creates a new Game instance with the boardGridPane, starts the game,
     * and sets up the event handler for the hint button.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new Game(boardGridPane);
        game.startGame();

        if (hintButton != null) {
            hintButton.setOnAction(event -> game.provideHint());
        }
    }

    /**
     * Sets the current user for the game controller.
     * If the game instance has been initialized, this method also sets the user in the game instance.
     * This method should be called after the controller is initialized to ensure the game knows about the player.
     *
     * @param user the User object representing the current player
     */
    public void setUser(User user) {
        this.user = user;
        if (game != null) {
            game.setUser(user);
        }
    }
}