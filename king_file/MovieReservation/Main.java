package org.example;

import org.example.dao.PaymentDao;
import org.example.model.Booking;
import org.example.model.Member;
import org.example.model.Movie;
import org.example.model.Screen;
import org.example.model.Seat;
import org.example.model.SeatStatus;
import org.example.model.Showtime;
import org.example.model.Theater;
import org.example.model.Payment;
import org.example.service.BookingService;
import org.example.service.MemberService;
import org.example.service.MovieService;
import org.example.service.ScreenService;
import org.example.service.SeatService;
import org.example.service.ShowtimeService;
import org.example.service.TheaterService;
import org.example.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    // 서비스 레이어
    private static final MovieService movieService = new MovieService();
    private static final ShowtimeService showtimeService = new ShowtimeService();
    private static final SeatService seatService = new SeatService();
    private static final BookingService bookingService = new BookingService();
    private static final MemberService memberService = new MemberService();
    private static final TheaterService theaterService = new TheaterService();
    private static final ScreenService screenService = new ScreenService();
    private static final PaymentDao paymentDao = new PaymentDao();

    // 현재 로그인/선택된 사용자 상태
    private static Member currentMember = null;

    public static void main(String[] args) {
        System.out.println("===== 영화 예매 시스템 (콘솔 버전) =====");

        try {
            mainMenuLoop();
        } catch (Exception e) {
            System.out.println("[FATAL] 예기치 못한 오류로 프로그램을 종료합니다: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("프로그램을 종료합니다.");
        }
    }

    // =====================================================================
    // 메인 메뉴 루프
    // =====================================================================
    private static void mainMenuLoop() {
        while (true) {
            System.out.println();
            System.out.println("-------------------------------------------------");
            System.out.println(" 현재 로그인 상태: " + describeCurrentUser());
            System.out.println("-------------------------------------------------");
            System.out.println("1. 회원 로그인 / 회원가입");
            System.out.println("2. 영화 예매");
            System.out.println("3. 결제");
            System.out.println("4. 내 예매 내역 조회");
            System.out.println("5. 예매 취소 및 환불");
            System.out.println("6. 상영관 및 스크린 조회");
            System.out.println("0. 종료");
            System.out.print("메뉴를 선택하세요: ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> memberMenu();
                case "2" -> runMemberReservationFlow();
                case "3" -> payForBooking();
                case "4" -> viewMyBookings();
                case "5" -> cancelBookingWithRefund();
                case "6" -> theaterScreenMenu();
                case "0" -> {
                    return;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 입력해 주세요.");
            }
        }
    }

    private static String describeCurrentUser() {
        if (currentMember != null) {
            return "[회원] ID=" + currentMember.getMemberId()
                    + ", 이메일=" + currentMember.getEmail();
        }
        return "로그인되지 않음";
    }

    // =====================================================================
    // 1) 회원 로그인 / 회원가입 메뉴
    // =====================================================================
    private static void memberMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 회원 메뉴 =====");
            System.out.println("1. 이메일로 로그인");
            System.out.println("2. 이메일 + 이름으로 로그인 (없으면 자동 회원가입)");
            System.out.println("3. 새 회원 가입");
            System.out.println("4. 로그아웃");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("이메일을 입력하세요: ");
                    String email = scanner.nextLine().trim();
                    Member m = memberService.login(email);
                    if (m == null) {
                        System.out.println("해당 이메일의 회원이 없습니다.");
                    } else {
                        currentMember = m;
                        System.out.println("로그인 성공! 회원 ID=" + m.getMemberId()
                                + ", 이름=" + m.getName());
                    }
                }
                case "2" -> {
                    System.out.print("이메일을 입력하세요: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("이름을 입력하세요: ");
                    String name = scanner.nextLine().trim();
                    Member m = memberService.loginOrRegister(email, name);
                    if (m != null) {
                        currentMember = m;
                        System.out.println("로그인/가입 완료! 회원 ID=" + m.getMemberId()
                                + ", 이름=" + m.getName());
                    } else {
                        System.out.println("로그인/가입에 실패했습니다.");
                    }
                }
                case "3" -> {
                    System.out.print("이메일을 입력하세요: ");
                    String email = scanner.nextLine().trim();
                    System.out.print("이름을 입력하세요: ");
                    String name = scanner.nextLine().trim();
                    Member m = memberService.registerMember(email, name);
                    if (m != null) {
                        System.out.println("회원 가입 완료! 회원 ID=" + m.getMemberId());
                    } else {
                        System.out.println("회원 가입에 실패했습니다.");
                    }
                }
                case "4" -> {
                    currentMember = null;
                    System.out.println("로그아웃되었습니다.");
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("잘못된 선택입니다.");
            }
        }
    }


    // =====================================================================
    // 2) 영화 예매
    // =====================================================================
    private static void runMemberReservationFlow() {
        if (currentMember == null) {
            System.out.println("먼저 회원으로 로그인해야 예매할 수 있습니다.");
            return;
        }

        Long memberId = currentMember.getMemberId();

        // 1. 영화 선택
        Integer movieId = selectMovie();
        if (movieId == null) {
            System.out.println("영화 선택이 취소되었습니다.");
            return;
        }

        // 2. 상영 선택
        Long showId = selectShowtime(movieId);
        if (showId == null) {
            System.out.println("상영 정보 선택이 취소되었습니다.");
            return;
        }

        // 3. 좌석 선택
        List<Long> seatIds = selectSeats(showId);
        if (seatIds.isEmpty()) {
            System.out.println("좌석을 선택하지 않아 예매를 중단합니다.");
            return;
        }

        // 4. 최종 예약 가능 여부 체크
        boolean allAvailable = seatService.areSeatsAllAvailable(showId, seatIds);
        if (!allAvailable) {
            System.out.println("[WARN] 선택한 좌석 중 이미 예약되었거나 홀드된 좌석이 있습니다.");
            System.out.println("다시 시도해 주세요.");
            return;
        }

        // 5. 예약 생성
        Booking booking = bookingService.createBookingForMember(memberId, showId, seatIds);

        if (booking == null) {
            System.out.println("[ERROR] 예약 생성에 실패했습니다.");
        } else {
            System.out.println();
            System.out.println("===== 예약 완료 =====");
            System.out.println("예약 ID      : " + booking.getBookingId());
            System.out.println("회원 ID      : " + booking.getMemberId());
            System.out.println("상영 ID      : " + booking.getShowId());
            System.out.println("총 결제 금액 : " + booking.getTotalAmount());
            System.out.println("상태         : " + booking.getStatus());
            System.out.println("생성 시각    : " + booking.getCreatedAt());
            System.out.println();
            System.out.println("※ 결제 메뉴에서 결제를 진행할 수 있습니다.");
        }
    }

    // =====================================================================
    // 3) 결제
    // =====================================================================
    private static void payForBooking() {
        if (currentMember == null) {
            System.out.println("먼저 회원으로 로그인해야 합니다.");
            return;
        }
        Long memberId = currentMember.getMemberId();

        System.out.print("결제할 예약 ID를 입력하세요: ");
        String line = scanner.nextLine().trim();
        long bookingId;
        try {
            bookingId = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력해 주세요.");
            return;
        }

        String selectSql =
                "SELECT booking_id, member_id, total_amount, status " +
                        "FROM booking WHERE booking_id = ? AND member_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

            pstmt.setLong(1, bookingId);
            pstmt.setLong(2, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("해당 ID의 예약이 없거나, 현재 회원의 예약이 아닙니다.");
                    return;
                }

                String status = rs.getString("status");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");

                if ("CANCELLED".equalsIgnoreCase(status)) {
                    System.out.println("이미 취소된 예약은 결제할 수 없습니다.");
                    return;
                }
                if ("CONFIRMED".equalsIgnoreCase(status)) {
                    System.out.println("이미 결제가 완료된 예약입니다.");
                    return;
                }

                System.out.println("예약 ID=" + bookingId + ", 결제 금액=" + totalAmount
                        + ", 현재 상태=" + status);
                System.out.println("결제 수단 ID를 입력하세요 (예: 1=카드, 2=계좌이체 등): ");
                String methodLine = scanner.nextLine().trim();
                long methodId;
                try {
                    methodId = Long.parseLong(methodLine);
                } catch (NumberFormatException e) {
                    System.out.println("결제 수단 ID는 숫자만 입력해 주세요.");
                    return;
                }

                // Payment.amount 가 int 이므로 원 단위 정수로 변환
                int amountInt = totalAmount.intValue();

                Payment payment = new Payment(bookingId, methodId, amountInt);
                payment.setStatus("SUCCESS");

                Payment inserted = paymentDao.insert(payment);
                System.out.println("결제 완료! paymentId=" + inserted.getPaymentId());

                // 결제 성공 후 booking 상태를 CONFIRMED로 변경
                String updateSql =
                        "UPDATE booking SET status = ?, updated_at = NOW() WHERE booking_id = ?";

                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, "CONFIRMED");
                    updateStmt.setLong(2, bookingId);
                    updateStmt.executeUpdate();
                }

                System.out.println("예약 상태가 CONFIRMED로 변경되었습니다.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] 결제 처리 중 DB 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================================
    // 4) 내 예매 내역 조회
    // =====================================================================
    private static void viewMyBookings() {
        if (currentMember == null) {
            System.out.println("먼저 회원으로 로그인해야 합니다.");
            return;
        }

        Long memberId = currentMember.getMemberId();

        String sql = "SELECT booking_id, show_id, status, total_amount, created_at " +
                "FROM booking WHERE member_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println();
                System.out.println("===== 내 예매 내역 =====");
                boolean hasAny = false;

                while (rs.next()) {
                    hasAny = true;
                    long bookingId = rs.getLong("booking_id");
                    long showId = rs.getLong("show_id");
                    String status = rs.getString("status");
                    BigDecimal amount = rs.getBigDecimal("total_amount");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    System.out.printf("예약ID=%d, 상영ID=%d, 상태=%s, 금액=%s, 생성시각=%s%n",
                            bookingId,
                            showId,
                            status,
                            amount,
                            createdAt != null ? createdAt.toLocalDateTime() : null);
                }

                if (!hasAny) {
                    System.out.println("예매 내역이 없습니다.");
                }
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] 예매 내역 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================================
    // 5) 예매 취소 및 환불 - 상영 20분 전까지 100% 환불
    // =====================================================================
    private static void cancelBookingWithRefund() {
        if (currentMember == null) {
            System.out.println("먼저 회원으로 로그인해야 합니다.");
            return;
        }
        Long memberId = currentMember.getMemberId();

        System.out.print("취소할 예약 ID를 입력하세요: ");
        String line = scanner.nextLine().trim();
        long bookingId;
        try {
            bookingId = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력해 주세요.");
            return;
        }

        String selectSql =
                "SELECT b.booking_id, b.member_id, b.show_id, b.status, b.total_amount, s.starts_at " +
                        "FROM booking b JOIN showtime s ON b.show_id = s.show_id " +
                        "WHERE b.booking_id = ? AND b.member_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setLong(1, bookingId);
                pstmt.setLong(2, memberId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("해당 ID의 예약이 없거나, 현재 회원의 예약이 아닙니다.");
                        conn.rollback();
                        return;
                    }

                    String status = rs.getString("status");
                    if ("CANCELLED".equalsIgnoreCase(status)) {
                        System.out.println("이미 취소된 예약입니다.");
                        conn.rollback();
                        return;
                    }

                    BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                    Timestamp tsStartsAt = rs.getTimestamp("starts_at");
                    LocalDateTime startsAt = tsStartsAt != null ? tsStartsAt.toLocalDateTime() : null;
                    LocalDateTime now = LocalDateTime.now();

                    BigDecimal refundAmount = BigDecimal.ZERO;
                    if (startsAt != null) {
                        LocalDateTime limit = startsAt.minusMinutes(20);
                        if (!now.isAfter(limit)) {
                            // 상영 20분 전까지는 100% 환불
                            refundAmount = totalAmount;
                        }
                    }

                    // 1) refund 레코드 생성
                    String insertRefund =
                            "INSERT INTO refund (booking_id, amount, reason) VALUES (?, ?, ?)";

                    try (PreparedStatement refundStmt = conn.prepareStatement(insertRefund)) {
                        refundStmt.setLong(1, bookingId);
                        refundStmt.setBigDecimal(2, refundAmount);
                        refundStmt.setString(3, "USER_CANCEL");
                        refundStmt.executeUpdate();
                    }

                    // 2) booking 상태 변경
                    String updateBooking =
                            "UPDATE booking SET status = ?, updated_at = NOW() WHERE booking_id = ?";

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateBooking)) {
                        updateStmt.setString(1, "CANCELLED");
                        updateStmt.setLong(2, bookingId);
                        updateStmt.executeUpdate();
                    }

                    conn.commit();

                    System.out.println();
                    System.out.println("===== 예매 취소 처리 완료 =====");
                    System.out.println("예약 ID      : " + bookingId);
                    System.out.println("환불 금액    : " + refundAmount);
                    System.out.println("취소 상태    : CANCELLED");
                }
            } catch (Exception ex) {
                conn.rollback();
                System.out.println("[ERROR] 예매 취소 처리 중 오류: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] 예매 취소 처리 중 DB 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================================================================
    // 6) 상영관 / 스크린 조회
    // =====================================================================
    private static void theaterScreenMenu() {
        System.out.println();
        System.out.println("===== 상영관 목록 =====");

        List<Theater> theaters = theaterService.getAllTheaters();
        if (theaters == null || theaters.isEmpty()) {
            System.out.println("등록된 상영관이 없습니다.");
            return;
        }

        for (Theater t : theaters) {
            System.out.printf("%d. %s (%s)%n",
                    t.getTheaterId(), t.getName(), t.getAddress());
        }

        System.out.print("상세 조회할 상영관 ID를 입력하세요 (0 입력 시 취소): ");
        String line = scanner.nextLine().trim();
        int theaterId;
        try {
            theaterId = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력해 주세요.");
            return;
        }
        if (theaterId == 0) {
            return;
        }

        Theater selected = theaterService.getTheater(theaterId);
        if (selected == null) {
            System.out.println("해당 ID의 상영관이 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("선택한 상영관: " + selected.getName()
                + " (" + selected.getAddress() + ")");
        System.out.println("===== 스크린 목록 =====");

        List<Screen> screens = screenService.getScreensByTheater(theaterId);
        if (screens == null || screens.isEmpty()) {
            System.out.println("등록된 스크린이 없습니다.");
            return;
        }

        for (Screen s : screens) {
            System.out.printf(" - 스크린 ID=%d, 이름=%s%n",
                    s.getScreenId(), s.getName());
        }
    }

    // =====================================================================
    // 보조 메서드들 (영화/상영/좌석 선택)
    // =====================================================================

    // 영화 목록 조회 + 선택
    private static Integer selectMovie() {
        List<Movie> movies = movieService.getAllMovies();

        if (movies == null || movies.isEmpty()) {
            System.out.println("등록된 영화가 없습니다.");
            return null;
        }

        System.out.println();
        System.out.println("===== 영화 목록 =====");
        for (Movie m : movies) {
            System.out.printf("%d. %s (상영시간: %d분, 관람등급: %d)%n",
                    m.getMovieId(),
                    m.getTitle(),
                    m.getDurationMin(),
                    m.getAgeRatingId());
        }

        Set<Integer> validIds = movies.stream()
                .map(Movie::getMovieId)
                .collect(Collectors.toSet());

        while (true) {
            System.out.print("예매할 영화 ID를 선택하세요 (0 입력 시 취소): ");
            String line = scanner.nextLine().trim();

            try {
                int selected = Integer.parseInt(line);
                if (selected == 0) {
                    return null;
                }
                if (validIds.contains(selected)) {
                    Movie selectedMovie = movies.stream()
                            .filter(m -> m.getMovieId() == selected)
                            .findFirst()
                            .orElse(null);
                    if (selectedMovie != null) {
                        System.out.println("선택한 영화: " + selectedMovie.getTitle());
                    }
                    return selected;
                } else {
                    System.out.println("목록에 없는 영화 ID입니다. 다시 입력해 주세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력해 주세요.");
            }
        }
    }

    // 상영 정보 목록 조회 + 선택
    private static Long selectShowtime(Integer movieId) {
        Long movieIdLong = movieId.longValue();

        List<Showtime> showtimes = showtimeService.getShowtimesByMovie(movieIdLong);

        if (showtimes == null || showtimes.isEmpty()) {
            System.out.println("선택한 영화에 대한 상영 정보가 없습니다.");
            return null;
        }

        System.out.println();
        System.out.println("===== 상영 정보 목록 =====");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Showtime s : showtimes) {
            LocalDateTime startsAt = s.getStartsAt();
            LocalDateTime endsAt = s.getEndsAt();

            String starts = (startsAt != null) ? startsAt.format(formatter) : "시작 시간 없음";
            String ends = (endsAt != null) ? endsAt.format(formatter) : "종료 시간 없음";

            System.out.printf("%d. 상영관ID=%d, 시작=%s, 종료=%s%n",
                    s.getShowId(),
                    s.getScreenId(),
                    starts,
                    ends);
        }

        Set<Long> validIds = showtimes.stream()
                .map(Showtime::getShowId)
                .collect(Collectors.toSet());

        while (true) {
            System.out.print("예매할 상영 ID를 선택하세요 (0 입력 시 취소): ");
            String line = scanner.nextLine().trim();

            try {
                long selected = Long.parseLong(line);
                if (selected == 0L) {
                    return null;
                }
                if (validIds.contains(selected)) {
                    System.out.println("선택한 상영 ID: " + selected);
                    return selected;
                } else {
                    System.out.println("목록에 없는 상영 ID입니다. 다시 입력해 주세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력해 주세요.");
            }
        }
    }

    // 좌석 선택
    private static List<Long> selectSeats(Long showId) {

        List<Seat> seats = seatService.getSeatsWithStatus(showId);

        if (seats == null || seats.isEmpty()) {
            System.out.println("해당 상영에 대한 좌석 정보가 없습니다.");
            return List.of();
        }

        System.out.println();
        System.out.println("===== 좌석 현황 =====");
        printSeatLayout(seats);

        System.out.println("예약 가능한 좌석의 seat_id를 콤마(,)로 구분해서 입력하세요.");
        System.out.println("예: 1,2,3 (0 입력 시 취소)");

        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("0")) {
                return List.of();
            }

            String[] parts = line.split(",");
            List<Long> seatIds = new ArrayList<>();
            boolean valid = true;

            for (String part : parts) {
                String token = part.trim();
                if (token.isEmpty()) continue;

                Long seatId;
                try {
                    seatId = Long.parseLong(token);
                } catch (NumberFormatException e) {
                    System.out.println("좌석 ID는 숫자만 입력해 주세요: " + token);
                    valid = false;
                    break;
                }

                // 존재하는 seat인지 + 상태가 AVAILABLE인지 확인
                Seat seat = seats.stream()
                        .filter(s -> s.getSeatId().equals(seatId))
                        .findFirst()
                        .orElse(null);

                if (seat == null) {
                    System.out.println("해당 상영에 존재하지 않는 좌석입니다: seat_id=" + seatId);
                    valid = false;
                    break;
                }
                if (seat.getStatus() != null &&
                        seat.getStatus() != SeatStatus.AVAILABLE) {
                    System.out.println("이미 예약/홀드된 좌석입니다: seat_id=" +
                            seatId + ", 상태=" + seat.getStatus());
                    valid = false;
                    break;
                }

                seatIds.add(seatId);
            }

            if (!valid || seatIds.isEmpty()) {
                System.out.println("좌석 입력이 잘못되었습니다. 다시 입력해 주세요. (0 입력 시 취소)");
                continue;
            }

            System.out.println("선택한 좌석 ID 목록: " + seatIds);
            return seatIds;
        }
    }

    // 좌석 배치 출력 (행(row_label) 기준으로 줄 나누고, 상태 표시)
    private static void printSeatLayout(List<Seat> seats) {
        // row_label, col_number 기준으로 정렬
        seats = seats.stream()
                .sorted(Comparator
                        .comparing(Seat::getRowLabel)
                        .thenComparingInt(Seat::getColNumber))
                .collect(Collectors.toList());

        String currentRow = null;
        for (Seat seat : seats) {
            if (currentRow == null || !currentRow.equals(seat.getRowLabel())) {
                // 새 행 시작
                currentRow = seat.getRowLabel();
                System.out.println();
                System.out.print(currentRow + "행: ");
            }

            String statusMark;
            if (seat.getStatus() == SeatStatus.BOOKED) {
                statusMark = "[X]";      // 예약됨
            } else if (seat.getStatus() == SeatStatus.HOLD) {
                statusMark = "[H]";      // 홀드
            } else {
                statusMark = "[ ]";      // 예약 가능
            }

            // seat_id와 상태를 함께 출력 (예: 1[ ], 2[X])
            System.out.print(seat.getSeatId() + statusMark + " ");
        }
        System.out.println();
        System.out.println("\n[X]=예약됨, [H]=홀드, [ ]=예약 가능");
    }
}
