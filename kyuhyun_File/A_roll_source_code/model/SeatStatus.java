package com.example.model;

public enum SeatStatus {
    AVAILABLE,  // 예약/홀드 둘 다 아님
    HOLD,       // hold_seat 에 존재 (만료 전)
    BOOKED      // booking_seat 에 존재
}
