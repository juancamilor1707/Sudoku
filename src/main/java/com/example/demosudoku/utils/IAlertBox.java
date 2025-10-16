package com.example.demosudoku.utils;

import javafx.scene.control.Alert;

/**
 * Defines the contract for an alert box utility.
 * This interface establishes the standard method for creating and displaying
 * alert dialogs to the user. Any class that implements this interface must
 * provide a way to show alerts with customizable header text, content text,
 * and alert type.
 *
 * Alert boxes are commonly used to:
 * - Display error messages when invalid input is provided
 * - Show informational messages about game state or events
 * - Confirm user actions or provide warnings
 * - Provide user feedback in response to various application events
 */
public interface IAlertBox {

    /**
     * Displays an alert dialog to the user.
     * This method creates and shows a modal alert window that remains visible
     * until the user acknowledges it by clicking the OK button or closing the dialog.
     *
     * The alert can be customized with:
     * - A header text that typically describes the general nature or category of the alert
     * - A content text that provides the detailed message or information
     * - An alert type that determines the visual style and icon displayed (e.g., ERROR in red, INFORMATION in blue)
     *
     * This method should block further user interaction until the alert is dismissed,
     * ensuring the user sees and acknowledges the message before continuing.
     *
     * @param headerText the text to display in the header area of the dialog; typically a brief title
     *                   describing the category or nature of the alert (e.g., "Error", "Warning", "Success")
     * @param contentText the main content message of the dialog; typically a longer description
     *                    providing detailed information about the alert (e.g., "Please enter a valid nickname")
     * @param alertType the type of alert to display, which determines the visual appearance and icon;
     *                  common types include Alert.AlertType.ERROR, Alert.AlertType.INFORMATION,
     *                  Alert.AlertType.WARNING, and Alert.AlertType.CONFIRMATION
     */
    void showAlert(String headerText, String contentText, Alert.AlertType alertType);
}