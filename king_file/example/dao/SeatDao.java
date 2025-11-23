package org.example.dao;

import org.example.model.Seat;
import org.example.util.DBUtil;

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

    private Seat mapRowToSeat(ResultSet rs) throws SQLException {
        Long seatId = rs.getLong("seat_id");
        Long layoutId = rs.getLong("layout_id");
        String rowLabel = rs.getString("row_label");
        int colNumber = rs.getInt("col_number");
        Long seatTypeId = rs.getLong("seat_type_id");

        return new Seat(seatId, layoutId, rowLabel, colNumber, seatTypeId);
    }
}