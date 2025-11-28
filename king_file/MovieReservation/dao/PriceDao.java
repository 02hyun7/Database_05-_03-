package org.example.dao;

import org.example.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * price 관련 DB 접근
 * - seatType 기반 조회: findFinalPrice(showId, seatTypeId)
 * - seatId 기반 조회:   findFinalPriceBySeat(showId, seatId)  (fn_get_final_price 사용)
 */
public class PriceDao {

    /**
     * 🔥 새로 추가한 메소드
     *  - 좌석 ID(seat_id) 기준 최종 가격 조회
     *  - MySQL 함수 fn_get_final_price(show_id, seat_id)을 그대로 호출
     */
    public BigDecimal findFinalPriceBySeat(Long showId, Long seatId) throws SQLException {

        String sql = "SELECT fn_get_final_price(?, ?) AS final_price";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // price가 없으면 fn_get_final_price가 NULL을 리턴하므로 여기서도 null 나올 수 있음
                    return rs.getBigDecimal("final_price");
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * 🔁 기존 A/B 코드 호환용 메소드
     *  - show_id + seat_type_id 기준으로 price 테이블 조회
     *  - changed_price가 있으면 그 값, 아니면 default_price 사용
     *  - 행이 없으면 null 반환
     *
     *  ※ 더 이상 Price 모델 클래스(엔티티)를 사용하지 않고
     *     바로 BigDecimal만 조회하도록 구현했다.
     */
    public BigDecimal findFinalPrice(Long showId, Long seatTypeId) throws SQLException {

        String sql =
                "SELECT COALESCE(changed_price, default_price) AS final_price " +
                        "FROM price " +
                        "WHERE show_id = ? AND seat_type_id = ? " +
                        "LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, showId);
            pstmt.setLong(2, seatTypeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("final_price");   // 없으면 null
                } else {
                    return null;
                }
            }
        }
    }
}