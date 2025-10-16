package com.example.demosudoku.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * A utility class for creating and displaying standard JavaFX alerts.
 * This class implements the IAlertBox interface and provides a concrete implementation
 * for showing alert dialogs to the user throughout the application.
 *
 * AlertBox is used to display various types of alerts such as:
 * - Error alerts when invalid input is provided (e.g., empty nickname)
 * - Information alerts to notify the user of important events
 * - Warning alerts for critical actions
 * - Confirmation alerts to request user approval
 *
 * This utility class encapsulates the complexity of creating JavaFX Alert objects
 * and provides a simple, reusable interface for displaying alerts across the application.
 * Each alert displayed is configured with a standard title ("Sudoku") and customizable
 * header text, content text, and alert type.
 */
public class AlertBox implements IAlertBox {

    /**
     * The Alert object used to display alert dialogs.
     * This field holds a reference to the current Alert instance being displayed.
     * It is created fresh for each call to showAlert to ensure independent alert dialogs.
     */
    private Alert alert;

    /**
     * Displays an alert dialog to the user.
     * This method implements the IAlertBox interface contract and creates a new Alert
     * with the specified parameters, configures it with standard application settings,
     * and displays it to the user.
     *
     * The implementation performs the following steps:
     * 1. Creates a new Alert object with the specified AlertType
     * 2. Sets the title to "Sudoku" (application name)
     * 3. Sets the header text to the provided headerText parameter
     * 4. Sets the content text to the provided contentText parameter
     * 5. Calls showAndWait() to display the alert and block until user dismissal
     *
     * The alert type determines the visual styling and icon displayed:
     * - Alert.AlertType.ERROR: Displays red styling with an error icon
     * - Alert.AlertType.INFORMATION: Displays blue styling with an information icon
     * - Alert.AlertType.WARNING: Displays yellow/orange styling with a warning icon
     * - Alert.AlertType.CONFIRMATION: Displays OK/Cancel buttons for user confirmation
     *
     * The showAndWait() method ensures that the alert is modal, meaning the user
     * must interact with the alert before returning to the main application.
     *
     * @param headerText the text to display in the header area of the dialog;
     *                   typically a brief title describing the nature of the alert
     *                   (e.g., "Error", "Ingresa un nickname")
     * @param contentText the main content message of the dialog;
     *                    typically a longer description providing detailed information
     *                    (e.g., "You must enter a valid nickname to play the game")
     * @param alertType the type of alert to display, which determines the visual appearance
     *                  and icon; examples include Alert.AlertType.ERROR, Alert.AlertType.INFORMATION,
     *                  Alert.AlertType.WARNING, and Alert.AlertType.CONFIRMATION
     */
    @Override
    public void showAlert(String headerText, String contentText, AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle("Sudoku");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}