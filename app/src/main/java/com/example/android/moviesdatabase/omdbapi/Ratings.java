package com.example.android.moviesdatabase.omdbapi;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Emad on 30/01/2018.
 */

public class Ratings {

    @JsonProperty("Source")
    private String source;

    @JsonProperty("Value")
    private String value;

    public String getSource() {
        return source;
    }

    public String getValue() {
        return value;
    }
}
