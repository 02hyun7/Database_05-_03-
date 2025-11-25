package com.example.dao;

import com.example.model.Price;

import java.sql.*;

public class PriceDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    /**
     * 특정 상영(showId) + 좌석유형(seatTypeId)의 최종 가격 조회
     */
    public Integer getFinalPrice(int showId, int seatTypeId) {

        String sql = """
            SELECT price_id, show_id, seat_type_id, default_price, changed_price, rule_apply_id
            FROM price
            WHERE show_id = ? AND seat_type_id = ?
            LIMIT 1
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, showId);
            pstmt.setInt(2, seatTypeId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int defaultPrice = rs.getInt("default_price");
                int ruleApplyId = rs.getInt("rule_apply_id");
                Integer changed = (Integer) rs.getObject("changed_price");

                // 최종 가격 계산: changed_price가 있으면 그것을 우선
                int finalPrice = (changed != null) ? changed : defaultPrice;

                return finalPrice;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 가격 없음
    }
}
