package com.example.android.moviesdatabase.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.moviesdatabase.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Emad on 12/12/2017.
 */

public final class NetworkUtils {

    final static String BY_ID_PARAM = "i";
    final static String BY_TITLE_PARAM = "t";
    final static String BY_SEARCH_PARAM = "s";
    final static String RELEASE_YEAR_PARAM = "y";
    final static String PLOT_PARAM = "plot";
    final static String FORMAT_PARAM = "r";

    final static String API_KEY = "apikey";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String OMDB_BASE_URL = "http://www.omdbapi.com/";
    private static final String format = "json";
    private static final String plotShort = "short";
    private static final String plotFull = "full";

    public static URL buildUrl(Context context, String movieQuery, SearchType searchType) {
        String apiKey = BuildConfig.OMDB_API_KEY;
        Uri builtUri = null;

        switch (searchType) {
            case BY_SEARCH:
                builtUri = Uri.parse(OMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(BY_SEARCH_PARAM, movieQuery)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(API_KEY, apiKey)
                        .build();
                break;
            case BY_ID:
                builtUri = Uri.parse(OMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(BY_ID_PARAM, movieQuery)
                        .appendQueryParameter(PLOT_PARAM, plotShort)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(API_KEY, apiKey)
                        .build();
                break;
            default:
                builtUri = null;
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public enum SearchType {BY_ID, BY_SEARCH}
}
