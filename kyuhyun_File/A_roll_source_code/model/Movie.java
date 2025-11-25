package com.example.model;

import java.time.LocalDate;

public class Movie {
    private int movieId;
    private String title;
    private int durationMin;
    private LocalDate releasedOn;
    private String distributorName;   // JOIN 결과로 채움
    private int ageRatingId;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public LocalDate getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(LocalDate releasedOn) {
        this.releasedOn = releasedOn;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public int getAgeRatingId() {
        return ageRatingId;
    }

    public void setAgeRatingId(int ageRatingId) {
        this.ageRatingId = ageRatingId;
    }
}
