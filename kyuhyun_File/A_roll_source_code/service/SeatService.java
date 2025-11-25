package com.example.service;

import com.example.dao.SeatDao;
import com.example.model.Seat;

import java.util.List;

public class SeatService {

    private final SeatDao dao = new SeatDao();

    // showId만 넘기면 내부에서 layout_id까지 계산
    public List<Seat> getSeatsWithStatus(int showId) {
        return dao.findSeatsByShow(showId);
    }
}
