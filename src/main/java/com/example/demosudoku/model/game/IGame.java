package com.example.demosudoku.model.game;

/**
 * Defines the contract for a game class. Any class that implements
 * this interface must provide a method to start the game.
 * This interface establishes the core behavior expected from any game implementation,
 * ensuring that all game classes follow a consistent pattern for initialization and startup.
 */
public interface IGame {
    /**
     * Initializes and starts the game logic.
     * This method is called to begin the game, setting up the initial game state,
     * creating the game board, initializing UI components, and preparing for player interaction.
     * Implementations should handle all necessary setup operations required to transition
     * the application from a menu or welcome state to an active playable game state.
     */
    void startGame();
}