package com.mycompany.project.controller;

import com.mycompany.project.database.DatabaseHelper;
import com.mycompany.project.model.Measurement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportController {

    public boolean addMeasurement(Measurement measure) {
        String query = "INSERT INTO measurements (patient_id, type, value, record_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, measure.getPatientId());
            pstmt.setString(2, measure.getType());
            pstmt.setString(3, measure.getValue());
            pstmt.setDate(4, measure.getRecordDate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Measurement> getMeasurements(int patientId, String type) {
        List<Measurement> list = new ArrayList<>();
        String query = "SELECT * FROM measurements WHERE patient_id = ? AND type = ? ORDER BY record_date DESC";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            pstmt.setString(2, type);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new Measurement(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("type"),
                        rs.getString("value"),
                        rs.getDate("record_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // New method to get ALL history for the simplified table
    public List<Measurement> getAllMeasurements(int patientId) {
        List<Measurement> list = new ArrayList<>();
        String query = "SELECT * FROM measurements WHERE patient_id = ? ORDER BY record_date DESC";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new Measurement(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("type"),
                        rs.getString("value"),
                        rs.getDate("record_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // New: Calculate Weekly Averages for Dashboard
    public java.util.Map<String, String> getWeeklyStats(int patientId) {
        java.util.Map<String, String> stats = new java.util.HashMap<>();
        String query = "SELECT type, value FROM measurements WHERE patient_id = ? AND record_date >= ?";

        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Last 7 days
            long sevenDaysAgo = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);
            pstmt.setInt(1, patientId);
            pstmt.setDate(2, new java.sql.Date(sevenDaysAgo));

            ResultSet rs = pstmt.executeQuery();

            // Group by type
            java.util.Map<String, List<Double>> grouped = new java.util.HashMap<>();
            while (rs.next()) {
                String type = rs.getString("type");
                String val = rs.getString("value");

                // Parse value (handle "120/80")
                try {
                    if (val.contains("/"))
                        val = val.split("/")[0];
                    double v = Double.parseDouble(val);
                    grouped.computeIfAbsent(type, k -> new ArrayList<>()).add(v);
                } catch (Exception e) {
                    /* ignore */ }
            }

            // Calculate Averages and Status
            for (String type : grouped.keySet()) {
                List<Double> values = grouped.get(type);
                double avg = values.stream().mapToDouble(d -> d).average().orElse(0);
                stats.put(type, String.format("%.0f", avg)); // Simple integer-like string
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
