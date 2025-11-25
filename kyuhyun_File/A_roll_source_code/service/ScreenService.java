package com.example.service;

import com.example.dao.ScreenDao;
import com.example.model.Screen;

import java.util.List;

public class ScreenService {

    private final ScreenDao dao = new ScreenDao();

    public List<Screen> getScreensByTheater(int theaterId) {
        return dao.findByTheater(theaterId);
    }

    public Screen getScreen(int screenId) {
        return dao.findById(screenId);
    }
}
