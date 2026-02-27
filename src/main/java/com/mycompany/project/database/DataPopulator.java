package com.mycompany.project.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;

public class DataPopulator {

    public static void main(String[] args) {
        populateData();
    }

    public static void populateData() {
        try (Connection conn = DatabaseHelper.connect()) {
            System.out.println("Starting data population...");

            // 1. Create or Get User 'moreda'
            int userId = getOrCreateUser(conn, "moreda", "123", "PATIENT", "Moreda User", "Male", 30);
            System.out.println("User 'moreda' ID: " + userId);

            // 2. Add Medicines
            addMedicines(conn, userId);

            // 3. Add Doctors
            addDoctors(conn, userId);

            // 4. Add Reports
            addReports(conn, userId);

            System.out.println("Data population completed successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getOrCreateUser(Connection conn, String username, String password, String role, String fullName,
            String gender, int age) throws SQLException {
        // Check if exists
        String checkSql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // Create if not exists
        String insertSql = "INSERT INTO users (username, password, role, full_name, gender, age) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
            pstmt.setString(5, gender);
            pstmt.setInt(6, age);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to create user");
    }

    private static void addMedicines(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO medicines (patient_id, name, dosage, times_per_day, start_date, duration_days) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Medicine 1
            pstmt.setInt(1, userId);
            pstmt.setString(2, "Panadol Extra");
            pstmt.setString(3, "500mg");
            pstmt.setInt(4, 3);
            pstmt.setDate(5, java.sql.Date.valueOf(LocalDate.now().minusDays(2)));
            pstmt.setInt(6, 7);
            pstmt.executeUpdate();

            // Medicine 2
            pstmt.setInt(1, userId);
            pstmt.setString(2, "Vitamin C");
            pstmt.setString(3, "1000mg");
            pstmt.setInt(4, 1);
            pstmt.setDate(5, java.sql.Date.valueOf(LocalDate.now().minusDays(10)));
            pstmt.setInt(6, 30);
            pstmt.executeUpdate();

            // Medicine 3 (Alert Test)
            pstmt.setInt(1, userId);
            pstmt.setString(2, "TEST-Antibiotic");
            pstmt.setString(3, "250mg");
            pstmt.setInt(4, 2);
            pstmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setInt(6, 5);
            pstmt.executeUpdate();

            System.out.println("Medicines added.");
        }
    }

    private static void addDoctors(Connection conn, int userId) throws SQLException {
        // Insert Doctors
        String insertDocVal = "INSERT INTO doctors (name, phone, clinic_address, schedule) VALUES (?, ?, ?, ?)";
        int docId1 = 0, docId2 = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(insertDocVal, Statement.RETURN_GENERATED_KEYS)) {
            // Doc 1
            pstmt.setString(1, "Dr. Ahmed Ali");
            pstmt.setString(2, "01012345678");
            pstmt.setString(3, "123 Main St, Cairo");
            pstmt.setString(4, "Sun-Thu 10am-5pm");
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
                docId1 = rs.getInt(1);

            // Doc 2
            pstmt.setString(1, "Dr. Sara Smith");
            pstmt.setString(2, "01187654321");
            pstmt.setString(3, "456 Side St, Giza");
            pstmt.setString(4, "Mon-Wed 12pm-6pm");
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next())
                docId2 = rs.getInt(1);
        }

        // Link to Patient
        String linkSql = "INSERT INTO patient_doctor (patient_id, doctor_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(linkSql)) {
            if (docId1 > 0) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, docId1);
                pstmt.executeUpdate();
            }
            if (docId2 > 0) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, docId2);
                pstmt.executeUpdate();
            }
            System.out.println("Doctors added and linked.");
        }
    }

    private static void addReports(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO measurements (patient_id, type, value, record_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Random rand = new Random();
            for (int i = 0; i < 5; i++) {
                // BP
                pstmt.setInt(1, userId);
                pstmt.setString(2, "BLOOD_PRESSURE");
                int sys = 110 + rand.nextInt(30);
                int dia = 70 + rand.nextInt(20);
                pstmt.setString(3, sys + "/" + dia);
                pstmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().minusDays(i)));
                pstmt.executeUpdate();

                // Sugar
                pstmt.setInt(1, userId);
                pstmt.setString(2, "SUGAR");
                int sugar = 80 + rand.nextInt(60);
                pstmt.setString(3, String.valueOf(sugar));
                pstmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().minusDays(i)));
                pstmt.executeUpdate();
            }
            System.out.println("Reports added.");
        }
    }
}
