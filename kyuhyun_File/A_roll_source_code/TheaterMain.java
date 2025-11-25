package com.example;

import com.example.dao.ScreenDao;
import com.example.dao.TheaterDao;
import com.example.model.Screen;
import com.example.model.Theater;

import java.util.List;

public class TheaterMain {
    public static void main(String[] args) {

        TheaterDao theaterDao = new TheaterDao();
        ScreenDao screenDao = new ScreenDao();

        System.out.println("=== 영화관 목록 ===");
        List<Theater> theaters = theaterDao.findAll();
        for (Theater t : theaters) {
            System.out.println(t.getTheaterId() + " | " + t.getName() + " | " + t.getAddress());
        }

        System.out.println("\n=== 첫 번째 영화관의 상영관 목록 ===");
        if (!theaters.isEmpty()) {
            int firstTheaterId = theaters.get(0).getTheaterId();
            List<Screen> screens = screenDao.findByTheater(firstTheaterId);

            for (Screen s : screens) {
                System.out.println(s.getScreenId() + " | " + s.getName());
            }
        }
    }
}
