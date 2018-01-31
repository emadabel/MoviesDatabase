package com.example.android.moviesdatabase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.moviesdatabase.omdbapi.MovieDb;
import com.squareup.picasso.Picasso;

import static com.example.android.moviesdatabase.MainActivity.omdbApi;

/**
 * Created by Emad on 13/12/2017.
 */

public class DetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<MovieDb> {

    private static final String SEARCH_QUERY_EXTRA = "query";
    private static final int MOVIES_LOADER_ID = 21;
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

        LoaderManager.LoaderCallbacks<MovieDb> callback = DetailsActivity.this;

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, callback);

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

        loadMovieData(extraMovieId);
    }

    private void loadMovieData(String query) {
        if (TextUtils.isEmpty(query)) return;

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_EXTRA, query);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<MovieDb> moviesSearchLoader = loaderManager.getLoader(MOVIES_LOADER_ID);
        if (moviesSearchLoader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, queryBundle, this);
        }
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

    @Override
    public Loader<MovieDb> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieDb>(this) {

            MovieDb mMovie = null;

            @Override
            public void deliverResult(MovieDb movie) {
                mMovie = movie;
                super.deliverResult(movie);
            }

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                if (mMovie != null) {
                    deliverResult(mMovie);
                } else {
                    mLoadingProgressBar.setVisibility(View.VISIBLE);
                    mDetailsContainer.setVisibility(View.INVISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieDb loadInBackground() {
                String query = args.getString(SEARCH_QUERY_EXTRA);

                if (query == null || TextUtils.isEmpty(query)) {
                    return null;
                }

                try {
                    return omdbApi.getMovieData(query);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieDb> loader, MovieDb movieData) {
        if (movieData != null) {
            mMovieTitleTextView.setText(movieData.getMovieTitle());
            mReleaseYearTextView.setText("(" + movieData.getReleaseYear() + ")");
            mMovieInfoTextView.setText(movieData.getRate() + " | " + movieData.getRunTime() + " | " +
                    movieData.getGenre());
            mPlotTextView.setText(movieData.getPlot());
            mImdbRatingTextView.setText(String.valueOf(movieData.getImdbRating()));
            mImdbVotesTextView.setText(movieData.getImdbVotes());
            mMovieCastTextView.setText("Director: " + movieData.getDirector() + "\n" + "Writers: " + movieData.getWriter() + "\n" + "Actors: " + movieData.getActors());

            String metascore = String.valueOf(movieData.getMetascore());
            int bgColorForMetascore = getMetaBgColor(DetailsActivity.this, metascore);

            mMetascoreTextView.setText(metascore);
            mMetascoreTextView.setBackgroundColor(bgColorForMetascore);

            Picasso.with(DetailsActivity.this).load(movieData.getPoster())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(mMoviePic);
        }
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mDetailsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<MovieDb> loader) {

    }
}
