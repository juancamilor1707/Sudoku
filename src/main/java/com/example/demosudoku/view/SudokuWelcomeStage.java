package com.example.demosudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the Sudoku welcome window.
 * This class ensures that only one instance of the welcome stage can exist at a time,
 * serving as the primary entry point for the application where players can start a game
 * or access help information.
 *
 * SudokuWelcomeStage is responsible for:
 * - Loading and displaying the welcome view UI (sudoku-welcome-view.fxml)
 * - Managing the welcome stage lifecycle (creation, display, and cleanup)
 * - Ensuring only one welcome window exists at any time
 * - Providing a user-friendly interface for game entry and navigation
 *
 * The singleton pattern with lazy initialization ensures efficient resource usage
 * and prevents multiple welcome windows from being created simultaneously.
 */
public class SudokuWelcomeStage extends Stage {

    /**
     * Private constructor to enforce the singleton pattern.
     * This constructor is called only once to create the unique instance of the welcome stage.
     * It performs the following operations:
     * 1. Creates an FXMLLoader to load the sudoku-welcome-view.fxml file
     * 2. Loads the FXML resource and retrieves the root Parent node
     * 3. Creates a Scene with the loaded root node
     * 4. Sets the scene on this Stage
     * 5. Configures stage properties:
     *    - Sets the window title to "Sudoku"
     *    - Disables window resizing
     * 6. Loads and sets the favicon image from the resources
     * 7. Immediately calls show() to display the welcome window to the user
     *
     * @throws IOException if the FXML file cannot be loaded or if resources are not found
     */
    private SudokuWelcomeStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demosudoku/sudoku-welcome-view.fxml")
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
     * This is part of the Bill Pugh Singleton implementation pattern, which ensures
     * thread-safe lazy initialization without requiring synchronization.
     * The INSTANCE field is initialized to null and is populated only when getInstance()
     * is called for the first time.
     */
    private static class Holder {
        private static SudokuWelcomeStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton SudokuWelcomeStage instance.
     * This method implements lazy initialization using the Bill Pugh Singleton pattern.
     * It creates a new instance only if one does not exist yet.
     *
     * The method performs the following logic:
     * 1. Checks if the instance exists in the Holder class
     * 2. If it exists, returns the existing instance
     * 3. If it does not exist, creates a new SudokuWelcomeStage instance
     * 4. Stores the new instance in Holder.INSTANCE
     * 5. Returns the instance to the caller
     *
     * Subsequent calls to this method will return the same instance, ensuring
     * only one welcome window exists at any time.
     *
     * @return The single instance of SudokuWelcomeStage
     * @throws IOException if the FXML file cannot be loaded during instance creation
     */
    public static SudokuWelcomeStage getInstance() throws IOException {
        Holder.INSTANCE = Holder.INSTANCE != null ?
                Holder.INSTANCE : new SudokuWelcomeStage();
        return Holder.INSTANCE;
    }

    /**
     * Closes and removes the singleton instance.
     * This method is used to clean up the welcome stage when it needs to be closed.
     * It performs the following operations:
     * 1. Closes the welcome stage window
     * 2. Sets the instance to null, allowing a new instance to be created later
     *
     * This method should be called when navigating away from the welcome screen
     * to another screen (e.g., game or help screen), ensuring proper cleanup
     * and memory management.
     */
    public static void deleteInstance() {
        Holder.INSTANCE.close();
        Holder.INSTANCE = null;
    }
}