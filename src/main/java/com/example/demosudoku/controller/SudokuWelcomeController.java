package com.example.demosudoku.controller;

import com.example.demosudoku.model.user.User;
import com.example.demosudoku.utils.AlertBox;
import com.example.demosudoku.view.SudokuGameStage;
import com.example.demosudoku.view.SudokuHelpStage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Controller for the welcome screen (sudoku-welcome-view.fxml).
 */
public class SudokuWelcomeController {

    @FXML
    private TextField nicknameTxt;

    @FXML
    void handlePlay(MouseEvent event) throws IOException {
        String nickname = nicknameTxt.getText().trim();

        if (!nickname.equals("")) {
            SudokuGameStage.getInstance().getController().setUser(new User(nickname));
            SudokuWelcomeStage.deleteInstance();
        } else {
            new AlertBox().showAlert("Error", "Ingresa un nickname", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleHelp(MouseEvent event) throws IOException {
        SudokuHelpStage.getInstance();
        SudokuWelcomeStage.deleteInstance();
    }
}