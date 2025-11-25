package com.example;

import com.example.model.Movie;
import com.example.model.Theater;
import com.example.model.Screen;
import com.example.model.Showtime;
import com.example.model.Seat;
import com.example.service.*;

import java.util.List;

public class ServiceAll {

    public static void main(String[] args) {

        MovieService movieService = new MovieService();
        TheaterService theaterService = new TheaterService();
        ScreenService screenService = new ScreenService();
        ShowtimeService showtimeService = new ShowtimeService();
        SeatService seatService = new SeatService();
        PriceService priceService = new PriceService();

        System.out.println("====================================");
        System.out.println("🎬 영화 목록 조회");
        System.out.println("====================================");
        List<Movie> movies = movieService.getAllMovies();
        for (Movie m : movies) {
            System.out.println(m.getMovieId() + " | " + m.getTitle());
        }

        System.out.println("\n====================================");
        System.out.println("🏢 영화관 목록 조회");
        System.out.println("====================================");
        List<Theater> theaters = theaterService.getAllTheaters();
        for (Theater t : theaters) {
            System.out.println(t.getTheaterId() + " | " + t.getName() + " | " + t.getAddress());
        }

        System.out.println("\n====================================");
        System.out.println("🏟 상영관 목록 조회 (theater_id = 1 기준)");
        System.out.println("====================================");
        List<Screen> screens = screenService.getScreensByTheater(1);
        for (Screen s : screens) {
            System.out.println(s.getScreenId() + " | " + s.getName());
        }

        System.out.println("\n====================================");
        System.out.println("⏱ 상영정보 조회 (movie_id = 1 기준)");
        System.out.println("====================================");
        List<Showtime> showtimes = showtimeService.getShowtimesByMovie(1);
        for (Showtime st : showtimes) {
            System.out.println(st.getShowId() + " | " + st.getStartsAt());
        }

        System.out.println("\n====================================");
        System.out.println("🪑 좌석 조회 + 상태 계산 (show_id = 1 기준)");
        System.out.println("====================================");
        List<Seat> seats = seatService.getSeatsWithStatus(1);
        for (Seat s : seats) {
            System.out.println(s.getRowLabel() + s.getColNumber() + " = " + s.getStatus());
        }

        System.out.println("\n====================================");
        System.out.println("💰 가격 조회 (show_id = 1, seat_type_id = 1 기준)");
        System.out.println("====================================");
        int price = priceService.getFinalPrice(1, 1);
        System.out.println("최종 가격: " + price);

        System.out.println("\n====================================");
        System.out.println("✅ A 역할 전체 기능 테스트 완료");
        System.out.println("====================================");
    }
}
