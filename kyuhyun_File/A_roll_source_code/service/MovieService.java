package com.example.service;

import com.example.dao.MovieDao;
import com.example.model.Movie;

import java.util.List;

public class MovieService {

    private final MovieDao movieDao = new MovieDao();

    /**
     * 영화 전체 목록 조회
     */
    public List<Movie> getAllMovies() {
        return movieDao.findAll();
    }

    /**
     * 특정 영화 조회 (movie_id)
     */
    public Movie getMovieById(int movieId) {
        return movieDao.findById(movieId);
    }

}
