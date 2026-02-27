// الإطار الرئيسي للتطبيق
package com.mycompany.project.view;

import com.mycompany.project.model.User;
import com.mycompany.project.view.panels.DashboardPanel;
import com.mycompany.project.view.panels.DoctorPanel;
import com.mycompany.project.view.panels.MedicinePanel;
import com.mycompany.project.view.panels.ReadingsPanel;
// import com.mycompany.project.view.panels.ReportPanel; // تم إزالته
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import java.awt.*;

// الإطار الأساسي اللي بيحتوي على كل الصفحات
public class MainFrame extends JFrame {
    private User currentUser;
    private CardLayout cardLayout; // للتنقل بين الصفحات
    private JPanel contentPanel;
    private JLabel headerTitle;
    private JButton backButton;
    private boolean notificationsEnabled = true; // التنبيهات مفعّلة افتراضياً

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Patient Management System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents(); // بناء الواجهة
        startAlertTimer(); // تشغيل مؤقت التنبيهات
    }

    // بدء مؤقت التنبيهات (يفحص كل 60 ثانية)
    private void startAlertTimer() {
        if (!"PATIENT".equals(currentUser.getRole()))
            return; // التنبيهات للمرضى فقط

        // Timer يشتغل كل 60 ثانية
        javax.swing.Timer timer = new javax.swing.Timer(60000, e -> {
            checkAndShowAlerts();
        });
        timer.setInitialDelay(5000); // ينتظر 5 ثواني قبل أول فحص
        timer.start();
    }

    // فحص وعرض التنبيهات
    private void checkAndShowAlerts() {
        // لو التنبيهات مش مفعّلة، ما نعملش حاجة
        if (!notificationsEnabled) {
            return;
        }

        com.mycompany.project.controller.AlertController alertController = new com.mycompany.project.controller.AlertController();

        String alertMessage = alertController.checkForAlerts(currentUser);

        if (alertMessage != null) {
            // عرض نافذة التنبيه
            com.mycompany.project.view.dialogs.AlertDialog.showAlert(
                    this,
                    alertMessage,
                    "Alert Notification");
        }
    }

    // تبديل التنبيهات (ON/OFF)
    private void toggleNotifications() {
        notificationsEnabled = !notificationsEnabled;

        String status = notificationsEnabled ? "ON" : "OFF";
        String icon = notificationsEnabled ? "🔔" : "🔕";

        JOptionPane.showMessageDialog(this,
                icon + " Notifications are now " + status,
                "Notification Settings",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponents() {
        // Main Container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Theme.BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        headerTitle = new JLabel("Health Track - " + currentUser.getFullName());
        headerTitle.setFont(Theme.HEADER_FONT);
        headerTitle.setForeground(Theme.WHITE);

        // Left side of Header (Back Button + Title)
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeader.setOpaque(false);

        backButton = new JButton("← Back");
        Theme.styleButton(backButton);
        backButton.setBackground(new Color(0, 0, 0, 0)); // Transparent
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        backButton.setVisible(false); // Hidden initially
        backButton.addActionListener(e -> showDashboard());

        leftHeader.add(backButton);
        leftHeader.add(headerTitle);
        headerPanel.add(leftHeader, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        Theme.styleButton(logoutBtn);
        logoutBtn.setBackground(Theme.ACCENT_COLOR);
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Content Area (CardLayout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BACKGROUND_COLOR);

        // Define Panels
        DashboardPanel dashboard = new DashboardPanel(
                currentUser,
                () -> showCard("Medicines", "My Medicines"),
                () -> showCard("Readings", "Health Dashboard"),
                // () -> showCard("Reports", "Health Reports"), // Removed argument
                () -> showCard("Doctors", "My Doctors"),
                () -> toggleNotifications()); // Toggle ON/OFF

        contentPanel.add(dashboard, "Dashboard");
        contentPanel.add(new DoctorPanel(currentUser), "Doctors");

        if ("PATIENT".equals(currentUser.getRole())) {
            contentPanel.add(new MedicinePanel(currentUser), "Medicines");
            contentPanel.add(new ReadingsPanel(currentUser), "Readings");
            // ReportPanel removed as it's merged into Readings
        } else {
            // For Doctors, show empty or different panels
            contentPanel.add(new JPanel(), "Medicines");
        }

        mainContainer.add(contentPanel, BorderLayout.CENTER);

        add(mainContainer);
    }

    private void showCard(String cardName, String title) {
        if (!"PATIENT".equals(currentUser.getRole()) && (cardName.equals("Medicines") || cardName.equals("Reports"))) {
            JOptionPane.showMessageDialog(this, "This feature is for Patients only.");
            return;
        }
        cardLayout.show(contentPanel, cardName);
        headerTitle.setText(title);
        backButton.setVisible(true);
    }

    private void showDashboard() {
        cardLayout.show(contentPanel, "Dashboard");
        headerTitle.setText("Health Track - " + currentUser.getFullName());
        backButton.setVisible(false);
    }
}
