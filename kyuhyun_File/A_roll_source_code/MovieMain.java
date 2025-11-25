package com.example;

import com.example.dao.MovieDao;
import com.example.model.Movie;

import java.util.List;

public class MovieMain {
    public static void main(String[] args) {
        MovieDao movieDao = new MovieDao();
        List<Movie> movies = movieDao.findAll();

        System.out.println("=== 영화 목록 출력 ===");
        for (Movie m : movies) {
            System.out.println(
                    m.getMovieId() + " | " +
                            m.getTitle() + " | " +
                            m.getDistributorName() + " | " +
                            m.getReleasedOn()  // ✔ 여기 수정됨
            );
        }
    }
}
