package com.example.android.moviesdatabase.omdbapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Emad on 25/01/2018.
 */

public class OmdbApi {

    public static final String PARAM_API_KEY = "apikey";
    public static final String PARAM_ID = "i";
    public static final String PARAM_TITLE = "t";
    public static final String PARAM_SEARCH = "s";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_RELEASE_YEAR = "y";
    public static final String PARAM_PLOT = "plot";
    public static final String PARAM_FORMAT = "r";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_CALLBACK = "callback";
    public static final String PARAM_VERSION = "v";
    static final ObjectMapper jsonMapper = new ObjectMapper();
    private String apiKey;

    public OmdbApi(String apiKey) {
        this.apiKey = apiKey;
    }

    public SearchResults getMovies(String title) {
        ApiUrl apiUrl = new ApiUrl(this);

        apiUrl.addParam(PARAM_API_KEY, getApiKey());
        apiUrl.addParam(PARAM_SEARCH, title);

        URL url = apiUrl.buildUrl();
        return mapJsonResult(url, SearchResults.class);
    }

    public MovieDb getMovieData(String imdbId) {
        ApiUrl apiUrl = new ApiUrl(this);

        apiUrl.addParam(PARAM_API_KEY, getApiKey());
        apiUrl.addParam(PARAM_ID, imdbId);

        URL url = apiUrl.buildUrl();
        return mapJsonResult(url, MovieDb.class);
    }

    /**
     * Get the API key that is to be used by this instance
     */
    public String getApiKey() {
        return apiKey;
    }

    public <T> T mapJsonResult(URL apiUrl, Class<T> someClass) {
        try {
            return jsonMapper.readValue(apiUrl, someClass);
        } catch (IOException ex) {
            throw new RuntimeException("mapping failed:\n" + apiUrl.toString());
        }
    }
}
