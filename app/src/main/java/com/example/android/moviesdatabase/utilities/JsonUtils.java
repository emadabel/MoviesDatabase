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

    public static DatasetUtils getMovieDataFromJson(String omdbJsonStr)
        throws JSONException {

        final String OMDB_TITLE = "Title";
        final String OMDB_YEAR = "Year";
        final String OMDB_RATED = "Rated";
        final String OMDB_RUNTIME = "Runtime";
        final String OMDB_GENRE = "Genre";
        final String OMDB_DIRECTOR = "Director";
        final String OMDB_WRITER = "Writer";
        final String OMDB_ACTORS = "Actors";
        final String OMDB_PLOT = "Plot";
        final String OMDB_POSTER = "Poster";
        final String OMDB_METASCORE = "Metascore";
        final String OMDB_RATING = "imdbRating";
        final String OMDB_VOTES = "imdbVotes";

        DatasetUtils datasetItem;

        JSONObject movieObject = new JSONObject(omdbJsonStr);
        String title = movieObject.getString(OMDB_TITLE);
        String year = movieObject.getString(OMDB_YEAR);
        String rated = movieObject.getString(OMDB_RATED);
        String runtime = movieObject.getString(OMDB_RUNTIME);
        String genre = movieObject.getString(OMDB_GENRE);
        String director = movieObject.getString(OMDB_DIRECTOR);
        String writer = movieObject.getString(OMDB_WRITER);
        String actors = movieObject.getString(OMDB_ACTORS);
        String plot = movieObject.getString(OMDB_PLOT);
        String poster = movieObject.getString(OMDB_POSTER);
        String metascore = movieObject.getString(OMDB_METASCORE);
        String rating = movieObject.getString(OMDB_RATING);
        String votes = movieObject.getString(OMDB_VOTES);

        datasetItem = new DatasetUtils(title, poster, year, rated, runtime,
                genre, director, writer, actors, plot, metascore, rating, votes);

        return datasetItem;
    }
}
