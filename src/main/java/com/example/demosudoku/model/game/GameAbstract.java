package com.example.demosudoku.model.game;

import com.example.demosudoku.model.board.Board;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * An abstract base class for game logic.
 */
public class GameAbstract implements IGame {
    protected GridPane boardGridpane;
    protected Board board;
    protected ArrayList<TextField> numberFields;
    protected Button buttonGame;
    protected com.example.demosudoku.model.user.User user;

    public GameAbstract(GridPane boardGridpane) {
        this.boardGridpane = boardGridpane;
        this.board = new Board();
        this.numberFields = new ArrayList<TextField>();
        this.buttonGame = new Button();
    }

    @Override
    public void startGame() {
    }
}