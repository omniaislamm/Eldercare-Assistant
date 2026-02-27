// واجهة إدارة الأطباء
package com.mycompany.project.view.panels;

import com.mycompany.project.controller.DoctorController;
import com.mycompany.project.model.Doctor;
import com.mycompany.project.model.User;
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

// بانل إدارة الأطباء
public class DoctorPanel extends JPanel {
    private DoctorController doctorController;
    private User currentUser;
    private JTable doctorTable;
    private DefaultTableModel tableModel;

    public DoctorPanel(User user) {
        this.currentUser = user;
        this.doctorController = new DoctorController();
        setLayout(new BorderLayout());
        Theme.stylePanel(this);
        initComponents();
        loadDoctors();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Theme.BACKGROUND_COLOR);
        JLabel title = new JLabel("My Doctors");
        Theme.styleHeaderLabel(title);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        // Center
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Theme.BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Theme.BACKGROUND_COLOR);

        JButton addBtn = new JButton("Add Doctor");
        Theme.styleButton(addBtn);
        addBtn.addActionListener(e -> showDoctorDialog(null));
        actionPanel.add(addBtn);

        JButton editBtn = new JButton("Edit");
        Theme.styleButton(editBtn);
        editBtn.setBackground(Theme.ACCENT_COLOR);
        editBtn.addActionListener(e -> {
            int row = doctorTable.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                Doctor doc = findDoctorById(id);
                showDoctorDialog(doc);
            }
        });
        actionPanel.add(editBtn);

        JButton deleteBtn = new JButton("Delete");
        Theme.styleButton(deleteBtn);
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteDoctor());
        actionPanel.add(deleteBtn);

        centerPanel.add(actionPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Name", "Phone", "Clinic", "Schedule", "Time" };
        tableModel = new DefaultTableModel(columns, 0);
        doctorTable = new JTable(tableModel);
        doctorTable.setFont(Theme.REGULAR_FONT);
        doctorTable.setRowHeight(30);

        JTableHeader header = doctorTable.getTableHeader();
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setForeground(Theme.WHITE);
        header.setFont(Theme.BOLD_FONT);

        centerPanel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadDoctors() {
        tableModel.setRowCount(0);
        List<Doctor> list = doctorController.getAllDoctors();
        for (Doctor d : list) {
            tableModel.addRow(new Object[] {
                    d.getId(), d.getName(), d.getPhone(),
                    d.getClinicAddress(), d.getSchedule(),
                    d.getAppointmentTime() != null ? d.getAppointmentTime() : ""
            });
        }
    }

    // دالة واحدة للإضافة والتعديل
    private void showDoctorDialog(Doctor doctor) {
        boolean isEdit = (doctor != null);

        JTextField nameF = new JTextField(isEdit ? doctor.getName() : "");
        JTextField phoneF = new JTextField(isEdit ? doctor.getPhone() : "");
        JTextField addressF = new JTextField(isEdit ? doctor.getClinicAddress() : "");
        JTextField scheduleF = new JTextField(isEdit ? doctor.getSchedule() : "");
        JTextField timeF = new JTextField(isEdit ? doctor.getAppointmentTime() : "");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameF);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneF);
        panel.add(new JLabel("Clinic:"));
        panel.add(addressF);
        panel.add(new JLabel("Schedule:"));
        panel.add(scheduleF);
        panel.add(new JLabel("Time (e.g., 10:00):"));
        panel.add(timeF);

        int result = JOptionPane.showConfirmDialog(this, panel,
                isEdit ? "Edit Doctor" : "Add Doctor", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && !nameF.getText().isEmpty()) {
            Doctor d = new Doctor(
                    isEdit ? doctor.getId() : 0,
                    nameF.getText(), phoneF.getText(),
                    addressF.getText(), scheduleF.getText(), timeF.getText());

            boolean success = isEdit ? doctorController.updateDoctor(d) : doctorController.addDoctor(d);
            if (success) {
                loadDoctors();
                JOptionPane.showMessageDialog(this, "Success!");
            }
        }
    }

    private void deleteDoctor() {
        int row = doctorTable.getSelectedRow();
        if (row == -1)
            return;

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this doctor?");
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (doctorController.deleteDoctor(id)) {
                loadDoctors();
            }
        }
    }

    private Doctor findDoctorById(int id) {
        for (Doctor d : doctorController.getAllDoctors()) {
            if (d.getId() == id)
                return d;
        }
        return null;
    }
}
