package org.example.service;

import org.example.dao.SeatDao;
import org.example.model.Seat;

import java.util.List;
import java.util.stream.Collectors;

public class SeatService {

    private final SeatDao seatDao = new SeatDao();

    /** show_id 기준으로 좌석 + 상태 전체 조회 */
    public List<Seat> getSeatsWithStatus(Long showId) {
        return seatDao.findSeatsByShow(showId);
    }

    /** show_id + 선택한 seatId 목록이 모두 예약 가능(AVAILABLE)인지 체크 */
    public boolean areSeatsAllAvailable(Long showId, List<Long> seatIds) {
        List<Seat> seats = getSeatsWithStatus(showId);

        var unavailable = seats.stream()
                .filter(s -> seatIds.contains(s.getSeatId()))
                .filter(s -> s.getStatus() == null || !s.getStatus().isReservable())
                .toList();

        return unavailable.isEmpty();
    }
}