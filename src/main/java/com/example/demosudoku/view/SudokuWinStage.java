package com.example.demosudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the Sudoku Win window.
 * This class ensures that only one instance of the win window can exist at a time.
 */
public class SudokuWinStage extends Stage {

    // --- Core Stage Logic (Constructor) ---

    /**
     * Private constructor to enforce the singleton pattern. It loads the FXML view,
     * sets up the scene, and configures the stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private SudokuWinStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demosudoku/sudoku-win-view.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Sudoku");
        setResizable(false);
        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/com/example/demosudoku/favicon.png")))
        );
        // Do NOT call show() here. Stages should be shown externally
        // to control when they appear, especially in a game flow.
    }

    // --- Singleton Implementation (Correction) ---

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        // ERROR CORRECTION 1: The type must be the class itself (SudokuWinStage)
        private static SudokuWinStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton SudokuWinStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return The single instance of SudokuWinStage.
     * @throws IOException if the FXML file cannot be loaded during the first creation.
     */
    public static SudokuWinStage getInstance() throws IOException {
        // ERROR CORRECTION 2: The return type must match the class (SudokuWinStage)
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new SudokuWinStage();
        }
        return Holder.INSTANCE;
    }

    //cambio

    /**
     * Closes the stage, effectively deleting the instance from view.
     */
    public static void deleteInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
            Holder.INSTANCE = null;
        }
    }
}