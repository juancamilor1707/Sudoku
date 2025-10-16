package com.example.demosudoku.controller;

import com.example.demosudoku.view.SudokuHelpStage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Controller for the help screen view (sudoku-help-view.fxml).
 * This class is responsible for handling user interactions and navigation on the help screen.
 */
public class SudokuHelpController {

    /**
     * Handles the help menu button click event.
     * This method closes the current help stage, removes its instance, and displays the welcome stage.
     * It allows the user to navigate back to the main welcome screen from the help menu.
     *
     * @param event the MouseEvent triggered by the user clicking the help menu button
     * @throws IOException if an I/O error occurs while displaying the welcome stage
     */
    @FXML
    void handleHelpMenu(MouseEvent event) throws IOException {
        SudokuHelpStage.deleteInstance();
        SudokuWelcomeStage.getInstance();
    }
}