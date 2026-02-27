package com.mycompany.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
        private static final String URL = "jdbc:mysql://localhost:3306/";
        private static final String DB_NAME = "patient_db";
        private static final String USER = "root";
        private static final String PASSWORD = "123456"; // As provided by user

        public static Connection connect() throws SQLException {
                return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
        }

        public static void setupDatabase() {
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                                Statement stmt = conn.createStatement()) {

                        // Create Database if not exists
                        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                        stmt.executeUpdate("USE " + DB_NAME);

                        // Create Users Table
                        String createUsers = "CREATE TABLE IF NOT EXISTS users ("
                                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                        + "username VARCHAR(50) UNIQUE NOT NULL, "
                                        + "password VARCHAR(50) NOT NULL, "
                                        + "role VARCHAR(20) NOT NULL, " // PATIENT, DOCTOR
                                        + "full_name VARCHAR(100), "
                                        + "gender VARCHAR(10), "
                                        + "age INT"
                                        + ")";
                        stmt.executeUpdate(createUsers);

                        // Create Doctor Details Table
                        String createDoctors = "CREATE TABLE IF NOT EXISTS doctors ("
                                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                        + "name VARCHAR(100) NOT NULL, "
                                        + "phone VARCHAR(20), "
                                        + "clinic_address VARCHAR(255), "
                                        + "schedule VARCHAR(255), "
                                        + "appointment_time VARCHAR(10)" // New
                                        + ")";
                        stmt.executeUpdate(createDoctors);

                        // Add appointment_time column if not exists
                        try {
                                stmt.executeUpdate(
                                                "ALTER TABLE doctors ADD COLUMN IF NOT EXISTS appointment_time VARCHAR(10)");
                        } catch (SQLException e) {
                                // Column already exists
                        }

                        // Create Patient_Doctor Link Table
                        String createPatientDoctor = "CREATE TABLE IF NOT EXISTS patient_doctor ("
                                        + "patient_id INT, "
                                        + "doctor_id INT, "
                                        + "FOREIGN KEY (patient_id) REFERENCES users(id), "
                                        + "FOREIGN KEY (doctor_id) REFERENCES doctors(id), "
                                        + "PRIMARY KEY (patient_id, doctor_id)"
                                        + ")";
                        stmt.executeUpdate(createPatientDoctor);

                        // Create Medicine Table
                        String createMedicines = "CREATE TABLE IF NOT EXISTS medicines ("
                                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                        + "patient_id INT, "
                                        + "name VARCHAR(100) NOT NULL, "
                                        + "dosage VARCHAR(50), "
                                        + "times_per_day INT, "
                                        + "start_date DATE, "
                                        + "duration_days INT, "
                                        + "schedule_times VARCHAR(255), " // New: e.g., "9:00,15:00,21:00"
                                        + "FOREIGN KEY (patient_id) REFERENCES users(id)"
                                        + ")";
                        stmt.executeUpdate(createMedicines);

                        // Add schedule_times column to existing tables if not exists
                        try {
                                stmt.executeUpdate(
                                                "ALTER TABLE medicines ADD COLUMN IF NOT EXISTS schedule_times VARCHAR(255)");
                        } catch (SQLException e) {
                                // Column already exists, ignore
                        }

                        // Create Measurements Table (for Reports)
                        String createMeasurements = "CREATE TABLE IF NOT EXISTS measurements ("
                                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                        + "patient_id INT, "
                                        + "type VARCHAR(50), " // BLOOD_PRESSURE, SUGAR, etc.
                                        + "value VARCHAR(50), " // Stored as string to handle "120/80"
                                        + "record_date DATE, "
                                        + "FOREIGN KEY (patient_id) REFERENCES users(id)"
                                        + ")";
                        stmt.executeUpdate(createMeasurements);

                        // Create Medical History (Diseases)
                        String createDiseases = "CREATE TABLE IF NOT EXISTS diseases ("
                                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                                        + "patient_id INT, "
                                        + "disease_name VARCHAR(100), "
                                        + "FOREIGN KEY (patient_id) REFERENCES users(id)"
                                        + ")";
                        stmt.executeUpdate(createDiseases);

                        System.out.println("Database setup completed successfully.");

                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }
}
