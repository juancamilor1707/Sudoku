package com.example.demosudoku.controller;

import com.example.demosudoku.view.SudokuHelpStage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class SudokuHelpController {

    @FXML
    void handleHelpMenu(MouseEvent event) throws IOException {
        SudokuHelpStage.deleteInstance();
        SudokuWelcomeStage.getInstance();
    }
}
