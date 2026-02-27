package com.mycompany.project.view.panels;

import com.mycompany.project.controller.ReportController;
import com.mycompany.project.model.Measurement;
import com.mycompany.project.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ReadingsPanel extends JPanel {
    private User currentUser;
    private ReportController reportController;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JPanel summaryPanel;

    // Input Fields
    private JComboBox<String> typeCombo;
    private JTextField valueField;

    public ReadingsPanel(User user) {
        this.currentUser = user;
        this.reportController = new ReportController();

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250)); // Softer background
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        loadData();
    }

    private void initComponents() {
        // --- 1. Top Panel: Add New Reading ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(null); // Transparent

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        // Title for Section
        JLabel formTitle = new JLabel("Add New Reading");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        formTitle.setForeground(new Color(50, 50, 50));
        inputPanel.add(formTitle);

        // Separator
        JLabel separator = new JLabel("  |  ");
        separator.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        separator.setForeground(new Color(200, 200, 200));
        inputPanel.add(separator);

        // Type Label
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputPanel.add(typeLabel);

        // Type ComboBox
        String[] types = { "BLOOD_PRESSURE", "SUGAR", "HEART_RATE", "OXYGEN", "ANEMIA" };
        typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(180, 32));
        typeCombo.setBackground(Color.WHITE);
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputPanel.add(typeCombo);

        // Value Label
        JLabel valueLabel = new JLabel("  Value:");
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputPanel.add(valueLabel);

        // Value TextField - cleaner design without titled border
        valueField = new JTextField();
        valueField.setPreferredSize(new Dimension(150, 32));
        valueField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        inputPanel.add(valueField);

        // Button
        JButton addBtn = new JButton("Save Record");
        addBtn.setPreferredSize(new Dimension(130, 32));
        addBtn.setBackground(new Color(0, 150, 136)); // Teal
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        addBtn.addActionListener(e -> saveReading());
        inputPanel.add(addBtn);

        topContainer.add(inputPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // --- 2. Middle: Weekly Summary Cards ---
        // We will fill this dynamically in loadData
        summaryPanel = new JPanel(new GridLayout(1, 4, 15, 0)); // 4 columns
        summaryPanel.setBackground(null);
        summaryPanel.setPreferredSize(new Dimension(0, 100));

        // Wrapper for summary to give it a title
        JPanel summaryWrapper = new JPanel(new BorderLayout(0, 10));
        summaryWrapper.setBackground(null);
        JLabel summaryTitle = new JLabel("📅 Last 7 Days Average & Status");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryTitle.setForeground(new Color(80, 80, 80));

        summaryWrapper.add(summaryTitle, BorderLayout.NORTH);
        summaryWrapper.add(summaryPanel, BorderLayout.CENTER);

        // --- 3. Bottom: History Table ---
        String[] columns = { "Date", "Health Type", "Value", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        historyTable.getTableHeader().setBackground(new Color(230, 230, 230));
        historyTable.setSelectionBackground(new Color(144, 12, 63)); // لون بيرجاندي
        historyTable.setSelectionForeground(Color.WHITE); // نص أبيض
        historyTable.setShowVerticalLines(false);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Split Pane or Box Panel
        JPanel centerContainer = new JPanel(new BorderLayout(0, 20));
        centerContainer.setBackground(null);
        centerContainer.add(summaryWrapper, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);
    }

    private void createSummaryCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, color), // Left colored strip
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLbl.setForeground(Color.GRAY);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(new Color(50, 50, 50));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valLbl, BorderLayout.CENTER);

        summaryPanel.add(card);
    }

    private void saveReading() {
        String type = (String) typeCombo.getSelectedItem();
        String value = valueField.getText().trim();

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a value!", "Missing Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Measurement m = new Measurement();
        m.setPatientId(currentUser.getId());
        m.setType(type);
        m.setValue(value);
        m.setRecordDate(new java.sql.Date(System.currentTimeMillis()));

        if (reportController.addMeasurement(m)) {
            JOptionPane.showMessageDialog(this, "Reading Saved!");
            valueField.setText("");
            loadData(); // Refresh everything
        } else {
            JOptionPane.showMessageDialog(this, "Error saving data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        // 1. Load Summary
        summaryPanel.removeAll();
        Map<String, String> stats = reportController.getWeeklyStats(currentUser.getId());

        // Define key metrics to always show
        addStatCard("Avg Pressure", stats.getOrDefault("BLOOD_PRESSURE", "-"), new Color(41, 128, 185));
        addStatCard("Avg Sugar", stats.getOrDefault("SUGAR", "-"), new Color(231, 76, 60));
        addStatCard("Avg Heart Rate", stats.getOrDefault("HEART_RATE", "-"), new Color(155, 89, 182));
        addStatCard("Avg Oxygen", stats.getOrDefault("OXYGEN", "-"), new Color(39, 174, 96));

        summaryPanel.revalidate();
        summaryPanel.repaint();

        // 2. Load Table
        tableModel.setRowCount(0);
        List<Measurement> list = reportController.getAllMeasurements(currentUser.getId());
        for (Measurement m : list) {
            tableModel.addRow(new Object[] {
                    m.getRecordDate().toString(),
                    m.getType().replace("_", " "),
                    m.getValue(),
                    "Saved"
            });
        }
    }

    private void addStatCard(String title, String val, Color c) {
        if (val.equals("-"))
            val = "N/A";
        createSummaryCard(title, val, c);
    }
}