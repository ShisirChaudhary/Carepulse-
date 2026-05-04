package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Service layer for Doctor operations.
// All SELECTs join the specializations lookup table so the doctor's
// specialization name is always populated alongside its id.
public class DoctorService {

    private static final String SELECT_DOCTOR =
            "SELECT d.*, s.name AS specialization_name FROM doctors d " +
            "JOIN specializations s ON d.specialization_id = s.id";

    public List<Doctor> getAll() throws Exception {
        return getAll(null, null);
    }

    // Sortable variant. Allowed sortKey values: name, specialization, status.
    // Anything else (or null) falls back to "newest first".
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

    // Builds a safe ORDER BY clause for the doctors listing.
    // Column names come from a literal switch -- user input is never inlined.
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

    public List<Doctor> searchAvailable(String keyword) throws Exception {
        return searchAvailable(keyword, null, null);
    }

    // Searches available doctors by name or specialization, with optional sort.
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
            // Re-use getAvailable but with our own ORDER BY.
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

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Reads specialization name from the joined specializations table (alias "specialization_name")
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
