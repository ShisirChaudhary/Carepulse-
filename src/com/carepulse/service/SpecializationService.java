package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Specialization;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Service class for the specializations lookup table.
public class SpecializationService {

    // Returns every specialization ordered by name.
    public List<Specialization> getAll() throws Exception {
        List<Specialization> list = new ArrayList<>();
        String sql = "SELECT * FROM specializations ORDER BY name ASC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    // Returns a single specialization by id, or null if not found.
    public Specialization getById(int id) throws Exception {
        String sql = "SELECT * FROM specializations WHERE id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        }
    }

    // Maps the current row of the result set into a Specialization object.
    private Specialization map(ResultSet rs) throws SQLException {
        Specialization s = new Specialization();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        return s;
    }
}
