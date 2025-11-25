package com.example.model;

public class MovieVersion {

    private int versionId;
    private int movieId;
    private String format;
    private int audioLangId;
    private int subtitleId;

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getAudioLangId() {
        return audioLangId;
    }

    public void setAudioLangId(int audioLangId) {
        this.audioLangId = audioLangId;
    }

    public int getSubtitleId() {
        return subtitleId;
    }

    public void setSubtitleId(int subtitleId) {
        this.subtitleId = subtitleId;
    }
}
