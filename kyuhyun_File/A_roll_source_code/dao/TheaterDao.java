package com.example.dao;

import com.example.model.Theater;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheaterDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    /**
     * 모든 영화관 조회
     */
    public List<Theater> findAll() {
        List<Theater> theaters = new ArrayList<>();

        String sql = "SELECT theater_id, city_region_id, name, address FROM theater";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Theater t = new Theater();
                t.setTheaterId(rs.getInt("theater_id"));
                t.setCityRegionId(rs.getInt("city_region_id"));
                t.setName(rs.getString("name"));
                t.setAddress(rs.getString("address"));

                theaters.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return theaters;
    }

    /**
     * 특정 영화관 조회 (theater_id)
     */
    public Theater findById(int theaterId) {

        String sql = """
            SELECT 
                theater_id,
                city_region_id,
                name,
                address
            FROM theater
            WHERE theater_id = ?
            LIMIT 1;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Theater t = new Theater();
                t.setTheaterId(rs.getInt("theater_id"));
                t.setCityRegionId(rs.getInt("city_region_id"));
                t.setName(rs.getString("name"));
                t.setAddress(rs.getString("address"));
                return t;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
