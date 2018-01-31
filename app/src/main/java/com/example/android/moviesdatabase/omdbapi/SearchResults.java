package com.example.android.moviesdatabase.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Emad on 30/01/2018.
 */

public class SearchResults {

    @JsonProperty("Search")
    private List<OmdbMovies> movies;

    @JsonProperty("totalResults")
    private String totalResults;

    @JsonProperty("Response")
    private boolean response;

    public List<OmdbMovies> getMovies() {
        return movies;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public boolean isResponse() {
        return response;
    }
}
