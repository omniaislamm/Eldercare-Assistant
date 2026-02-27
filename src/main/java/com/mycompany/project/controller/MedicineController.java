// كونترولر إدارة الأدوية
package com.mycompany.project.controller;

import com.mycompany.project.database.DatabaseHelper;
import com.mycompany.project.model.Medicine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// مسؤول عن العمليات على جدول الأدوية (CRUD)
public class MedicineController {

    // إضافة دواء جديد
    public boolean addMedicine(Medicine med) {
        String query = "INSERT INTO medicines (patient_id, name, dosage, times_per_day, start_date, duration_days, schedule_times) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, med.getPatientId());
            pstmt.setString(2, med.getName());
            pstmt.setString(3, med.getDosage());
            pstmt.setInt(4, med.getTimesPerDay());
            pstmt.setDate(5, med.getStartDate());
            pstmt.setInt(6, med.getDurationDays());
            pstmt.setString(7, med.getScheduleTimes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMedicine(Medicine med) {
        String query = "UPDATE medicines SET name = ?, dosage = ?, times_per_day = ?, duration_days = ?, schedule_times = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, med.getName());
            pstmt.setString(2, med.getDosage());
            pstmt.setInt(3, med.getTimesPerDay());
            pstmt.setInt(4, med.getDurationDays());
            pstmt.setString(5, med.getScheduleTimes());
            pstmt.setInt(6, med.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMedicine(int id) {
        String query = "DELETE FROM medicines WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Medicine> getMedicinesForPatient(int patientId) {
        List<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM medicines WHERE patient_id = ?";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                medicines.add(new Medicine(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("name"),
                        rs.getString("dosage"),
                        rs.getInt("times_per_day"),
                        rs.getDate("start_date"),
                        rs.getInt("duration_days"),
                        rs.getString("schedule_times")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }
}
