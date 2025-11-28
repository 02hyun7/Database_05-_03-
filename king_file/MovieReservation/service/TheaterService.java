package org.example.service;

import org.example.dao.TheaterDao;
import org.example.model.Theater;

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
