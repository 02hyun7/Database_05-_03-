package org.example.dao;

import org.example.model.Screen;
import org.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScreenDao {

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

        try (Connection conn = DBUtil.getConnection();
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

        try (Connection conn = DBUtil.getConnection();
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
