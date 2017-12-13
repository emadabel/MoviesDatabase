package com.example.android.moviesdatabase.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Emad on 12/12/2017.
 */

public final class JsonUtils {

    public static ArrayList<DatasetUtils> getOmdbDataFromJson(String omdbJsonStr)
            throws JSONException {

        final String OMDB_SEARCH = "Search";
        final String OMDB_TITLE = "Title";
        final String OMDB_YEAR = "Year";
        final String OMDB_ID = "imdbID";
        final String OMDB_POSTER = "Poster";

        ArrayList<DatasetUtils> datasetList = new ArrayList<>();

        JSONObject omdbJson = new JSONObject(omdbJsonStr);
        JSONArray searchArray = omdbJson.getJSONArray(OMDB_SEARCH);

        for (int i = 0; i < searchArray.length(); i++) {
            String title;
            String year;
            String imdbId;
            String poster;
            DatasetUtils dataItem;

            JSONObject movieObject = searchArray.getJSONObject(i);
            title = movieObject.getString(OMDB_TITLE);
            year = movieObject.getString(OMDB_YEAR);
            imdbId = movieObject.getString(OMDB_ID);
            poster = movieObject.getString(OMDB_POSTER);

            dataItem = new DatasetUtils(title, poster, year, imdbId);

            datasetList.add(dataItem);
        }

        return datasetList;
    }
}
