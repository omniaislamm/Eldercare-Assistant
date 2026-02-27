package com.mycompany.project;

import com.mycompany.project.database.DatabaseHelper;
import com.mycompany.project.view.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Setup Database on start
        DatabaseHelper.setupDatabase();

        // Seed Data for user 'moreda'
        com.mycompany.project.util.DataSeeder.seedData();

        // Use Nimbus Look and Feel for better aesthetics
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to default
        }

        // Launch Login Frame
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
