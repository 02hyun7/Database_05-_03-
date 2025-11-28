package org.example.dao;

import org.example.model.Seat;
import org.example.util.DBUtil;
import org.example.model.SeatStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {

    public Seat findById(Long seatId) throws SQLException {
        String sql = "SELECT seat_id, layout_id, row_label, col_number, seat_type_id " +
                "FROM seat WHERE seat_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSeat(rs);
                } else {
                    return null;
                }
            }
        }
    }

    /** 특정 layout(상영관 좌석 배치 버전)에 속한 모든 좌석 조회 */
    public List<Seat> findByLayout(Long layoutId) throws SQLException {
        String sql = "SELECT seat_id, layout_id, row_label, col_number, seat_type_id " +
                "FROM seat WHERE layout_id = ? " +
                "ORDER BY row_label, col_number";

        List<Seat> seats = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, layoutId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapRowToSeat(rs));
                }
            }
        }

        return seats;
    }

    public List<Seat> findSeatsByShow(Long showId) {
        List<Seat> seats = new ArrayList<>();

        Long layoutId = getLayoutIdForShow(showId);
        if (layoutId == null) {
            System.out.println("좌석 배치(layout)가 존재하지 않습니다. show_id=" + showId);
            return seats;
        }

        String sql = """
        SELECT
            s.seat_id,
            s.layout_id,
            s.row_label,
            s.col_number,
            s.seat_type_id,
            b.seat_id AS booked_seat,
            h.seat_id AS hold_seat
        FROM seat s
        LEFT JOIN booking_seat b
               ON s.seat_id = b.seat_id
              AND b.show_id = ?
        LEFT JOIN hold_seat h
               ON s.seat_id = h.seat_id
              AND h.show_id = ?
              AND h.expires_at > NOW()
        WHERE s.layout_id = ?
        ORDER BY s.row_label, s.col_number
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, showId);
            pstmt.setLong(3, layoutId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat();
                    seat.setSeatId(rs.getLong("seat_id"));
                    seat.setLayoutId(rs.getLong("layout_id"));
                    seat.setRowLabel(rs.getString("row_label"));
                    seat.setColNumber(rs.getInt("col_number"));
                    seat.setSeatTypeId(rs.getLong("seat_type_id"));

                    // 상태 계산: BOOKED > HOLD > AVAILABLE
                    if (rs.getObject("booked_seat") != null) {
                        seat.setStatus(SeatStatus.BOOKED);
                    } else if (rs.getObject("hold_seat") != null) {
                        seat.setStatus(SeatStatus.HOLD);
                    } else {
                        seat.setStatus(SeatStatus.AVAILABLE);
                    }

                    seats.add(seat);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    private Seat mapRowToSeat(ResultSet rs) throws SQLException {
        Long seatId = rs.getLong("seat_id");
        Long layoutId = rs.getLong("layout_id");
        String rowLabel = rs.getString("row_label");
        int colNumber = rs.getInt("col_number");
        Long seatTypeId = rs.getLong("seat_type_id");

        return new Seat(seatId, layoutId, rowLabel, colNumber, seatTypeId);
    }
    private Long getLayoutIdForShow(Long showId) {

        String sql = """
        SELECT sl.layout_id
        FROM showtime s
        JOIN screen_layout sl
          ON s.screen_id = sl.screen_id
        WHERE s.show_id = ?
        ORDER BY sl.created_at DESC
        LIMIT 1
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("layout_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 못 찾으면 null
    }
}