package com.mycompany.project.view.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Simple Alert Dialog to show medicine and doctor reminders
 */
public class AlertDialog {

    /**
     * Shows an alert popup to the user
     * 
     * @param parent       Parent component
     * @param alertMessage The alert message to display
     * @param title        Dialog title
     */
    public static void showAlert(Component parent, String alertMessage, String title) {
        // Play beep sound
        Toolkit.getDefaultToolkit().beep();

        // Show alert dialog
        int result = JOptionPane.showConfirmDialog(
                parent,
                alertMessage + "\n\nMark as acknowledged?",
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // Log if user acknowledged
        if (result == JOptionPane.YES_OPTION) {
            System.out.println("[Alert] User acknowledged the alert");
        }
    }

    /**
     * Shows a simple info message
     * 
     * @param parent  Parent component
     * @param message Message to display
     * @param title   Dialog title
     */
    public static void showInfo(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
