package com.example.demosudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuHelpStage extends Stage {
    /**
     * Private constructor to enforce the singleton pattern. It loads the FXML view,
     * sets up the scene, and configures the stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded.
     */
    private SudokuHelpStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demosudoku/sudoku-help-view.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Sudoku");
        setResizable(false);
        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/com/example/demosudoku/favicon.png")))
        );
        show();
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static SudokuHelpStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton SudokuHelpStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return The single instance of SudokuHelpStage.
     * @throws IOException if the FXML file cannot be loaded during the first creation.
     */
    public static SudokuHelpStage getInstance() throws IOException {
        Holder.INSTANCE = Holder.INSTANCE != null ?
                Holder.INSTANCE : new SudokuHelpStage();
        return Holder.INSTANCE;
    }

    /**
     * Closes the stage, effectively deleting the instance from view.
     */
    public static void deleteInstance() {
        Holder.INSTANCE.close();
        Holder.INSTANCE = null;
    }

}
