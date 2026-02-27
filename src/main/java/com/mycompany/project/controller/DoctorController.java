// كونترولر إدارة الأطباء
package com.mycompany.project.controller;

import com.mycompany.project.database.DatabaseHelper;
import com.mycompany.project.model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// مسؤول عن العمليات على جدول الأطباء (CRUD + ربط مع المرضى)
public class DoctorController {

    // إضافة طبيب جديد
    public boolean addDoctor(Doctor doctor) {
        String query = "INSERT INTO doctors (name, phone, clinic_address, schedule, appointment_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, doctor.getName());
            pstmt.setString(2, doctor.getPhone());
            pstmt.setString(3, doctor.getClinicAddress());
            pstmt.setString(4, doctor.getSchedule());
            pstmt.setString(5, doctor.getAppointmentTime());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";
        try (Connection conn = DatabaseHelper.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("clinic_address"),
                        rs.getString("schedule"),
                        rs.getString("appointment_time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public boolean linkPatientToDoctor(int patientId, int doctorId) {
        String query = "INSERT INTO patient_doctor (patient_id, doctor_id) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            pstmt.setInt(2, doctorId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Likely duplicate entry
            return false;
        }
    }

    public List<Doctor> getDoctorsForPatient(int patientId) {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT d.* FROM doctors d JOIN patient_doctor pd ON d.id = pd.doctor_id WHERE pd.patient_id = ?";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("clinic_address"),
                        rs.getString("schedule"),
                        rs.getString("appointment_time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public boolean updateDoctor(Doctor doctor) {
        String query = "UPDATE doctors SET name = ?, phone = ?, clinic_address = ?, schedule = ?, appointment_time = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, doctor.getName());
            pstmt.setString(2, doctor.getPhone());
            pstmt.setString(3, doctor.getClinicAddress());
            pstmt.setString(4, doctor.getSchedule());
            pstmt.setString(5, doctor.getAppointmentTime());
            pstmt.setInt(6, doctor.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDoctor(int doctorId) {
        // First delete from linking table
        String deleteLinkQuery = "DELETE FROM patient_doctor WHERE doctor_id = ?";
        String deleteDocQuery = "DELETE FROM doctors WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect()) {
            conn.setAutoCommit(false); // Transaction

            try (PreparedStatement linkStmt = conn.prepareStatement(deleteLinkQuery);
                    PreparedStatement docStmt = conn.prepareStatement(deleteDocQuery)) {

                linkStmt.setInt(1, doctorId);
                linkStmt.executeUpdate();

                docStmt.setInt(1, doctorId);
                int rows = docStmt.executeUpdate();

                conn.commit();
                return rows > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
