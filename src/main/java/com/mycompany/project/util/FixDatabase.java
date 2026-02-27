package com.mycompany.project.util;

import com.mycompany.project.database.DatabaseHelper;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility to fix database schema
 */
public class FixDatabase {
    public static void main(String[] args) {
        try (Connection conn = DatabaseHelper.connect();
                Statement stmt = conn.createStatement()) {

            System.out.println("Fixing database schema...");

            // Add schedule_times column
            try {
                stmt.executeUpdate("ALTER TABLE medicines ADD COLUMN schedule_times VARCHAR(255)");
                System.out.println("✓ Added schedule_times column to medicines table");
            } catch (Exception e) {
                System.out.println("schedule_times column already exists or error: " + e.getMessage());
            }

            // Add appointment_time column
            try {
                stmt.executeUpdate("ALTER TABLE doctors ADD COLUMN appointment_time VARCHAR(10)");
                System.out.println("✓ Added appointment_time column to doctors table");
            } catch (Exception e) {
                System.out.println("appointment_time column already exists or error: " + e.getMessage());
            }

            System.out.println("\n✅ Database schema fixed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error fixing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
