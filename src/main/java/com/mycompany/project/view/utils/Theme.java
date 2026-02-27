package com.mycompany.project.view.utils;

import javax.swing.*;

import java.awt.*;

public class Theme {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(0, 105, 92); // Deep Teal
    public static final Color ACCENT_COLOR = new Color(255, 111, 0); // Amber
    public static final Color BACKGROUND_COLOR = new Color(224, 242, 241); // Light Teal
    public static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark Grey
    public static final Color WHITE = Color.WHITE;

    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUB_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(WHITE);
        button.setFont(BOLD_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(REGULAR_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    public static void styleLabel(JLabel label) {
        label.setFont(REGULAR_FONT);
        label.setForeground(TEXT_COLOR);
    }

    public static void styleHeaderLabel(JLabel label) {
        label.setFont(HEADER_FONT);
        label.setForeground(PRIMARY_COLOR);
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
    }

    public static void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(REGULAR_FONT);
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setBackground(BACKGROUND_COLOR);
    }
}
