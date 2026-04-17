package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Doctor operations.
 */
public class DoctorService {

    /**
     * Retrieves all doctors.
     */
    public List<Doctor> getAll() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY created_at DESC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    /**
     * Retrieves only available doctors.
     */
    public List<Doctor> getAvailable() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE available = 1 ORDER BY full_name ASC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(mapDoctor(rs));
            }
        }
        return doctors;
    }

    /**
     * Retrieves a doctor by ID.
     */
    public Doctor getById(int id) throws Exception {
        String sql = "SELECT * FROM doctors WHERE id = ?";
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

    /**
     * Adds a new doctor.
     */
    public void add(Doctor doctor) throws Exception {
        String sql = "INSERT INTO doctors (full_name, specialization, contact, email, available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getContact());
            ps.setString(4, doctor.getEmail());
            ps.setInt(5, doctor.isAvailable() ? 1 : 0);
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing doctor.
     */
    public void update(Doctor doctor) throws Exception {
        String sql = "UPDATE doctors SET full_name = ?, specialization = ?, contact = ?, email = ?, available = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getContact());
            ps.setString(4, doctor.getEmail());
            ps.setInt(5, doctor.isAvailable() ? 1 : 0);
            ps.setInt(6, doctor.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a doctor by ID.
     */
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setContact(rs.getString("contact"));
        doctor.setEmail(rs.getString("email"));
        doctor.setAvailable(rs.getInt("available") == 1);
        return doctor;
    }
}
