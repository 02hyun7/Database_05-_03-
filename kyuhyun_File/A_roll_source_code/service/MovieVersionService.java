package com.example.service;

import com.example.dao.MovieVersionDao;
import com.example.model.MovieVersion;

import java.util.List;

public class MovieVersionService {

    private final MovieVersionDao dao = new MovieVersionDao();

    public List<MovieVersion> getVersionsByMovie(int movieId) {
        return dao.findByMovie(movieId);
    }

    public List<MovieVersion> getVersionsByFormat(String format) {
        return dao.findByFormat(format);
    }
}
