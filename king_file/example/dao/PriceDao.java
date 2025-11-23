package org.example.dao;

import org.example.model.Price;
import org.example.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;

public class PriceDao {

    /** show_id + seat_type_id 기준으로 한 줄 가져오기 */
    public Price findPriceRow(Long showId, Long seatTypeId) throws SQLException {
        String sql =
                "SELECT price_id, show_id, seat_type_id, default_price, changed_price, rule_apply_id " +
                        "FROM price " +
                        "WHERE show_id = ? AND seat_type_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, seatTypeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Long priceId = rs.getLong("price_id");
                    BigDecimal defaultPrice = rs.getBigDecimal("default_price");
                    BigDecimal changedPrice = rs.getBigDecimal("changed_price");
                    Long ruleApplyId = rs.getObject("rule_apply_id") != null
                            ? rs.getLong("rule_apply_id") : null;

                    return new Price(priceId, showId, seatTypeId, defaultPrice, changedPrice, ruleApplyId);
                } else {
                    return null;
                }
            }
        }
    }

    /** 실제 결제에 사용할 금액 계산: changed_price가 있으면 그 값을, 없으면 default_price 사용 */
    public BigDecimal findFinalPrice(Long showId, Long seatTypeId) throws SQLException {
        Price row = findPriceRow(showId, seatTypeId);
        if (row == null) {
            throw new SQLException("가격 정보 없음: showId=" + showId + ", seatTypeId=" + seatTypeId);
        }
        if (row.getChangedPrice() != null) {
            return row.getChangedPrice();
        }
        return row.getDefaultPrice();
    }
}