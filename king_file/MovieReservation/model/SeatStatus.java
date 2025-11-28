package org.example.model;

public enum SeatStatus {
    AVAILABLE,
    HOLD,
    BOOKED;

    /** 실제 예약 생성 가능한 상태인지 */
    public boolean isReservable() {
        return this == AVAILABLE;
    }
}