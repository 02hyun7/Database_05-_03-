package org.example.service;

import org.example.dao.BookingDao;
import org.example.dao.BookingSeatDao;
import org.example.dao.HoldSeatDao;
import org.example.dao.PriceDao;
import org.example.model.Booking;
import org.example.model.HoldSeat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    private final BookingDao bookingDao;
    private final BookingSeatDao bookingSeatDao;
    private final HoldSeatDao holdSeatDao;
    private final PriceDao priceDao;
    private final SeatService seatService;

    public BookingService() {
        this.bookingDao = new BookingDao();
        this.bookingSeatDao = new BookingSeatDao();
        this.holdSeatDao = new HoldSeatDao();
        this.priceDao = new PriceDao();
        this.seatService = new SeatService();
    }

    /**
     * 회원 예매 생성
     *
     * @param memberId 예매하는 회원 ID
     * @param showId   상영 ID (showtime.show_id)
     * @param seatIds  선택한 좌석 ID 리스트
     * @return 생성된 Booking, 실패 시 null
     */
    public Booking createBookingForMember(Long memberId, Long showId, List<Long> seatIds) {

        try {
            if (seatIds == null || seatIds.isEmpty()) {
                System.out.println("[ERROR] 좌석이 하나도 선택되지 않았습니다.");
                return null;
            }

            if (!seatService.areSeatsAllAvailable(showId, seatIds)) {
                System.out.println("[WARN] 선택한 좌석 중 이미 예약되었거나 홀드된 좌석이 있습니다.");
                System.out.println("다시 시도해 주세요.");
                return null;
            }

            // 1) 만료된 홀드 먼저 정리
            holdSeatDao.deleteExpired();

            // 2) 좌석별로 예약 가능 여부 체크 + 가격 계산
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<BigDecimal> seatPrices = new ArrayList<>();

            for (Long seatId : seatIds) {

                // 2-1) 이미 예약된 좌석인지 확인
                boolean booked = bookingSeatDao.existsActiveBookingForSeat(showId, seatId);
                if (booked) {
                    System.out.println("[ERROR] 이미 예약된 좌석이 포함되어 있습니다. seatId=" + seatId);
                    return null;
                }

                // 2-2) 다른 사람이 홀드 중인지 확인
                HoldSeat activeHold = holdSeatDao.findActiveHold(showId, seatId);
                if (activeHold != null && !activeHold.getMemberId().equals(memberId)) {
                    System.out.println("[ERROR] 다른 사용자가 홀드 중인 좌석이 포함되어 있습니다. seatId=" + seatId);
                    return null;
                }

                // 2-3) 가격 계산 (DB 함수 fn_get_final_price 이용: show_id + seat_id 기반)
                BigDecimal price = priceDao.findFinalPriceBySeat(showId, seatId);
                if (price == null) {
                    System.out.println("[ERROR] 선택한 좌석에는 가격이 등록되어 있지 않습니다. showId=" + showId + ", seatId=" + seatId);
                    System.out.println("다른 좌석 또는 다른 상영일시를 선택해주세요.\n");
                    return null;
                }

                seatPrices.add(price);
                totalAmount = totalAmount.add(price);
            }

            // 3) booking 생성
            Booking booking = new Booking();
            booking.setMemberId(memberId);
            booking.setShowId(showId);
            booking.setStatus("PENDING");
            booking.setTotalAmount(totalAmount);

            Booking created = bookingDao.create(booking);
            Long bookingId = created.getBookingId();

            // 4) booking_seat 좌석별 insert
            for (int i = 0; i < seatIds.size(); i++) {
                Long seatId = seatIds.get(i);
                BigDecimal seatPrice = seatPrices.get(i);
                bookingSeatDao.insertSeat(bookingId, showId, seatId, seatPrice);
            }

            // 5) 이 회원이 잡고 있던 해당 좌석들에 대한 hold 삭제
            for (Long seatId : seatIds) {
                holdSeatDao.deleteByMemberAndSeat(memberId, showId, seatId);
            }

            System.out.println("[INFO] 예약 생성 완료. bookingId=" + bookingId +
                    ", totalAmount=" + totalAmount);
            return created;

        } catch (Exception e) {
            System.out.println("[ERROR] 예약 생성 중 오류: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
