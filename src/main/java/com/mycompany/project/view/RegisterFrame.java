package com.mycompany.project.view;

import com.mycompany.project.controller.UserController;
import com.mycompany.project.model.User;
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField, fullNameField, ageField;
    private JPasswordField passwordField;
    private JComboBox<String> genderBox;
    private JCheckBox pressureCheck, sugarCheck, anemiaCheck, heartCheck, asthmaCheck, cholesterolCheck, kidneyCheck,
            vitaminCheck;
    private UserController userController;
    private LoginFrame parentFrame;

    public RegisterFrame(LoginFrame parent) {
        this.parentFrame = parent;
        userController = new UserController();
        setTitle("Create Account");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);

        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header
        JLabel title = new JLabel("Registration", SwingConstants.CENTER);
        Theme.styleHeaderLabel(title);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Fields with labels
        addField("Username:", usernameField = new JTextField(), 1, gbc);
        addField("Password:", passwordField = new JPasswordField(), 2, gbc);
        addField("Full Name:", fullNameField = new JTextField(), 3, gbc);
        addField("Age:", ageField = new JTextField(), 4, gbc);

        // Gender
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel gLabel = new JLabel("Gender:");
        Theme.styleLabel(gLabel);
        add(gLabel, gbc);

        gbc.gridx = 1;
        genderBox = new JComboBox<>(new String[] { "Male", "Female" });
        genderBox.setFont(Theme.REGULAR_FONT);
        add(genderBox, gbc);

        // Medical History
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel mLabel = new JLabel("Conditions:");
        Theme.styleLabel(mLabel);
        add(mLabel, gbc);

        gbc.gridx = 1;
        JPanel diseasePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        diseasePanel.setBackground(Theme.BACKGROUND_COLOR);
        pressureCheck = new JCheckBox("Pressure");
        pressureCheck.setBackground(Theme.BACKGROUND_COLOR);
        Theme.styleCheckBox(pressureCheck);
        sugarCheck = new JCheckBox("Sugar");
        sugarCheck.setBackground(Theme.BACKGROUND_COLOR);
        Theme.styleCheckBox(sugarCheck);
        anemiaCheck = new JCheckBox("Anemia");
        anemiaCheck.setBackground(Theme.BACKGROUND_COLOR);
        Theme.styleCheckBox(anemiaCheck);

        heartCheck = new JCheckBox("Heart Disease");
        Theme.styleCheckBox(heartCheck);

        asthmaCheck = new JCheckBox("Asthma");
        Theme.styleCheckBox(asthmaCheck);

        cholesterolCheck = new JCheckBox("Cholesterol");
        Theme.styleCheckBox(cholesterolCheck);

        kidneyCheck = new JCheckBox("Kidney Disease");
        Theme.styleCheckBox(kidneyCheck);

        vitaminCheck = new JCheckBox("Vit D Deficiency");
        Theme.styleCheckBox(vitaminCheck);

        diseasePanel.add(pressureCheck);
        diseasePanel.add(sugarCheck);
        diseasePanel.add(anemiaCheck);
        diseasePanel.add(heartCheck);
        diseasePanel.add(asthmaCheck);
        diseasePanel.add(cholesterolCheck);
        diseasePanel.add(kidneyCheck);
        diseasePanel.add(vitaminCheck);
        add(diseasePanel, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Theme.BACKGROUND_COLOR);

        JButton registerButton = new JButton("Register");
        Theme.styleButton(registerButton);
        registerButton.addActionListener(e -> handleRegister());
        btnPanel.add(registerButton);

        JButton backButton = new JButton("Back");
        Theme.styleButton(backButton);
        backButton.setBackground(Theme.ACCENT_COLOR);
        backButton.addActionListener(e -> {
            parentFrame.setVisible(true);
            this.dispose();
        });
        btnPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);
    }

    private void addField(String labelText, JTextField field, int y, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        Theme.styleLabel(label);
        add(label, gbc);

        gbc.gridx = 1;
        Theme.styleTextField(field);
        add(field, gbc);
    }

    private void handleRegister() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String gender = (String) genderBox.getSelectedItem();
            String role = "PATIENT";

            StringBuilder diseases = new StringBuilder();
            if (pressureCheck.isSelected())
                diseases.append("Pressure,");
            if (sugarCheck.isSelected())
                diseases.append("Sugar,");
            if (anemiaCheck.isSelected())
                diseases.append("Anemia,");
            if (heartCheck.isSelected())
                diseases.append("Heart,");
            if (asthmaCheck.isSelected())
                diseases.append("Asthma,");
            if (cholesterolCheck.isSelected())
                diseases.append("Cholesterol,");
            if (kidneyCheck.isSelected())
                diseases.append("Kidney,");
            if (vitaminCheck.isSelected())
                diseases.append("VitaminD,");

            User user = new User(0, username, password, role, fullName, gender, age);

            if (userController.register(user, diseases.toString())) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                parentFrame.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Age entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
