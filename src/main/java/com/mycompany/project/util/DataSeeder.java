package com.mycompany.project.util;

import com.mycompany.project.database.DatabaseHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class DataSeeder {

    public static void seedData() {
        System.out.println("Starting Data Seeding...");
        try (Connection conn = DatabaseHelper.connect()) {
            // 1. Check/Create User
            int userId = getOrCreateUser(conn, "moreda", "123456");
            if (userId == -1) {
                System.err.println("Failed to get or create user.");
                return;
            }

            // 2. Clear old measurements for this user to avoid duplicates if run multiple
            // times
            // clearMeasurements(conn, userId); // Optional: keep or remove based on
            // preference. Let's keep data if exists? No, user wants fresh report.
            // Actually, let's just add new data. If we run it twice, we get double data.
            // Let's clear for 'moreda' to be clean.
            clearMeasurements(conn, userId);

            // 3. Insert 3 Weeks of Data
            insertMockData(conn, userId);

            System.out.println("Data Seeding Completed Successfully for user 'moreda'.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getOrCreateUser(Connection conn, String username, String password) throws SQLException {
        // Check exist
        String checkSql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // Create
        String insertSql = "INSERT INTO users (username, password, role, full_name, gender, age) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, "PATIENT");
            pstmt.setString(4, "Moreda Patient");
            pstmt.setString(5, "Male");
            pstmt.setInt(6, 30);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    private static void clearMeasurements(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM measurements WHERE patient_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("Cleared old measurements.");
        }
    }

    private static void insertMockData(Connection conn, int userId) throws SQLException {
        String insertSql = "INSERT INTO measurements (patient_id, type, value, record_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            Random rand = new Random();
            LocalDate today = LocalDate.now();

            // Generate for last 21 days
            for (int i = 0; i < 22; i++) {
                LocalDate date = today.minusDays(i);
                java.sql.Date sqlDate = java.sql.Date.valueOf(date);

                // 1. Blood Pressure (Random fluctuations but mostly normal)
                // Systolic: 110-130, Diastolic: 70-85
                int sys = 110 + rand.nextInt(21);
                int dia = 70 + rand.nextInt(16);
                addBatch(pstmt, userId, "BLOOD_PRESSURE", sys + "/" + dia, sqlDate);

                // 2. Sugar (Fasting)
                // 80 - 110
                int sugar = 80 + rand.nextInt(31);
                addBatch(pstmt, userId, "SUGAR", String.valueOf(sugar), sqlDate);

                // 3. Heart Rate
                // 65 - 90
                int heart = 65 + rand.nextInt(26);
                addBatch(pstmt, userId, "HEART_RATE", String.valueOf(heart), sqlDate);

                // 4. Oxygen
                // 96 - 100
                int oxy = 96 + rand.nextInt(5);
                addBatch(pstmt, userId, "OXYGEN", String.valueOf(oxy), sqlDate);

                // 5. Anemia (Hemoglobin) - rarely changes but let's add some entries (maybe
                // every 3 days)
                if (i % 3 == 0) {
                    double hemo = 13.5 + (rand.nextDouble() * 2); // 13.5 - 15.5
                    addBatch(pstmt, userId, "ANEMIA", String.format("%.1f", hemo), sqlDate);
                }
            }
            pstmt.executeBatch();
            System.out.println("Inserted 21 days of mock data.");
        }
    }

    private static void addBatch(PreparedStatement pstmt, int userId, String type, String value, java.sql.Date date)
            throws SQLException {
        pstmt.setInt(1, userId);
        pstmt.setString(2, type);
        pstmt.setString(3, value);
        pstmt.setDate(4, date);
        pstmt.addBatch();
    }
}
