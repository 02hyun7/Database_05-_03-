package com.example;

import com.example.dao.SeatDao;
import com.example.model.Seat;

import java.util.List;

public class SeatMain {

    public static void main(String[] args) {
        SeatDao dao = new SeatDao();

        int showId = 1;    // 테스트용 상영 ID
        int layoutId = 1;  // 테스트용 좌석배치 ID

        List<Seat> seats = dao.findSeatsByShow(showId);

        System.out.println("=== 좌석 상태 출력 ===");
        for (Seat s : seats) {
            System.out.println(
                    s.getRowLabel() + s.getColNumber() + " -> " + s.getStatus()
            );
        }
    }
}
