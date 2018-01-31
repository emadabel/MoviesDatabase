package com.example.android.moviesdatabase.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.List;

/**
 * Created by Emad on 24/01/2018.
 */

public class MovieDb extends OmdbMovies {

    @JsonProperty("Rated")
    private String rate;

    @JsonProperty("Released")
    private String releaseDate;

    @JsonProperty("Runtime")
    private String runTime;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Director")
    private String director;

    @JsonProperty("Writer")
    private String writer;

    @JsonProperty("Actors")
    private String actors;

    @JsonProperty("Plot")
    private String plot;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Awards")
    private String awards;

    @JsonProperty("Ratings")
    private List<Ratings> ratings;

    @JsonProperty("Metascore")
    private int metascore;

    @JsonProperty("imdbRating")
    private float imdbRating;

    @JsonProperty("imdbVotes")
    private String imdbVotes;

    @JsonProperty("DVD")
    private String dvdReleaseDate;

    @JsonProperty("BoxOffice")
    private String boxOffice;

    @JsonProperty("Production")
    private String production;

    @JsonProperty("Website")
    private URL website;

    @JsonProperty("Response")
    private boolean response;

    public String getRate() {
        return rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public List<Ratings> getRatings() {
        return ratings;
    }

    public int getMetascore() {
        return metascore;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getDvdReleaseDate() {
        return dvdReleaseDate;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getProduction() {
        return production;
    }

    public URL getWebsite() {
        return website;
    }

    public boolean isResponse() {
        return response;
    }
}
