package com.example.demosudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the Sudoku win window.
 * This class ensures that only one instance of the victory/win stage can exist at a time,
 * displaying the congratulations screen when a player successfully completes a puzzle.
 *
 * SudokuWinStage is responsible for:
 * - Loading and displaying the win view UI (sudoku-win-view.fxml)
 * - Managing the win stage lifecycle (creation, display, and cleanup)
 * - Ensuring only one win window exists at any time
 * - Providing a congratulations screen with player recognition
 *
 * The singleton pattern with lazy initialization ensures efficient resource usage
 * and prevents multiple win windows from being created simultaneously.
 *
 * Note: This stage is NOT shown in the constructor to allow external code to control
 * when and how the win screen is displayed in the game flow.
 */
public class SudokuWinStage extends Stage {

    /**
     * Private constructor to enforce the singleton pattern.
     * This constructor is called only once to create the unique instance of the win stage.
     * It performs the following operations:
     * 1. Creates an FXMLLoader to load the sudoku-win-view.fxml file
     * 2. Loads the FXML resource and retrieves the root Parent node
     * 3. Creates a Scene with the loaded root node
     * 4. Sets the scene on this Stage
     * 5. Configures stage properties:
     *    - Sets the window title to "Sudoku"
     *    - Disables window resizing
     * 6. Loads and sets the favicon image from the resources
     *
     * Note: The stage is NOT automatically shown in this constructor.
     * This allows external code to control when the win screen appears,
     * enabling proper game flow management.
     *
     * @throws IOException if the FXML file cannot be loaded or if resources are not found
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
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     * This is part of the Bill Pugh Singleton implementation pattern, which ensures
     * thread-safe lazy initialization without requiring synchronization.
     * The INSTANCE field is initialized to null and is populated only when getInstance()
     * is called for the first time.
     */
    private static class Holder {
        private static SudokuWinStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton SudokuWinStage instance.
     * This method implements lazy initialization using the Bill Pugh Singleton pattern.
     * It creates a new instance only if one does not exist yet.
     *
     * The method performs the following logic:
     * 1. Checks if an instance already exists in the Holder class
     * 2. If it does not exist (is null), creates a new SudokuWinStage instance
     * 3. Stores the new instance in Holder.INSTANCE
     * 4. Returns the instance to the caller
     *
     * Subsequent calls to this method will return the same instance, ensuring
     * only one win window can be managed at any time.
     *
     * @return The single instance of SudokuWinStage
     * @throws IOException if the FXML file cannot be loaded during instance creation
     */
    public static SudokuWinStage getInstance() throws IOException {
        if (Holder.INSTANCE == null) {
            Holder.INSTANCE = new SudokuWinStage();
        }
        return Holder.INSTANCE;
    }

    /**
     * Closes and removes the singleton instance.
     * This method is used to clean up the win stage when it needs to be closed.
     * It performs the following operations:
     * 1. Checks if an instance exists before attempting to close it
     * 2. If it exists, closes the win stage window
     * 3. Sets the instance to null, allowing a new instance to be created later
     *
     * This method should be called when the player chooses to return to the menu
     * from the victory screen, ensuring proper cleanup and memory management.
     */
    public static void deleteInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
            Holder.INSTANCE = null;
        }
    }
}