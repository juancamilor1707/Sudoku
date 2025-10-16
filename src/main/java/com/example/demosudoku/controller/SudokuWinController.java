package com.example.demosudoku.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label; // Import for the Label component
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SudokuWinController {

    // FXML variable linked to the Label in sudoku-win-view.fxml
    // This is where the nickname will be displayed.
    @FXML
    private Label nicknameLabel;

    /**
     * Public method to receive the nickname and update the victory message Label.
     * This method must be called by the launching controller (e.g., the Game or Welcome controller).
     * @param nickname The user's nickname passed from the previous screen.
     */
    public void setVictoryMessage(String nickname) {
        // Update the Label text with the user's nickname
        nicknameLabel.setText("Congratulations, " + nickname + "! You Won!");

        // Note: You might need to adjust the font size or layout in FXML/CSS
        // if the message is too long for the Label.
    }

    /**
     * Handles the click event for the "Go Back" button (or similar).
     * Navigates the user back to the welcome/main menu screen.
     * @param event The MouseEvent triggered by the button click.
     */
    @FXML
    private void goToGame(MouseEvent event) {
        try {
            // 1. Create the FXMLLoader and load the new FXML resource (Welcome View)
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/demosudoku/sudoku-welcome-view.fxml")
            );

            // 2. Load the root node of the new scene
            Parent root = loader.load();

            // 3. Get the current Stage (window) from the button/event source
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            // 4. Create a new Scene with the loaded root node
            Scene scene = new Scene(root);

            // 5. Set the new scene on the stage and display it
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            // Handle error if the FXML file is not found or cannot be loaded
            System.err.println("Error loading sudoku-welcome-view.fxml: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}