package com.example.dao;

import com.example.model.MovieVersion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieVersionDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    // 1) 전체 버전 조회
    public List<MovieVersion> findAll() {
        List<MovieVersion> list = new ArrayList<>();

        String sql = """
            SELECT version_id, movie_id, format, audio_lang_id, subtitle_id
            FROM movie_version
            ORDER BY movie_id, version_id
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                MovieVersion mv = mapRow(rs);
                list.add(mv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 2) 특정 영화의 버전 목록 조회 (Service가 요구하는 정확한 이름)
    public List<MovieVersion> findByMovie(int movieId) {
        List<MovieVersion> list = new ArrayList<>();

        String sql = """
            SELECT version_id, movie_id, format, audio_lang_id, subtitle_id
            FROM movie_version
            WHERE movie_id = ?
            ORDER BY version_id
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieVersion mv = mapRow(rs);
                list.add(mv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 3) format 기준 조회 (IMAX / 4DX / 2D 등)
    public List<MovieVersion> findByFormat(String format) {
        List<MovieVersion> list = new ArrayList<>();

        String sql = """
            SELECT version_id, movie_id, format, audio_lang_id, subtitle_id
            FROM movie_version
            WHERE format = ?
            ORDER BY movie_id
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, format);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MovieVersion mv = mapRow(rs);
                list.add(mv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 공통 매핑 작업
    private MovieVersion mapRow(ResultSet rs) throws SQLException {
        MovieVersion mv = new MovieVersion();
        mv.setVersionId(rs.getInt("version_id"));
        mv.setMovieId(rs.getInt("movie_id"));
        mv.setFormat(rs.getString("format"));
        mv.setAudioLangId(rs.getInt("audio_lang_id"));
        mv.setSubtitleId(rs.getInt("subtitle_id"));
        return mv;
    }
}
