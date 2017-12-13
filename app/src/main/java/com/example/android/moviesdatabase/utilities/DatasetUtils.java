package com.example.android.moviesdatabase.utilities;

/**
 * Created by Emad on 12/12/2017.
 */

public class DatasetUtils {

    private String title;
    private String poster;
    private String year;
    private String id;

    public DatasetUtils(String movieTitle, String posterUrl, String year, String imdbId) {
        this.title = movieTitle;
        this.poster = posterUrl;
        this.year = year;
        this.id = imdbId;
    }

    public String getMovieTitle() {
        return title;
    }

    public String getMoviePoster() {
        return poster;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return id;
    }
}
