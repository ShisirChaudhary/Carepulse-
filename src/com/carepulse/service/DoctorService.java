package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Service class for the doctors table.
public class DoctorService {

    // Base SELECT statement that joins the specializations lookup table.
    private static final String SELECT_DOCTOR =
            "SELECT d.*, s.name AS specialization_name FROM doctors d " +
            "JOIN specializations s ON d.specialization_id = s.id";

    // Returns every doctor ordered by creation date (newest first).
    public List<Doctor> getAll() throws Exception {
        return getAll(null, null);
    }

    // Returns every doctor with the specified sort key and direction.
    // Allowed sort keys: name, specialization, status.
    public List<Doctor> getAll(String sortKey, String sortDir) throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        String sql = SELECT_DOCTOR + " " + buildOrderBy(sortKey, sortDir);
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    // Builds an ORDER BY clause from a fixed list of allowed sort keys.
    private static String buildOrderBy(String sortKey, String sortDir) {
        String dir = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
        if (sortKey == null) return "ORDER BY d.created_at DESC";
        switch (sortKey) {
            case "name":           return "ORDER BY d.full_name " + dir;
            case "specialization": return "ORDER BY s.name " + dir + ", d.full_name ASC";
            case "status":         return "ORDER BY d.available " + dir + ", d.full_name ASC";
            default:               return "ORDER BY d.created_at DESC";
        }
    }

    // Returns only available doctors ordered alphabetically.
    public List<Doctor> getAvailable() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        String sql = SELECT_DOCTOR + " WHERE d.available = 1 ORDER BY d.full_name ASC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    // Searches available doctors by name or specialization.
    public List<Doctor> searchAvailable(String keyword) throws Exception {
        return searchAvailable(keyword, null, null);
    }

    // Searches available doctors by name or specialization with an optional sort.
    public List<Doctor> searchAvailable(String keyword, String sortKey, String sortDir) throws Exception {
        String orderBy;
        String dir = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
        if (sortKey == null) {
            orderBy = "ORDER BY d.full_name ASC";
        } else {
            switch (sortKey) {
                case "name":           orderBy = "ORDER BY d.full_name " + dir; break;
                case "specialization": orderBy = "ORDER BY s.name " + dir + ", d.full_name ASC"; break;
                default:               orderBy = "ORDER BY d.full_name ASC";
            }
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            List<Doctor> doctors = new ArrayList<>();
            String sql = SELECT_DOCTOR + " WHERE d.available = 1 " + orderBy;
            try (Connection conn = DBConfig.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) doctors.add(mapDoctor(rs));
            }
            return doctors;
        }

        List<Doctor> doctors = new ArrayList<>();
        String sql = SELECT_DOCTOR +
                " WHERE d.available = 1 AND (d.full_name LIKE ? OR s.name LIKE ?) " + orderBy;
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword.trim() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    // Returns the most recently added available doctors up to the supplied limit.
    public List<Doctor> getRecent(int limit) throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        String sql = SELECT_DOCTOR +
                " WHERE d.available = 1 ORDER BY d.created_at DESC LIMIT ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    // Returns a single doctor by id, or null if not found.
    public Doctor getById(int id) throws Exception {
        String sql = SELECT_DOCTOR + " WHERE d.id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapDoctor(rs);
            }
            return null;
        }
    }

    // Inserts a new doctor.
    public void add(Doctor doctor) throws Exception {
        String sql = "INSERT INTO doctors (full_name, specialization_id, contact, email, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setInt(2, doctor.getSpecializationId());
            ps.setString(3, doctor.getContact());
            ps.setString(4, doctor.getEmail());
            ps.setInt(5, doctor.isAvailable() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    // Updates an existing doctor.
    public void update(Doctor doctor) throws Exception {
        String sql = "UPDATE doctors SET full_name = ?, specialization_id = ?, contact = ?, email = ?, available = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setInt(2, doctor.getSpecializationId());
            ps.setString(3, doctor.getContact());
            ps.setString(4, doctor.getEmail());
            ps.setInt(5, doctor.isAvailable() ? 1 : 0);
            ps.setInt(6, doctor.getId());
            ps.executeUpdate();
        }
    }

    // Deletes a doctor by id.
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Maps the current row of the result set into a Doctor object.
    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setSpecializationId(rs.getInt("specialization_id"));
        doctor.setSpecialization(rs.getString("specialization_name"));
        doctor.setContact(rs.getString("contact"));
        doctor.setEmail(rs.getString("email"));
        doctor.setAvailable(rs.getInt("available") == 1);
        return doctor;
    }
}
