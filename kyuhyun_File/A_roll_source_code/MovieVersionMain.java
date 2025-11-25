package com.example;

import com.example.dao.MovieVersionDao;
import com.example.model.MovieVersion;

import java.util.List;

public class MovieVersionMain {

    public static void main(String[] args) {

        MovieVersionDao dao = new MovieVersionDao();

        // 1) 전체 조회
        System.out.println("=== 전체 movie_version 조회 ===");
        List<MovieVersion> all = dao.findAll();
        for (MovieVersion mv : all) {
            System.out.println(
                    mv.getVersionId() + " | " +
                            mv.getMovieId() + " | " +
                            mv.getFormat() + " | " +
                            mv.getAudioLangId() + " | " +
                            mv.getSubtitleId()
            );
        }

        // 2) 특정 영화 version 조회
        System.out.println("\n=== movie_id=1 버전 조회 ===");
        List<MovieVersion> list = dao.findByMovie(1);
        for (MovieVersion mv : list) {
            System.out.println(
                    mv.getVersionId() + " | " +
                            mv.getFormat()
            );
        }
    }
}
