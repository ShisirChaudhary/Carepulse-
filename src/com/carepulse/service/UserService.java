package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.User;
import com.carepulse.util.AESUtil;
import com.carepulse.util.CarePulseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Handles user logic like login, registration, and auth
public class UserService {

    // New patient registration with encrypted password
    public void register(User user) throws Exception {
        String sql = "INSERT INTO users (full_name, email, password, phone, role) VALUES (?, ?, ?, ?, 'patient')";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, AESUtil.encrypt(user.getPassword()));
            ps.setString(4, user.getPhone());
            ps.executeUpdate();
        }
    }

    // Login logic - checks email, password, and locks account after 5 tries
    public User login(String email, String rawPassword) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            User user = mapUser(rs);

            // Check lock status
            if (user.isLocked()) {
                throw new CarePulseException("Account is locked due to too many failed login attempts. Contact admin.");
            }

            // Decrypt stored password and compare
            String decryptedPassword = AESUtil.decrypt(user.getPassword());
            if (!decryptedPassword.equals(rawPassword)) {
                incrementFailedAttempts(user.getId());
                int newAttempts = user.getFailedAttempts() + 1;
                if (newAttempts >= 5) {
                    lockUser(user.getId());
                    throw new CarePulseException("Account locked after 5 failed attempts. Contact admin.");
                }
                return null;
            }

            // Successful login — reset failed attempts
            resetFailedAttempts(user.getId());
            return user;
        }
    }

    /**
     * Checks if email already exists in the database.
     */
    public boolean emailExists(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    /**
     * Checks if phone number already exists in the database.
     */
    public boolean phoneExists(String phone) throws Exception {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    /**
     * Retrieves a user by their ID.
     */
    public User getById(int id) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
            return null;
        }
    }

    /**
     * Retrieves all users with the 'patient' role.
     */
    public List<User> getAllPatients() throws Exception {
        List<User> patients = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'patient' ORDER BY created_at DESC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                patients.add(mapUser(rs));
            }
        }
        return patients;
    }

    // Updates basic profile info
    public void updateProfile(int id, String fullName, String phone) throws Exception {
        String sql = "UPDATE users SET full_name = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    // Sets a new password (encrypts it first)
    public void updatePassword(int id, String newPassword) throws Exception {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, AESUtil.encrypt(newPassword));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a user by ID.
     */
    public void deleteUser(int id) throws Exception {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Unlocks a user account and resets failed attempts.
     */
    public void unlockUser(int id) throws Exception {
        String sql = "UPDATE users SET is_locked = 0, failed_attempts = 0 WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Generates an 8-character uppercase reset token and stores it in the DB.
     * Returns the token.
     */
    public String generateResetToken(String email) throws Exception {
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        String sql = "UPDATE users SET reset_token = ? WHERE email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new CarePulseException("Email not found.");
            }
        }
        return token;
    }

    /**
     * Resets password using a token. Verifies token matches, then updates password and clears token.
     */
    public void resetPassword(String email, String token, String newPassword) throws Exception {
        String sql = "SELECT reset_token FROM users WHERE email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new CarePulseException("Email not found.");
            }
            String storedToken = rs.getString("reset_token");
            if (storedToken == null || !storedToken.equals(token)) {
                throw new CarePulseException("Invalid reset token.");
            }
        }

        String updateSql = "UPDATE users SET password = ?, reset_token = NULL, is_locked = 0, failed_attempts = 0 WHERE email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, AESUtil.encrypt(newPassword));
            ps.setString(2, email);
            ps.executeUpdate();
        }
    }

    // Helpers

    private void incrementFailedAttempts(int userId) throws Exception {
        String sql = "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private void lockUser(int userId) throws Exception {
        String sql = "UPDATE users SET is_locked = 1 WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private void resetFailedAttempts(int userId) throws Exception {
        String sql = "UPDATE users SET failed_attempts = 0 WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setFailedAttempts(rs.getInt("failed_attempts"));
        user.setLocked(rs.getInt("is_locked") == 1);
        user.setResetToken(rs.getString("reset_token"));
        return user;
    }
}
