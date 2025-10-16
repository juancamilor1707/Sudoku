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

    @FXML
    private GridPane boardGridPane;

    @FXML
    private Button hintButton;

    private Game game;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new Game(boardGridPane);
        game.startGame();

        if (hintButton != null) {
            hintButton.setOnAction(event -> game.provideHint());
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (game != null) {
            game.setUser(user);
        }
    }
}