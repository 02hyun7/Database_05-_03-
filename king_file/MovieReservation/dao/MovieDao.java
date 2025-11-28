package org.example.dao;

import org.example.model.Movie;
import org.example.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDao {


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

        try (Connection conn = DBUtil.getConnection();
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

        try (Connection conn = DBUtil.getConnection();
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
