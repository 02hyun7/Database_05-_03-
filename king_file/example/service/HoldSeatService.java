package org.example.service;

import org.example.dao.BookingSeatDao;
import org.example.dao.HoldSeatDao;
import org.example.model.HoldSeat;

import java.time.LocalDateTime;

public class HoldSeatService {

    private final HoldSeatDao holdSeatDao;
    private final BookingSeatDao bookingSeatDao;

    // 기본 홀드 시간 (분)
    private final int defaultHoldMinutes = 5;

    public HoldSeatService() {
        this.holdSeatDao = new HoldSeatDao();
        this.bookingSeatDao = new BookingSeatDao();
    }

    // -------------------------------------------------
    // 1) 좌석 홀드 시도
    //    - 이미 예약됨?  → 실패
    //    - 이미 다른 사람 홀드 중? → 실패
    //    - 그 외 → 새 홀드 생성
    // -------------------------------------------------
    public HoldSeat holdSeatForMember(Long memberId, Long showId, Long seatId) {
        try {
            // 1) 먼저 만료된 것들 정리
            holdSeatDao.deleteExpired();

            // 2) 이미 예약된 좌석인지 확인
            boolean booked = bookingSeatDao.existsActiveBookingForSeat(showId, seatId);
            if (booked) {
                System.out.println("[INFO] 이미 예약된 좌석입니다.");
                return null;
            }

            // 3) 다른 사람이 홀드 중인지 확인
            HoldSeat existing = holdSeatDao.findActiveHold(showId, seatId);
            if (existing != null) {
                System.out.println("[INFO] 다른 사용자가 홀드 중인 좌석입니다.");
                return null;
            }

            // 4) 새 홀드 생성
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(defaultHoldMinutes);
            HoldSeat holdSeat = new HoldSeat();
            holdSeat.setShowId(showId);
            holdSeat.setSeatId(seatId);
            holdSeat.setMemberId(memberId);
            holdSeat.setExpiresAt(expiresAt);

            HoldSeat created = holdSeatDao.create(holdSeat);
            System.out.println("[INFO] 좌석 홀드 성공: " + created);
            return created;

        } catch (Exception e) {
            System.out.println("[ERROR] 좌석 홀드 실패: " + e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------
    // 2) 홀드 취소 (사용자가 직접 취소)
    //    - 간단하게 hold_id만 받아서 삭제
    // -------------------------------------------------
    public boolean cancelHold(Long holdId) {
        try {
            int deleted = holdSeatDao.deleteById(holdId);
            return deleted > 0;
        } catch (Exception e) {
            System.out.println("[ERROR] 홀드 취소 실패: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------
    // 3) 만료된 홀드 일괄 삭제 (배치/관리자용)
    // -------------------------------------------------
    public int clearExpiredHolds() {
        try {
            return holdSeatDao.deleteExpired();
        } catch (Exception e) {
            System.out.println("[ERROR] 만료 홀드 삭제 실패: " + e.getMessage());
            return 0;
        }
    }
}