package org.example.dao;

import org.example.model.BookingSeat;
import org.example.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingSeatDao {

    public void insert(BookingSeat bookingSeat) throws SQLException {
        String sql = "INSERT INTO booking_seat (booking_id, seat_id, show_id, price) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, bookingSeat.getBookingId());
            pstmt.setLong(2, bookingSeat.getSeatId());
            pstmt.setLong(3, bookingSeat.getShowId());
            pstmt.setInt(4, bookingSeat.getPrice());

            pstmt.executeUpdate();
        }
    }
    public boolean existsActiveBookingForSeat(Long showId, Long seatId) throws SQLException {
        String sql =
                "SELECT 1 " +
                        "FROM booking_seat bs " +
                        "JOIN booking b ON bs.booking_id = b.booking_id " +
                        "WHERE bs.show_id = ? " +
                        "  AND bs.seat_id = ? " +
                        "  AND b.status IN ('PENDING', 'CONFIRMED') " +
                        "LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    //BookingService
    public void insertSeat(Long bookingId, Long showId, Long seatId, BigDecimal price) throws SQLException {
        String sql = "INSERT INTO booking_seat (booking_id, seat_id, show_id, price) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, bookingId);
            pstmt.setLong(2, seatId);
            pstmt.setLong(3, showId);
            pstmt.setBigDecimal(4, price);

            pstmt.executeUpdate();
        }
    }
}