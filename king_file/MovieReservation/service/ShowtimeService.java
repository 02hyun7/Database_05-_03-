package org.example.service;

import org.example.dao.ShowtimeDao;
import org.example.model.Showtime;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ShowtimeService {

    private final ShowtimeDao showtimeDao = new ShowtimeDao();

    /** 특정 영화의 상영 정보 목록 조회 */
    public List<Showtime> getShowtimesByMovie(Long movieId) {
        try {
            return showtimeDao.findByMovieId(movieId);
        } catch (SQLException e) {
            System.out.println("[ERROR] 상영 정보 목록 조회 실패: movieId=" + movieId
                    + ", message=" + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /** show_id로 단일 상영 정보 조회 */
    public Showtime getShowtime(Long showId) {
        try {
            return showtimeDao.findById(showId);
        } catch (SQLException e) {
            System.out.println("[ERROR] 상영 정보 단건 조회 실패: showId=" + showId
                    + ", message=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}