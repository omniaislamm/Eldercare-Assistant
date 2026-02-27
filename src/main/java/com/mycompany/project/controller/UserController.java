// كونترولر إدارة المستخدمين (تسجيل الدخول والتسجيل)
package com.mycompany.project.controller;

import com.mycompany.project.database.DatabaseHelper;
import com.mycompany.project.model.User;
import java.sql.*;

// مسؤول عن عمليات المستخدمين: تسجيل دخول، تسجيل حساب جديد
public class UserController {

    // دالة تسجيل الدخول
    public User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("full_name"),
                        rs.getString("gender"),
                        rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // دالة تسجيل حساب جديد
    public boolean register(User user, String diseases) {
        String query = "INSERT INTO users (username, password, role, full_name, gender, age) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getGender());
            pstmt.setInt(6, user.getAge());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    if (diseases != null && !diseases.isEmpty()) {
                        addDiseases(userId, diseases);
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addDiseases(int userId, String diseases) {
        // diseases is a comma separated string
        String[] diseaseList = diseases.split(",");
        String query = "INSERT INTO diseases (patient_id, disease_name) VALUES (?, ?)";

        try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            for (String d : diseaseList) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, d.trim());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
