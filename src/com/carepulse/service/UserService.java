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

    // SELECT prefix that joins the roles lookup so callers always get the role name
    private static final String SELECT_USER =
            "SELECT u.*, r.name AS role_name FROM users u " +
            "JOIN roles r ON u.role_id = r.id";

    // INSERT statement that resolves the role name to role_id via a subquery
    private static final String INSERT_USER_AS_ROLE =
            "INSERT INTO users (full_name, email, password, phone, role_id) " +
            "VALUES (?, ?, ?, ?, (SELECT id FROM roles WHERE name = ?))";

    // New patient registration with encrypted password
    public void register(User user) throws Exception {
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_AS_ROLE)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, AESUtil.encrypt(user.getPassword()));
            ps.setString(4, user.getPhone());
            ps.setString(5, "patient");
            ps.executeUpdate();
        }
    }

    // Login logic - checks email, password, and locks account after 5 tries
    public User login(String email, String rawPassword) throws Exception {
        String sql = SELECT_USER + " WHERE u.email = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            User user = mapUser(rs);

            if (user.isLocked()) {
                throw new CarePulseException("Account is locked due to too many failed login attempts. Contact admin.");
            }

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

            resetFailedAttempts(user.getId());
            return user;
        }
    }

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

    public User getById(int id) throws Exception {
        String sql = SELECT_USER + " WHERE u.id = ?";
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

    public List<User> getAllPatients() throws Exception {
        return getAllPatients(null, null);
    }

    // Sortable variant. Allowed sortKey values: name, email, phone, status, created.
    public List<User> getAllPatients(String sortKey, String sortDir) throws Exception {
        List<User> patients = new ArrayList<>();
        String sql = SELECT_USER + " WHERE r.name = 'patient' " + buildPatientOrderBy(sortKey, sortDir);
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                patients.add(mapUser(rs));
            }
        }
        return patients;
    }

    private static String buildPatientOrderBy(String sortKey, String sortDir) {
        String dir = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
        if (sortKey == null) return "ORDER BY u.created_at DESC";
        switch (sortKey) {
            case "name":   return "ORDER BY u.full_name " + dir;
            case "email":  return "ORDER BY u.email " + dir;
            case "phone":  return "ORDER BY u.phone " + dir;
            case "status": return "ORDER BY u.is_locked " + dir + ", u.full_name ASC";
            case "created":return "ORDER BY u.created_at " + dir;
            default:       return "ORDER BY u.created_at DESC";
        }
    }

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

    public void updateByAdmin(int id, String fullName, String email, String phone) throws Exception {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void addByAdmin(User user) throws Exception {
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_AS_ROLE)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, AESUtil.encrypt(user.getPassword()));
            ps.setString(4, user.getPhone());
            ps.setString(5, "patient");
            ps.executeUpdate();
        }
    }

    public boolean emailExistsForOtherUser(String email, int excludeId) throws Exception {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public boolean phoneExistsForOtherUser(String phone, int excludeId) throws Exception {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ? AND id <> ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public void updatePassword(int id, String newPassword) throws Exception {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, AESUtil.encrypt(newPassword));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteUser(int id) throws Exception {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void unlockUser(int id) throws Exception {
        String sql = "UPDATE users SET is_locked = 0, failed_attempts = 0 WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

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

    // Reads role from the joined roles table (alias "role_name")
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRole(rs.getString("role_name"));
        user.setFailedAttempts(rs.getInt("failed_attempts"));
        user.setLocked(rs.getInt("is_locked") == 1);
        user.setResetToken(rs.getString("reset_token"));
        return user;
    }
}
