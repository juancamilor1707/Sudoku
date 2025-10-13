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
                    textField.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
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
        private void handleNumberField(TextField txt, int row, int col) {

            txt.setOnKeyReleased(event -> {
                String input = txt.getText().trim();
                if(input.length() > 0){
                    try {
                        int num = Integer.parseInt(input);
                        if(num < 1 || num > 6) {
                            System.out.println("no bro, sal de ahí");
                            txt.clear();
                            highlightError(txt);
                            return;
                        }

                        boolean result = board.isValid(row, col, Integer.parseInt(input));
                        System.out.println(result);

                    } catch (NumberFormatException e) {
                        System.out.println("invalido");
                        highlightError(txt);
                        txt.clear();
                    }
                }
            });
        }

        public void provideHint() {
            for (int i = 0; i < board.getBoard().size(); i++) {
                for (int j = 0; j < board.getBoard().get(i).size(); j++) {
                    if (board.getBoard().get(i).get(j) == 0) {
                        int correctValue = board.getSolution().get(i).get(j);
                        board.getBoard().get(i).set(j, correctValue);
                        System.out.println("Pista en [" + i + "][" + j + "] = " + correctValue);
                        return;
                    }
                }
            }
        }


        private void highlightError(TextField txt) {
            txt.setStyle("-fx-border-color: red; -fx-border-width: 4px; -fx-font-size: 20px; -fx-text-fill: white;");

            PauseTransition pause = new PauseTransition(Duration.seconds(1)); // 1 second
            pause.setOnFinished(event -> resetStyle(txt));
            pause.play();
        }

        private void resetStyle(TextField txt) {
            txt.setStyle("-fx-border-color: transparent; -fx-font-size: 20px; -fx-text-fill: white;");
        }


    }
