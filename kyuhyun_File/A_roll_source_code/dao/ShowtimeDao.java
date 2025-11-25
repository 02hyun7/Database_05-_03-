package com.example.dao;

import com.example.model.Showtime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    // === 1) 특정 영화의 상영정보 조회 ===
    public List<Showtime> findByMovie(int movieId) {
        List<Showtime> list = new ArrayList<>();

        String sql = """
            SELECT 
                s.show_id,
                s.screen_id,
                sc.name AS screen_name,
                mv.movie_id,
                m.title AS movie_title,
                mv.format AS version_format,
                s.starts_at,
                s.ends_at
            FROM showtime s
            JOIN screen sc ON s.screen_id = sc.screen_id
            JOIN movie_version mv ON s.version_id = mv.version_id
            JOIN movie m ON mv.movie_id = m.movie_id
            WHERE mv.movie_id = ?
            ORDER BY s.starts_at;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Showtime st = new Showtime();

                st.setShowId(rs.getInt("show_id"));
                st.setScreenId(rs.getInt("screen_id"));
                st.setScreenName(rs.getString("screen_name"));

                st.setMovieId(rs.getInt("movie_id"));
                st.setMovieTitle(rs.getString("movie_title"));
                st.setVersionFormat(rs.getString("version_format"));

                Timestamp starts = rs.getTimestamp("starts_at");
                if (starts != null) st.setStartsAt(starts.toLocalDateTime());

                Timestamp ends = rs.getTimestamp("ends_at");
                if (ends != null) st.setEndsAt(ends.toLocalDateTime());

                list.add(st);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // === 2) 단일 상영 조회 (show_id) ===
    public Showtime findById(int showId) {

        String sql = """
            SELECT 
                s.show_id,
                s.screen_id,
                sc.name AS screen_name,
                mv.movie_id,
                m.title AS movie_title,
                mv.format AS version_format,
                s.starts_at,
                s.ends_at
            FROM showtime s
            JOIN screen sc ON s.screen_id = sc.screen_id
            JOIN movie_version mv ON s.version_id = mv.version_id
            JOIN movie m ON mv.movie_id = m.movie_id
            WHERE s.show_id = ?
            LIMIT 1;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, showId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Showtime st = new Showtime();

                st.setShowId(rs.getInt("show_id"));
                st.setScreenId(rs.getInt("screen_id"));
                st.setScreenName(rs.getString("screen_name"));

                st.setMovieId(rs.getInt("movie_id"));
                st.setMovieTitle(rs.getString("movie_title"));
                st.setVersionFormat(rs.getString("version_format"));

                Timestamp starts = rs.getTimestamp("starts_at");
                if (starts != null) st.setStartsAt(starts.toLocalDateTime());

                Timestamp ends = rs.getTimestamp("ends_at");
                if (ends != null) st.setEndsAt(ends.toLocalDateTime());

                return st;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
