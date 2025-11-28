package org.example.dao;

import org.example.model.HoldSeat;
import org.example.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class HoldSeatDao {

    // -----------------------------
    // 1) 새 홀드 생성
    // -----------------------------
    public HoldSeat create(HoldSeat holdSeat) throws SQLException {
        String sql = "INSERT INTO hold_seat (show_id, seat_id, member_id, expires_at) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, holdSeat.getShowId());
            pstmt.setLong(2, holdSeat.getSeatId());
            pstmt.setLong(3, holdSeat.getMemberId());
            pstmt.setTimestamp(4, Timestamp.valueOf(holdSeat.getExpiresAt()));

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    holdSeat.setHoldId(keys.getLong(1));
                }
            }
        }
        return holdSeat;
    }

    // -----------------------------
    // 2) 아직 만료 안 된 홀드 찾기
    // -----------------------------
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

    // -----------------------------
    // 3) hold_id로 삭제 (취소용)
    // -----------------------------
    public int deleteById(Long holdId) throws SQLException {
        String sql = "DELETE FROM hold_seat WHERE hold_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, holdId);
            return pstmt.executeUpdate();
        }
    }

    // -----------------------------
    // 4) 만료된 홀드 일괄 삭제
    // -----------------------------
    public int deleteExpired() throws SQLException {
        String sql = "DELETE FROM hold_seat WHERE expires_at <= NOW()";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeUpdate();
        }
    }
    //BookingService에 필요한 행위 추가
    public int deleteByMemberAndSeat(Long memberId, Long showId, Long seatId) throws SQLException {
        String sql = "DELETE FROM hold_seat " +
                "WHERE member_id = ? AND show_id = ? AND seat_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, memberId);
            pstmt.setLong(2, showId);
            pstmt.setLong(3, seatId);

            return pstmt.executeUpdate();
        }
    }

    private HoldSeat map(ResultSet rs) throws SQLException {
        Long holdId = rs.getLong("hold_id");
        Long showId = rs.getLong("show_id");
        Long seatId = rs.getLong("seat_id");
        Long memberId = rs.getLong("member_id");
        LocalDateTime expiresAt = rs.getTimestamp("expires_at").toLocalDateTime();

        return new HoldSeat(holdId, showId, seatId, memberId, expiresAt);
    }
}