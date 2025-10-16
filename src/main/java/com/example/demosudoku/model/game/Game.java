    package com.example.demosudoku.model.game;

    import javafx.animation.PauseTransition;
    import javafx.geometry.Pos;
    import javafx.scene.control.TextField;
    import javafx.scene.control.Label;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.Background;
    import javafx.scene.layout.GridPane;
    import javafx.util.Duration;

    /**
     * Represents the concrete implementation of the Sudoku game logic.
     * This class is responsible for setting up the game board UI and handling user input.
     */
    public class Game extends GameAbstract {
        /**
         * Constructs a new Game instance.
         *
         * @param boardGridpane The GridPane from the view where the Sudoku board will be rendered.
         */
        public Game(GridPane boardGridpane) {
            super(boardGridpane);
        }

        /**
         * Starts the game by generating a board, creating UI components (TextFields) for each cell,
         * and adding them to the GridPane. It also sets properties for each cell, such as editability.
         */
        @Override
        public void startGame() {
            for (int i = 0; i < board.getBoard().size(); i++) {
                for (int j = 0; j < board.getBoard().get(i).size(); j++) {
                    int number = board.getBoard().get(i).get(j);
                    System.out.print(number + " ");

                    TextField textField = new TextField();
                    textField.setAlignment(Pos.CENTER);
                    textField.setText(String.valueOf(number));
                    textField.setBackground(Background.EMPTY);
                    textField.setStyle("-fx-font-size: 30px; -fx-text-fill: black;");
                    if (number != 0) {
                        textField.setEditable(false);
                    } else{
                        textField.setText("");
                    }
                    handleNumberField(textField, i, j);
                    boardGridpane.add(textField, j, i);


                }
                System.out.println();
            }
        }

        /**
         * Attaches a key released event handler to a TextField cell. When the key is released,
         * it validates the number entered by the user against the Sudoku rules.
         *
         * @param txt The TextField to which the handler will be attached.
         * @param row The row index of the cell in the board.
         * @param col The column index of the cell in the board.
         */
        // En Game.java, reemplaza handleNumberField():

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
                            board.getBoard().get(row).set(col, num);
                        }

                    } catch (NumberFormatException e) {
                        highlightError(txt);
                        txt.clear();
                    }
                } else {
                    board.getBoard().get(row).set(col, 0);
                }
            });
        }

        public void provideHint() {
            System.out.println("=== Buscando celda vacía ===");
            for (int i = 0; i < board.getBoard().size(); i++) {
                for (int j = 0; j < board.getBoard().get(i).size(); j++) {
                    System.out.println("Celda [" + i + "][" + j + "] = " + board.getBoard().get(i).get(j));
                    if (board.getBoard().get(i).get(j) == 0) {

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
                            int correctValue = board.getSolution().get(i).get(j);

                            if (board.isValid(i, j, correctValue)) {
                                board.getBoard().get(i).set(j, correctValue);
                                targetField.setText(String.valueOf(correctValue));
                                targetField.setEditable(false);
                                targetField.setStyle("-fx-font-size: 30px; -fx-text-fill: blue;-fx-min-height: 100;");
                                System.out.println("Pista en [" + i + "][" + j + "] = " + correctValue);
                                return;
                            } else {
                                System.out.println("La solución original ya no es válida en [" + i + "][" + j + "] debido a números que digitaste");
                            }
                        }
                    }
                }
            }
            System.out.println("No hay más celdas vacías para dar pista");
        }


        private void highlightError(TextField txt) {
            txt.setStyle("-fx-border-color: red; -fx-background-color: #F75270; -fx-border-width: 3px; -fx-font-size: 30px;");

            PauseTransition pause = new PauseTransition(Duration.seconds(1)); // 1 second
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
