package com.example.android.moviesdatabase.omdbapi;

import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emad on 30/01/2018.
 */

public class ApiUrl {

    private static final String OMDB_API_BASE = "http://www.omdbapi.com/";

    private final Map<String, String> params = new HashMap<>();

    private final OmdbApi omdbApi;

    public ApiUrl(OmdbApi omdbApi) {
        this.omdbApi = omdbApi;
    }

    public URL buildUrl() {
        Uri urlBuilder = Uri.parse(OMDB_API_BASE);
        Uri.Builder builder = urlBuilder.buildUpon();

        try {
            if (params.size() > 0) {
                List<String> keys = new ArrayList<String>(params.keySet());
                for (int i = 0; i < keys.size(); i++) {
                    String paramName = keys.get(i);
                    builder.appendQueryParameter(paramName, params.get(paramName));
                }
                builder.build();
            }

            return new URL(builder.toString());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void addParam(String name, String value) {
        if (params.containsKey(name)) {
            throw new RuntimeException("paramater '" + name + "' already defined");
        }

        name = name.trim();
        if (name.isEmpty()) {
            throw new RuntimeException("parameter name can not be empty");
        }

        value = value.trim();
        if (value.isEmpty()) {
            throw new RuntimeException("value of parameter '" + name + "' can not be empty");
        }

        params.put(name, value);
    }
}
