package com.example.dao;

import com.example.model.Movie;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDao {

    private final String url = "jdbc:mysql://localhost:3306/movie?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";
    private final String user = "root";
    private final String password = "sense??3?88160";

    // === 1) 영화 전체 목록 조회 ===
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();

        String sql = """
            SELECT 
                m.movie_id,
                m.title,
                m.duration_min,
                m.released_on,
                d.name AS distributor_name,
                m.age_rating_id
            FROM movie m
            LEFT JOIN distributor d ON m.distributor_id = d.distributor_id
            LIMIT 100;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setDurationMin(rs.getInt("duration_min"));

                Date date = rs.getDate("released_on");
                if (date != null) {
                    movie.setReleasedOn(date.toLocalDate());
                }

                movie.setDistributorName(rs.getString("distributor_name"));
                movie.setAgeRatingId(rs.getInt("age_rating_id"));

                movies.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    // === 2) 특정 영화 한 건 조회 (movie_id) ===
    public Movie findById(int movieId) {

        String sql = """
            SELECT 
                m.movie_id,
                m.title,
                m.duration_min,
                m.released_on,
                d.name AS distributor_name,
                m.age_rating_id
            FROM movie m
            LEFT JOIN distributor d ON m.distributor_id = d.distributor_id
            WHERE m.movie_id = ?
            LIMIT 1;
            """;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setDurationMin(rs.getInt("duration_min"));

                Date date = rs.getDate("released_on");
                if (date != null) {
                    movie.setReleasedOn(date.toLocalDate());
                }

                movie.setDistributorName(rs.getString("distributor_name"));
                movie.setAgeRatingId(rs.getInt("age_rating_id"));

                return movie;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 못 찾았으면 null
        return null;
    }
}
