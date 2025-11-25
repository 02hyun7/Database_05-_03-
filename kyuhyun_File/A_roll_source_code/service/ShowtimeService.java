package com.example.service;

import com.example.dao.ShowtimeDao;
import com.example.model.Showtime;

import java.util.List;

public class ShowtimeService {

    private final ShowtimeDao dao = new ShowtimeDao();

    public List<Showtime> getShowtimesByMovie(int movieId) {
        return dao.findByMovie(movieId);
    }

    public Showtime getShowtime(int showId) {
        return dao.findById(showId);
    }
}
