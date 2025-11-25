package com.example.dao;

import com.example.model.Screen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScreenDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    // === 1) 특정 영화관의 상영관 목록 ===
    public List<Screen> findByTheater(int theaterId) {
        List<Screen> list = new ArrayList<>();

        String sql = """
            SELECT 
                screen_id,
                theater_id,
                name
            FROM screen
            WHERE theater_id = ?
            ORDER BY screen_id;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Screen sc = new Screen();
                sc.setScreenId(rs.getInt("screen_id"));
                sc.setTheaterId(rs.getInt("theater_id"));
                sc.setName(rs.getString("name"));
                list.add(sc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // === 2) 상영관 단일 조회 ===
    public Screen findById(int screenId) {

        String sql = """
            SELECT 
                screen_id,
                theater_id,
                name
            FROM screen
            WHERE screen_id = ?
            LIMIT 1;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, screenId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Screen sc = new Screen();
                sc.setScreenId(rs.getInt("screen_id"));
                sc.setTheaterId(rs.getInt("theater_id"));
                sc.setName(rs.getString("name"));
                return sc;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
