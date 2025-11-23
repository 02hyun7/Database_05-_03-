package org.example.dao;

import org.example.model.HoldSeat;
import org.example.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class HoldSeatDao {

    public HoldSeat findActiveHold(Long showId, Long seatId) throws SQLException {
        String sql = "SELECT hold_id, show_id, seat_id, member_id, expires_at " +
                "FROM hold_seat " +
                "WHERE show_id = ? AND seat_id = ? AND expires_at > NOW()";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public HoldSeat insertHold(Long showId, Long seatId, Long memberId, int minutes) throws SQLException {
        String sql = "INSERT INTO hold_seat (show_id, seat_id, member_id, expires_at) " +
                "VALUES (?, ?, ?, DATE_ADD(NOW(), INTERVAL ? MINUTE))";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, seatId);
            pstmt.setLong(3, memberId);
            pstmt.setInt(4, minutes);

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);
                    return new HoldSeat(id, showId, seatId, memberId, LocalDateTime.now().plusMinutes(minutes));
                }
            }
        }
        return null;
    }

    private HoldSeat map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("hold_id");
        Long showId = rs.getLong("show_id");
        Long seatId = rs.getLong("seat_id");
        Long memberId = rs.getLong("member_id");
        LocalDateTime expiresAt = rs.getTimestamp("expires_at").toLocalDateTime();

        return new HoldSeat(id, showId, seatId, memberId, expiresAt);
    }
}
