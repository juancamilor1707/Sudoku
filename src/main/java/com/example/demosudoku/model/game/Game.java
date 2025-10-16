package com.example.demosudoku.model.game;

import com.example.demosudoku.model.board.Board;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Represents the concrete implementation of the Sudoku game logic.
 */
public class Game extends GameAbstract {

    public Game(GridPane boardGridpane) {
        super(boardGridpane);
    }

    @Override
    public void startGame() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int number = board.getValueAt(i, j);
                System.out.print(number + " ");

                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                textField.setText(String.valueOf(number));
                textField.setBackground(Background.EMPTY);
                textField.setStyle("-fx-font-size: 30px; -fx-text-fill: black;");

                if (number != 0) {
                    textField.setEditable(false);
                } else {
                    textField.setText("");
                }

                handleNumberField(textField, i, j);
                boardGridpane.add(textField, j, i);
            }
            System.out.println();
        }
    }

    private void handleNumberField(TextField txt, int row, int col) {
        txt.setOnKeyReleased(event -> {
            String input = txt.getText().trim();
            if(input.length() > 0){
                try {
                    int num = Integer.parseInt(input);
                    if(num < 1 || num > 6) {
                        txt.clear();
                        highlightError(txt);
                        return;
                    }

                    boolean result = board.isValid(row, col, num);
                    if (!result) {
                        txt.clear();
                        highlightRepeatNumber(txt);
                    } else {
                        board.setValueAt(row, col, num);

                        if (isBoardComplete()) {
                            showWinScreen();
                        }
                    }

                } catch (NumberFormatException e) {
                    highlightError(txt);
                    txt.clear();
                }
            } else {
                board.setValueAt(row, col, 0);
            }
        });
    }

    public void provideHint() {
        System.out.println("=== Buscando celda vacía ===");
        boolean hintGiven = false;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.println("Celda [" + i + "][" + j + "] = " + board.getValueAt(i, j));
                if (board.getValueAt(i, j) == 0) {

                    TextField targetField = null;
                    for (javafx.scene.Node node : boardGridpane.getChildren()) {
                        Integer rowIndex = GridPane.getRowIndex(node);
                        Integer colIndex = GridPane.getColumnIndex(node);

                        int actualRow = (rowIndex == null) ? 0 : rowIndex;
                        int actualCol = (colIndex == null) ? 0 : colIndex;

                        if (actualRow == i && actualCol == j && node instanceof TextField) {
                            targetField = (TextField) node;
                            break;
                        }
                    }

                    if (targetField != null && targetField.getText().trim().isEmpty()) {
                        int correctValue = board.getSolutionValueAt(i, j);

                        if (board.isValid(i, j, correctValue)) {
                            board.setValueAt(i, j, correctValue);
                            targetField.setText(String.valueOf(correctValue));
                            targetField.setEditable(false);
                            targetField.setStyle("-fx-font-size: 30px; -fx-text-fill: blue;-fx-min-height: 100;");
                            System.out.println("Pista en [" + i + "][" + j + "] = " + correctValue);
                            hintGiven = true;
                            break;
                        } else {
                            System.out.println("La solución original ya no es válida en [" + i + "][" + j + "]");
                        }
                    }
                }
            }
            if (hintGiven) break;
        }

        if (isBoardComplete()) {
            System.out.println("¡Tablero completo! Mostrando pantalla de victoria...");
            showWinScreen();
        } else if (!hintGiven) {
            System.out.println("No hay más celdas vacías para dar pista");
        }
    }

    public void setUser(com.example.demosudoku.model.user.User user) {
        this.user = user;
    }

    private boolean isBoardComplete() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board.getValueAt(i, j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinScreen() {
        System.out.println("¡GANASTE!");
        javafx.application.Platform.runLater(() -> {
            try {
                // Obtenemos la ventana del juego
                javafx.stage.Stage gameStage = (javafx.stage.Stage) boardGridpane.getScene().getWindow();

                // Creamos el WinStage
                javafx.stage.Stage winStage = new javafx.stage.Stage();
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                        getClass().getResource("/com/example/demosudoku/sudoku-win-view.fxml")
                );
                javafx.scene.Parent root = loader.load();

                // Pasamos el nickname al controlador
                com.example.demosudoku.controller.SudokuWinController winController = loader.getController();
                if (user != null) {
                    winController.setVictoryMessage(user.getNickname());
                }

                winStage.setScene(new javafx.scene.Scene(root));
                winStage.setTitle("Sudoku - Victory!");
                winStage.setResizable(false);

                // IMPORTANTE: Cuando se cierra WinStage, cerramos GameStage
                winStage.setOnHidden(e -> {
                    gameStage.close();
                });

                // Ocultamos el juego y mostramos la victoria
                gameStage.hide();
                winStage.show();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void highlightError(TextField txt) {
        txt.setStyle("-fx-border-color: red; -fx-background-color: #F75270; -fx-border-width: 3px; -fx-font-size: 30px;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> resetStyle(txt));
        pause.play();
    }

    private void highlightRepeatNumber(TextField txt) {
        txt.setStyle("-fx-border-color: yellow; -fx-background-color: #FBF3D1; -fx-border-width: 3px; -fx-font-size: 30px;");

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> resetStyle(txt));
        pause.play();
    }

    private void resetStyle(TextField txt) {
        txt.setStyle("-fx-border-color: transparent; -fx-font-size: 30px; -fx-text-fill: black;");
    }
}