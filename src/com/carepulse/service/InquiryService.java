package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Inquiry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Persists and retrieves contact-form inquiries
public class InquiryService {

    // Saves a new inquiry submitted from the public contact form
    public void submit(Inquiry inquiry) throws Exception {
        String sql = "INSERT INTO contact_inquiries (full_name, email, subject, message) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inquiry.getFullName());
            ps.setString(2, inquiry.getEmail());
            ps.setString(3, inquiry.getSubject());
            ps.setString(4, inquiry.getMessage());
            ps.executeUpdate();
        }
    }

    public List<Inquiry> getAll() throws Exception {
        return getAll(null, null);
    }

    // Sortable variant. Allowed: name, email, subject, received, status.
    public List<Inquiry> getAll(String sortKey, String sortDir) throws Exception {
        List<Inquiry> list = new ArrayList<>();
        String sql = "SELECT * FROM contact_inquiries " + buildInquiryOrderBy(sortKey, sortDir);
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapInquiry(rs));
            }
        }
        return list;
    }

    private static String buildInquiryOrderBy(String sortKey, String sortDir) {
        String dir = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
        if (sortKey == null) return "ORDER BY created_at DESC";
        switch (sortKey) {
            case "name":     return "ORDER BY full_name " + dir;
            case "email":    return "ORDER BY email " + dir;
            case "subject":  return "ORDER BY subject " + dir;
            case "received": return "ORDER BY created_at " + dir;
            case "status":   return "ORDER BY status " + dir + ", created_at DESC";
            default:         return "ORDER BY created_at DESC";
        }
    }

    // Marks an inquiry as resolved so it no longer shows as new
    public void markResolved(int id) throws Exception {
        String sql = "UPDATE contact_inquiries SET status = 'resolved' WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Permanently removes an inquiry record
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM contact_inquiries WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Counts open (unresolved) inquiries for the admin dashboard badge
    public int countOpen() throws Exception {
        String sql = "SELECT COUNT(*) FROM contact_inquiries WHERE status = 'open'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    private Inquiry mapInquiry(ResultSet rs) throws SQLException {
        Inquiry inquiry = new Inquiry();
        inquiry.setId(rs.getInt("id"));
        inquiry.setFullName(rs.getString("full_name"));
        inquiry.setEmail(rs.getString("email"));
        inquiry.setSubject(rs.getString("subject"));
        inquiry.setMessage(rs.getString("message"));
        inquiry.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        inquiry.setCreatedAt(ts != null ? ts.toString() : "");
        return inquiry;
    }
}
