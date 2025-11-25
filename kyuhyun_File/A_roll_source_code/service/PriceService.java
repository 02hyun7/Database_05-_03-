package com.example.service;

import com.example.dao.PriceDao;

public class PriceService {

    private final PriceDao dao = new PriceDao();

    /**
     * 특정 상영(showId) + 좌석유형(seatTypeId)의 최종 가격 조회
     */
    public Integer getFinalPrice(int showId, int seatTypeId) {
        return dao.getFinalPrice(showId, seatTypeId);
    }
}
