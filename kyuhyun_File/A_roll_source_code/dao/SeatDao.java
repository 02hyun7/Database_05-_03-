package com.example.dao;

import com.example.model.Seat;
import com.example.model.SeatStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    /**
     * show_id만 가지고 좌석 + 좌석 상태 조회
     * 1) showtime에서 screen_id 찾기
     * 2) screen_layout에서 해당 screen의 최신 layout_id 찾기
     * 3) seat + booking_seat + hold_seat 조인해서 상태 계산
     */
    public List<Seat> findSeatsByShow(int showId) {
        List<Seat> seats = new ArrayList<>();

        Integer layoutId = getLayoutIdForShow(showId);
        if (layoutId == null) {
            System.out.println("좌석 배치(layout)가 존재하지 않습니다. show_id=" + showId);
            return seats;
        }

        String sql = """
            SELECT
                s.seat_id,
                s.layout_id,
                s.row_label,
                s.col_number,
                s.seat_type_id,
                b.seat_id AS booked_seat,
                h.seat_id AS hold_seat
            FROM seat s
            LEFT JOIN booking_seat b
                   ON s.seat_id = b.seat_id
                  AND b.show_id = ?
            LEFT JOIN hold_seat h
                   ON s.seat_id = h.seat_id
                  AND h.show_id = ?
                  AND h.expires_at > NOW()   -- 만료된 hold는 무시
            WHERE s.layout_id = ?
            ORDER BY s.row_label, s.col_number
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, showId);
            pstmt.setInt(2, showId);
            pstmt.setInt(3, layoutId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setLayoutId(rs.getInt("layout_id"));
                seat.setRowLabel(rs.getString("row_label"));
                seat.setColNumber(rs.getInt("col_number"));
                seat.setSeatTypeId(rs.getInt("seat_type_id"));

                // 상태 계산: BOOKED > HOLD > AVAILABLE
                if (rs.getObject("booked_seat") != null) {
                    seat.setStatus(SeatStatus.BOOKED);
                } else if (rs.getObject("hold_seat") != null) {
                    seat.setStatus(SeatStatus.HOLD);
                } else {
                    seat.setStatus(SeatStatus.AVAILABLE);
                }

                seats.add(seat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }

    /**
     * show_id -> screen_id -> 최신 layout_id 조회
     * screen_layout에서 created_at이 가장 최신인 layout을 사용
     */
    private Integer getLayoutIdForShow(int showId) {

        String sql = """
            SELECT sl.layout_id
            FROM showtime s
            JOIN screen_layout sl
              ON s.screen_id = sl.screen_id
            WHERE s.show_id = ?
            ORDER BY sl.created_at DESC
            LIMIT 1
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, showId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("layout_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // 못 찾으면 null
    }
}
