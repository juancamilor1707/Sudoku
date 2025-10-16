package com.example.demosudoku.view;

import com.example.demosudoku.controller.SudokuGameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SudokuGameStage extends Stage {

    private static SudokuGameStage instance;
    private SudokuGameController controller;

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

    public static SudokuGameStage getInstance() throws IOException {
        if (instance == null || !instance.isShowing()) {
            instance = new SudokuGameStage();
        }
        instance.show();
        return instance;
    }

    public static void deleteInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }

    public SudokuGameController getController() {
        return controller;
    }
}