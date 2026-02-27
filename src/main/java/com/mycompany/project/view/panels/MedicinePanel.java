// واجهة إدارة الأدوية
package com.mycompany.project.view.panels;

import com.mycompany.project.controller.MedicineController;
import com.mycompany.project.model.Medicine;
import com.mycompany.project.model.User;
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

// بانل إدارة الأدوية
public class MedicinePanel extends JPanel {
    private MedicineController medicineController;
    private User currentUser;
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private List<Medicine> medicineList;

    public MedicinePanel(User user) {
        this.currentUser = user;
        this.medicineController = new MedicineController();
        setLayout(new BorderLayout());
        Theme.stylePanel(this);
        initComponents();
        loadMedicines();
    }

    private void initComponents() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Theme.BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Theme.BACKGROUND_COLOR);

        JButton addBtn = new JButton("Add Medicine");
        Theme.styleButton(addBtn);
        addBtn.addActionListener(e -> showMedicineDialog(null));
        topPanel.add(addBtn);

        JButton editBtn = new JButton("Edit");
        Theme.styleButton(editBtn);
        editBtn.setBackground(Theme.ACCENT_COLOR);
        editBtn.addActionListener(e -> {
            int row = medicineTable.getSelectedRow();
            if (row != -1) {
                showMedicineDialog(medicineList.get(row));
            }
        });
        topPanel.add(editBtn);

        JButton deleteBtn = new JButton("Delete");
        Theme.styleButton(deleteBtn);
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteMedicine());
        topPanel.add(deleteBtn);

        JButton testBtn = new JButton("Test Reminder");
        Theme.styleButton(testBtn);
        testBtn.setBackground(Theme.ACCENT_COLOR);
        testBtn.addActionListener(e -> showTestReminder());
        topPanel.add(testBtn);

        centerPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Name", "Dosage", "Times/Day", "Schedule", "Start", "Duration" };
        tableModel = new DefaultTableModel(columns, 0);
        medicineTable = new JTable(tableModel);
        medicineTable.setFont(Theme.REGULAR_FONT);
        medicineTable.setRowHeight(30);

        JTableHeader header = medicineTable.getTableHeader();
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setForeground(Theme.WHITE);
        header.setFont(Theme.BOLD_FONT);

        centerPanel.add(new JScrollPane(medicineTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadMedicines() {
        tableModel.setRowCount(0);
        medicineList = medicineController.getMedicinesForPatient(currentUser.getId());
        for (Medicine m : medicineList) {
            tableModel.addRow(new Object[] {
                    m.getName(), m.getDosage(), m.getTimesPerDay(),
                    m.getScheduleTimes() != null ? m.getScheduleTimes() : "",
                    m.getStartDate(), m.getDurationDays()
            });
        }
    }

    // دالة واحدة للإضافة والتعديل
    private void showMedicineDialog(Medicine med) {
        boolean isEdit = (med != null);

        JTextField nameF = new JTextField(isEdit ? med.getName() : "");
        JTextField dosageF = new JTextField(isEdit ? med.getDosage() : "");
        JTextField timesF = new JTextField(isEdit ? String.valueOf(med.getTimesPerDay()) : "");
        JTextField scheduleF = new JTextField(
                isEdit ? (med.getScheduleTimes() != null ? med.getScheduleTimes() : "") : "");
        JTextField durationF = new JTextField(isEdit ? String.valueOf(med.getDurationDays()) : "");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameF);
        panel.add(new JLabel("Dosage:"));
        panel.add(dosageF);
        panel.add(new JLabel("Times/Day:"));
        panel.add(timesF);
        panel.add(new JLabel("Schedule (e.g., 9:00,15:00):"));
        panel.add(scheduleF);
        panel.add(new JLabel("Duration (days):"));
        panel.add(durationF);

        int result = JOptionPane.showConfirmDialog(null, panel,
                isEdit ? "Edit Medicine" : "Add Medicine", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Medicine m = new Medicine(
                        isEdit ? med.getId() : 0,
                        currentUser.getId(),
                        nameF.getText(),
                        dosageF.getText(),
                        Integer.parseInt(timesF.getText()),
                        new java.sql.Date(System.currentTimeMillis()),
                        Integer.parseInt(durationF.getText()),
                        scheduleF.getText());

                boolean success = isEdit ? medicineController.updateMedicine(m) : medicineController.addMedicine(m);
                if (success) {
                    loadMedicines();
                    JOptionPane.showMessageDialog(this, "Success!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private void deleteMedicine() {
        int row = medicineTable.getSelectedRow();
        if (row == -1)
            return;

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this medicine?");
        if (confirm == JOptionPane.YES_OPTION) {
            Medicine med = medicineList.get(row);
            if (medicineController.deleteMedicine(med.getId())) {
                loadMedicines();
            }
        }
    }

    private void showTestReminder() {
        com.mycompany.project.controller.AlertController alertController = new com.mycompany.project.controller.AlertController();
        String msg = alertController.getHealthTip();
        JOptionPane.showMessageDialog(this, "REMINDER: " + msg, "Test Notification",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
