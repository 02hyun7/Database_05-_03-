package org.example.service;

import org.example.dao.PriceDao;

import java.math.BigDecimal;
import java.sql.SQLException;

public class PriceService {

    private final PriceDao priceDao = new PriceDao();

    /**
     * 특정 상영(showId) + 좌석유형(seatTypeId)의 최종 가격 조회
     *  - price.changed_price가 있으면 그 값을 사용
     *  - 없으면 price.default_price 사용
     */
    public BigDecimal getFinalPrice(Long showId, Long seatTypeId) {
        try {
            return priceDao.findFinalPrice(showId, seatTypeId);
        } catch (SQLException e) {
            System.out.println("[ERROR] 최종 가격 조회 실패: showId=" + showId
                    + ", seatTypeId=" + seatTypeId
                    + ", message=" + e.getMessage());
            e.printStackTrace();
            return null; // 호출부에서 null 체크해서 에러 처리
        }
    }

    /**
     * 필요하면: BigDecimal 대신 int로 쓰고 싶을 때 사용할 헬퍼
     */
    public Integer getFinalPriceAsInt(Long showId, Long seatTypeId) {
        BigDecimal price = getFinalPrice(showId, seatTypeId);
        if (price == null) {
            return null;
        }
        return price.intValue();   // 소수점 금액 안 쓰는 구조라면 이렇게 변환
    }
}