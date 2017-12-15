package com.example.android.moviesdatabase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesdatabase.utilities.DatasetUtils;
import com.example.android.moviesdatabase.utilities.JsonUtils;
import com.example.android.moviesdatabase.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by Emad on 13/12/2017.
 */

public class DetailsActivity extends AppCompatActivity {

    private TextView mMovieTitleTextView;
    private TextView mReleaseYearTextView;
    private TextView mMovieInfoTextView;
    private TextView mPlotTextView;
    private TextView mImdbRatingTextView;
    private TextView mImdbVotesTextView;
    private TextView mMetascoreTextView;
    private TextView mMovieCastTextView;

    private ImageView mMoviePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String extraMovieId = getIntent().getStringExtra("EXTRA_MOVIE_ID");

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseYearTextView = (TextView) findViewById(R.id.tv_movie_year);
        mMovieInfoTextView = (TextView) findViewById(R.id.tv_movie_info);
        mPlotTextView = (TextView) findViewById(R.id.tv_polt);
        mImdbRatingTextView = (TextView) findViewById(R.id.tv_imdb_rating);
        mImdbVotesTextView = (TextView) findViewById(R.id.tv_imdb_votes);
        mMetascoreTextView = (TextView) findViewById(R.id.tv_metascore);
        mMovieCastTextView = (TextView) findViewById(R.id.tv_movie_cast);

        mMoviePic = (ImageView) findViewById(R.id.iv_movie_pic);

        new FetchMovieData().execute(extraMovieId);
    }

    public class FetchMovieData extends AsyncTask<String, Void, DatasetUtils> {

        @Override
        protected DatasetUtils doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            String query = strings[0];
            URL movieSearchUrl = NetworkUtils.buildUrl(DetailsActivity.this, query, NetworkUtils.SearchType.BY_ID);

            try {
                String jsonOmdbResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieSearchUrl);

                DatasetUtils moviesObject = JsonUtils
                        .getMovieDataFromJson(jsonOmdbResponse);

                return moviesObject;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(DatasetUtils movieData) {
            if (movieData != null) {
                mMovieTitleTextView.setText(movieData.getMovieTitle());
                mReleaseYearTextView.setText("(" + movieData.getYear() + ")");
                mMovieInfoTextView.setText(movieData.getRated() + " | " + movieData.getRuntime() + " | " +
                movieData.getGenre());
                mPlotTextView.setText(movieData.getPlot());
                mImdbRatingTextView.setText(movieData.getImdbRating());
                mImdbVotesTextView.setText(movieData.getImdbVotes());
                mMetascoreTextView.setText(movieData.getMetascore());
                mMovieCastTextView.setText("Director: " + movieData.getDirector() + "\n" + "Writers: " + movieData.getWriter() + "\n" + "Actors: " + movieData.getActors());

                Picasso.with(DetailsActivity.this).load(movieData.getMoviePoster())
                        .into(mMoviePic);
            }
        }
    }
}