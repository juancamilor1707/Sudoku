package com.example.demosudoku.model.user;

/**
 * Represents a player/user in the Sudoku game application.
 * This class encapsulates player information, primarily their nickname,
 * which is used to identify and personalize the gaming experience.
 *
 * The User class serves as a simple model for storing and managing
 * user-specific data that needs to be maintained throughout the game session.
 * It provides methods to create a user, retrieve their nickname, and update it if needed.
 *
 * Key responsibilities:
 * - Store the player's nickname
 * - Provide access to the nickname for display in UI components
 * - Allow modification of the nickname during the game session
 */
public class User {

    /**
     * The nickname of the player.
     * This string represents the player's chosen display name or identifier
     * used throughout the game, particularly in welcome screens, during gameplay,
     * and in victory/congratulations messages.
     */
    private String nickname;

    /**
     * Constructs a User instance with the specified nickname.
     * This constructor initializes a new User object and sets their nickname
     * to the provided value. The nickname is typically entered by the player
     * on the welcome screen before starting a game.
     *
     * @param nickname the player's chosen nickname or display name
     */
    public User(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Retrieves the nickname of the user.
     * This method returns the current nickname associated with this User instance.
     * The nickname is used for personalization purposes, such as displaying
     * it in the victory screen when the player completes the puzzle.
     *
     * @return the user's nickname as a String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets or updates the nickname of the user.
     * This method allows modification of the user's nickname during the game session
     * if needed. The new nickname will be used in all subsequent displays and interactions.
     *
     * @param nickname the new nickname to assign to this user
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}