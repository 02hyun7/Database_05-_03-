package org.example.dao;

import org.example.model.Payment;
import org.example.util.DBUtil;

import java.sql.*;

public class PaymentDao {

    public Payment insert(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment (booking_id, method_id, amount, status, approved_at) " +
                "VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, payment.getBookingId());
            pstmt.setLong(2, payment.getMethodId());
            pstmt.setInt(3, payment.getAmount());
            pstmt.setString(4, payment.getStatus());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setPaymentId(rs.getLong(1));
                }
            }
        }
        return payment;
    }
}