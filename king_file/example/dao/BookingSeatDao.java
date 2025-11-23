package org.example.dao;

import org.example.model.BookingSeat;
import org.example.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}