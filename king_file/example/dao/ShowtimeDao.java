package org.example.dao;

import org.example.model.Showtime;
import org.example.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDao {

    public Showtime findById(Long showId) throws SQLException {
        String sql = "SELECT show_id, screen_id, version_id, starts_at, ends_at, show_status_id " +
                "FROM showtime WHERE show_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToShowtime(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /** 특정 영화(movie_id)에 대한 상영시간 목록 조회 */
    public List<Showtime> findByMovieId(Long movieId) throws SQLException {
        String sql =
                "SELECT s.show_id, s.screen_id, s.version_id, s.starts_at, s.ends_at, s.show_status_id " +
                        "FROM showtime s " +
                        "JOIN movie_version mv ON s.version_id = mv.version_id " +
                        "WHERE mv.movie_id = ? " +
                        "ORDER BY s.starts_at";

        List<Showtime> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, movieId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToShowtime(rs));
                }
            }
        }

        return result;
    }

    private Showtime mapRowToShowtime(ResultSet rs) throws SQLException {
        Long showId = rs.getLong("show_id");
        Long screenId = rs.getLong("screen_id");
        Long versionId = rs.getLong("version_id");

        Timestamp starts = rs.getTimestamp("starts_at");
        Timestamp ends = rs.getTimestamp("ends_at");
        LocalDateTime startsAt = starts != null ? starts.toLocalDateTime() : null;
        LocalDateTime endsAt = ends != null ? ends.toLocalDateTime() : null;

        Long showStatusId = rs.getLong("show_status_id");

        return new Showtime(showId, screenId, versionId, startsAt, endsAt, showStatusId);
    }
}