package com.carepulse.service;

import com.carepulse.config.DBConfig;
import com.carepulse.model.Specialization;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Reads from the specializations lookup table
public class SpecializationService {

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

    private Specialization map(ResultSet rs) throws SQLException {
        Specialization s = new Specialization();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        return s;
    }
}
