package com.example.demosudoku.view;

import com.example.demosudoku.controller.SudokuGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the Sudoku game window.
 * This class ensures that only one instance of the game stage can exist at a time,
 * managing the primary game interface where players interact with the Sudoku puzzle.
 *
 * SudokuGameStage is responsible for:
 * - Loading and displaying the game view UI (sudoku-game-view.fxml)
 * - Maintaining the game controller reference
 * - Managing the game stage lifecycle (creation, display, and cleanup)
 * - Providing access to the game controller for external components
 * - Handling stage closure and instance cleanup
 *
 * The singleton pattern ensures that only one game window exists at any time,
 * preventing multiple instances and ensuring consistent state management.
 */
public class SudokuGameStage extends Stage {

    /**
     * The static singleton instance of SudokuGameStage.
     * This field holds the unique instance of the stage, ensuring only one
     * game window can be open at a time. It is set to null when the stage is closed
     * to allow for proper cleanup and re-instantiation if needed.
     */
    private static SudokuGameStage instance;

    /**
     * The controller for the game stage.
     * This controller manages the game board UI and handles player interactions.
     * It is retrieved from the FXMLLoader during stage construction and provides
     * methods to control the game flow and update the game board display.
     */
    private SudokuGameController controller;

    /**
     * Private constructor to enforce the singleton pattern.
     * This constructor is called only once to create the unique instance of the game stage.
     * It performs the following operations:
     * 1. Creates an FXMLLoader to load the sudoku-game-view.fxml file
     * 2. Loads the FXML resource and retrieves the root Parent node
     * 3. Obtains the SudokuGameController from the FXMLLoader
     * 4. Creates a Scene with the loaded root node
     * 5. Sets the scene on this Stage
     * 6. Configures stage properties:
     *    - Sets the window title to "Sudoku Game"
     *    - Disables window resizing
     * 7. Loads and sets the favicon image from the resources
     * 8. Sets up a close handler that sets the instance to null when the stage is closed
     *    This allows the stage to be recreated if needed
     *
     * @throws IOException if the FXML file cannot be loaded or if resources are not found
     */
    private SudokuGameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/demosudoku/sudoku-game-view.fxml")
        );
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Sudoku Game");
        setResizable(false);
        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/com/example/demosudoku/favicon.png")))
        );

        setOnCloseRequest(event -> {
            instance = null;
        });
    }

    /**
     * Provides global access to the singleton SudokuGameStage instance.
     * This method implements lazy initialization of the singleton pattern.
     * It creates a new instance only if one does not exist or if the current instance
     * is not being displayed. The stage is automatically shown when accessed.
     *
     * The method performs the following logic:
     * 1. Checks if the instance is null or not currently showing
     * 2. If either condition is true, creates a new SudokuGameStage instance
     * 3. Calls show() on the instance to make it visible
     * 4. Returns the instance to the caller
     *
     * This ensures that only one game window is displayed at a time, and any previous
     * instance is properly replaced if it has been closed.
     *
     * @return The single instance of SudokuGameStage
     * @throws IOException if the FXML file cannot be loaded during instance creation
     */
    public static SudokuGameStage getInstance() throws IOException {
        if (instance == null || !instance.isShowing()) {
            instance = new SudokuGameStage();
        }
        instance.show();
        return instance;
    }

    /**
     * Closes and removes the singleton instance.
     * This method is used to clean up the game stage when it needs to be closed.
     * It performs the following operations:
     * 1. Checks if an instance exists
     * 2. If it does, closes the stage window
     * 3. Sets the instance to null, allowing a new instance to be created later
     *
     * This method should be called when navigating away from the game screen
     * to the welcome screen or help screen, ensuring proper cleanup and memory management.
     */
    public static void deleteInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }

    /**
     * Retrieves the controller associated with this game stage.
     * This method provides access to the SudokuGameController, allowing other components
     * to interact with the game logic and UI. The controller is obtained from the FXML loader
     * during stage construction.
     *
     * @return the SudokuGameController managing this stage's game interface
     */
    public SudokuGameController getController() {
        return controller;
    }
}