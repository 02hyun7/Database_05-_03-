package org.example.dao;

import org.example.model.Booking;
import org.example.util.DBUtil;

import java.sql.*;

public class BookingDao {

    public Booking create(Booking booking) throws SQLException {
        String sql = "INSERT INTO booking (member_id, show_id, status, total_amount, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, booking.getMemberId());
            pstmt.setLong(2, booking.getShowId());
            pstmt.setString(3, booking.getStatus());
            pstmt.setBigDecimal(4, booking.getTotalAmount());

            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    booking.setBookingId(keys.getLong(1));
                }
            }
        }
        return booking;
    }
}