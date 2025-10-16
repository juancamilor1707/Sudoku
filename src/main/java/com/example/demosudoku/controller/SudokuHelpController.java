package com.example.demosudoku.controller;

import com.example.demosudoku.view.SudokuHelpStage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;

public class SudokuHelpController extends Stage {

    @FXML
    void handleMenu(MouseEvent event) throws IOException {
        SudokuWelcomeStage.getInstance();
        SudokuHelpStage.deleteInstance();
    }

}

