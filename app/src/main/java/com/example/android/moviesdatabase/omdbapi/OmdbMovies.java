package com.example.android.moviesdatabase.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Emad on 25/01/2018.
 */

public class OmdbMovies {

    @JsonProperty("Title")
    private String movieTitle;

    @JsonProperty("Year")
    private String releaseYear;

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Poster")
    private String poster;

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }
}
