package com.example.service;

import com.example.dao.TheaterDao;
import com.example.model.Theater;

import java.util.List;

public class TheaterService {

    private final TheaterDao dao = new TheaterDao();

    public List<Theater> getAllTheaters() {
        return dao.findAll();
    }

    public Theater getTheater(int theaterId) {
        return dao.findById(theaterId);
    }
}
