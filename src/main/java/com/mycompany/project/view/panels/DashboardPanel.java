// الواجهة الرئيسية (لوحة المعلومات)
package com.mycompany.project.view.panels;

import com.mycompany.project.model.User;
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import java.awt.*;

// بانل الرئيسي اللي بيعرض الكروت (Medicines, Readings, Doctors, etc.)
public class DashboardPanel extends JPanel {
    private User currentUser;
    private Runnable onMedicinesClick;
    private Runnable onReadingsClick;
    // private Runnable onReportsClick; // تم إزالته
    private Runnable onDoctorsClick;
    private Runnable onAlertsClick; // زر تشغيل/إيقاف التنبيهات

    public DashboardPanel(User user, Runnable onMedicines, Runnable onReadings,
            // Runnable onReports, // تم إزالته
            Runnable onDoctors, Runnable onAlerts) {
        this.currentUser = user;
        this.onMedicinesClick = onMedicines;
        this.onReadingsClick = onReadings;
        // this.onReportsClick = onReports; // تم إزالته
        this.onDoctorsClick = onDoctors;
        this.onAlertsClick = onAlerts; // للتبديل بين ON/OFF

        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND_COLOR);

        initComponents(); // بناء الواجهة
    }

    private void initComponents() {
        // Main Grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(Theme.BACKGROUND_COLOR);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gridPanel.add(createCard("Medicines", "Manage your meds", "💊", onMedicinesClick));
        gridPanel.add(createCard("Health Dashboard", "Record & View History", "📊", onReadingsClick));
        gridPanel.add(createCard("Doctors", "Manage Doctors", "👨‍⚕️", onDoctorsClick));

        // Alerts Toggle Button - ON/OFF
        gridPanel.add(createCard("Alerts", "Toggle Notifications", "🔔", onAlertsClick != null ? onAlertsClick : () -> {
            JOptionPane.showMessageDialog(this, "Alerts feature unavailable", "Info", JOptionPane.INFORMATION_MESSAGE);
        }));

        // Profile instead of Settings
        gridPanel.add(createCard("Profile", "View Personal Info", "👤", () -> {
            new com.mycompany.project.view.dialogs.UserProfileDialog(SwingUtilities.getWindowAncestor(this),
                    currentUser).setVisible(true);
        }));

        add(gridPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String subtitle, String icon, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Theme.SUB_HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_COLOR);

        JLabel subLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subLabel.setFont(Theme.REGULAR_FONT);
        subLabel.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(subLabel);

        card.add(iconLabel, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (action != null)
                    action.run();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 245));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }
}
