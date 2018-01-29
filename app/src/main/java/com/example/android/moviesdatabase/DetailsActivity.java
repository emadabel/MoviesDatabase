package com.example.android.moviesdatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    private ProgressBar mLoadingProgressBar;
    private View mDetailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String extraMovieId = getIntent().getStringExtra("EXTRA_MOVIE_ID");

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mReleaseYearTextView = (TextView) findViewById(R.id.tv_movie_year);
        mMovieInfoTextView = (TextView) findViewById(R.id.tv_movie_info);
        mPlotTextView = (TextView) findViewById(R.id.tv_plot);
        mImdbRatingTextView = (TextView) findViewById(R.id.tv_imdb_rating);
        mImdbVotesTextView = (TextView) findViewById(R.id.tv_imdb_votes);
        mMetascoreTextView = (TextView) findViewById(R.id.tv_metascore);
        mMovieCastTextView = (TextView) findViewById(R.id.tv_movie_cast);

        mMoviePic = (ImageView) findViewById(R.id.iv_movie_pic);

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading_details);
        mDetailsContainer = findViewById(R.id.details_container);

        new FetchMovieData().execute(extraMovieId);
    }

    public int getMetaBgColor(Context context, String metascore) {

        if (metascore.isEmpty() || metascore.equalsIgnoreCase("N/A")) {
            return ContextCompat.getColor(context, R.color.metaNoScore);
        }

        int score = Integer.valueOf(metascore);
        if (score <= 39) {
            return ContextCompat.getColor(context, R.color.meta0to3Score);
        } else if (score <= 60 && score >= 40) {
            return ContextCompat.getColor(context, R.color.meta4to6Score);
        } else if (score >= 61) {
            return ContextCompat.getColor(context, R.color.meta7to10Score);
        } else {
            return ContextCompat.getColor(context, R.color.metaNoScore);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class FetchMovieData extends AsyncTask<String, Void, DatasetUtils> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
            mDetailsContainer.setVisibility(View.INVISIBLE);
        }

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

                return JsonUtils.getMovieDataFromJson(jsonOmdbResponse);

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
                mMovieCastTextView.setText("Director: " + movieData.getDirector() + "\n" + "Writers: " + movieData.getWriter() + "\n" + "Actors: " + movieData.getActors());

                String metascore = movieData.getMetascore();
                int bgColorForMetascore = getMetaBgColor(DetailsActivity.this, metascore);

                mMetascoreTextView.setText(metascore);
                mMetascoreTextView.setBackgroundColor(bgColorForMetascore);

                Picasso.with(DetailsActivity.this).load(movieData.getMoviePoster())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(mMoviePic);
            }
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            mDetailsContainer.setVisibility(View.VISIBLE);
        }
    }
}
