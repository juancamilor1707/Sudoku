package com.example.demosudoku.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import com.example.demosudoku.view.SudokuGameStage;

public class SudokuWinController {

    @FXML
    private Label nicknameLabel;

    public void setVictoryMessage(String nickname) {
        if (nicknameLabel != null) {
            nicknameLabel.setText("Congratulations, " + nickname + "! You Won!");
        }
    }

    @FXML
    private void handleMenuWin(MouseEvent event) throws IOException {
        Stage winStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        winStage.close();
        SudokuGameStage.deleteInstance();
        SudokuWelcomeStage.getInstance();
    }
}