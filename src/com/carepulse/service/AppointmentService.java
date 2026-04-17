package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for Appointment operations.
 */
public class AppointmentService {

    /**
     * Retrieves all appointments with joined user and doctor names.
     */
    public List<Appointment> getAll() throws Exception {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name AS patient_name, d.full_name AS doctor_name, d.specialization " +
                     "FROM appointments a " +
                     "JOIN users u ON a.patient_id = u.id " +
                     "JOIN doctors d ON a.doctor_id = d.id " +
                     "ORDER BY a.appointment_date DESC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        }
        return list;
    }

    /**
     * Searches appointments by patient name or doctor name.
     */
    public List<Appointment> search(String keyword) throws Exception {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name AS patient_name, d.full_name AS doctor_name, d.specialization " +
                     "FROM appointments a " +
                     "JOIN users u ON a.patient_id = u.id " +
                     "JOIN doctors d ON a.doctor_id = d.id " +
                     "WHERE u.full_name LIKE ? OR d.full_name LIKE ? " +
                     "ORDER BY a.appointment_date DESC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        }
        return list;
    }

    /**
     * Retrieves all appointments for a specific patient.
     */
    public List<Appointment> getByPatient(int patientId) throws Exception {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name AS patient_name, d.full_name AS doctor_name, d.specialization " +
                     "FROM appointments a " +
                     "JOIN users u ON a.patient_id = u.id " +
                     "JOIN doctors d ON a.doctor_id = d.id " +
                     "WHERE a.patient_id = ? " +
                     "ORDER BY a.appointment_date DESC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        }
        return list;
    }

    /**
     * Books a new appointment.
     */
    public void book(Appointment a) throws Exception {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes) VALUES (?, ?, ?, ?, 'pending', ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setString(3, a.getAppointmentDate());
            ps.setString(4, a.getAppointmentTime());
            ps.setString(5, a.getNotes());
            ps.executeUpdate();
        }
    }

    /**
     * Updates the status of an appointment (admin action).
     */
    public void updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Cancels an appointment only if it belongs to the specified patient.
     */
    public void cancel(int id, int patientId) throws Exception {
        String sql = "UPDATE appointments SET status = 'cancelled' WHERE id = ? AND patient_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, patientId);
            ps.executeUpdate();
        }
    }

    /**
     * Returns total count of all appointments.
     */
    public int countAll() throws Exception {
        String sql = "SELECT COUNT(*) FROM appointments";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    /**
     * Returns total count of appointments for a specific patient.
     */
    public int countByPatient(int patientId) throws Exception {
        String sql = "SELECT COUNT(*) FROM appointments WHERE patient_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setId(rs.getInt("id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setPatientName(rs.getString("patient_name"));
        a.setDoctorName(rs.getString("doctor_name"));
        a.setSpecialization(rs.getString("specialization"));
        a.setAppointmentDate(rs.getString("appointment_date"));
        a.setAppointmentTime(rs.getString("appointment_time"));
        a.setStatus(rs.getString("status"));
        a.setNotes(rs.getString("notes"));
        return a;
    }
}
