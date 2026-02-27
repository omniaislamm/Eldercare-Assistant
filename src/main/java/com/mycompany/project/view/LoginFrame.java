package com.mycompany.project.view;

import com.mycompany.project.controller.UserController;
import com.mycompany.project.model.User;
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserController userController;

    public LoginFrame() {
        userController = new UserController();
        setTitle("Patient Management System - Login");
        setSize(450, 400); // Slightly larger
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply Background
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        Theme.styleHeaderLabel(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Subtitle
        JLabel subLabel = new JLabel("Please login to your account", SwingConstants.CENTER);
        Theme.styleLabel(subLabel);
        gbc.gridy = 1;
        add(subLabel, gbc);

        // Username
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel("Username:");
        Theme.styleLabel(userLabel);
        add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        Theme.styleTextField(usernameField);
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passLabel = new JLabel("Password:");
        Theme.styleLabel(passLabel);
        add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        Theme.styleTextField(passwordField);
        add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Theme.BACKGROUND_COLOR);

        JButton loginButton = new JButton("Login");
        Theme.styleButton(loginButton);
        loginButton.addActionListener(this::handleLogin);
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
        Theme.styleButton(registerButton);
        registerButton.setBackground(Theme.ACCENT_COLOR); // Accent for secondary action
        registerButton.addActionListener(e -> openRegisterFrame());
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = userController.login(username, password);
        if (user != null) {
            new MainFrame(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterFrame() {
        new RegisterFrame(this).setVisible(true);
        this.setVisible(false);
    }
}
