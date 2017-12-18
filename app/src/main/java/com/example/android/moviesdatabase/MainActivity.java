package com.example.android.moviesdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesdatabase.utilities.DatasetUtils;
import com.example.android.moviesdatabase.utilities.JsonUtils;
import com.example.android.moviesdatabase.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieDbAdapter.MovieDbAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<DatasetUtils>> {

    private static final String SEARCH_QUERY_EXTRA = "query";
    private static final int MOVIES_LOADER_ID = 11;
    ArrayList<DatasetUtils> list;
    private EditText mMovieSearchEditText;
    private Button mSearchButton;
    private RecyclerView mRecyclerView;
    private MovieDbAdapter mMovieDbAdapter;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("moviesList", mMovieDbAdapter.getMovieData());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        list = savedInstanceState.getParcelableArrayList("moviesList");
        mMovieDbAdapter.setMovieData(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieSearchEditText = (EditText) findViewById(R.id.et_movie_search);
        mSearchButton = (Button) findViewById(R.id.button_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_data);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mMovieDbAdapter = new MovieDbAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieDbAdapter);

        LoaderManager.LoaderCallbacks<ArrayList<DatasetUtils>> callback = MainActivity.this;

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, callback);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieData();
            }
        });
    }

    private void loadMovieData() {
        String query = mMovieSearchEditText.getText().toString();

        if (TextUtils.isEmpty(query)) {
            Toast.makeText(MainActivity.this, "Please type something to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_EXTRA, query);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<DatasetUtils>> moviesSearchLoader = loaderManager.getLoader(MOVIES_LOADER_ID);
        if (moviesSearchLoader == null) {
            loaderManager.initLoader(MOVIES_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(MOVIES_LOADER_ID, queryBundle, this);
        }
    }

    private void showMovieDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(String movieId) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("EXTRA_MOVIE_ID", movieId);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<DatasetUtils>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<DatasetUtils>>(this) {

            ArrayList<DatasetUtils> mMoviesData = null;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                if (mMoviesData != null) {
                    deliverResult(mMoviesData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<DatasetUtils> loadInBackground() {
                String query = args.getString(SEARCH_QUERY_EXTRA);

                if (query == null || TextUtils.isEmpty(query)) {
                    return null;
                }

                URL movieSearchUrl = NetworkUtils.buildUrl(MainActivity.this, query, NetworkUtils.SearchType.BY_SEARCH);

                try {
                    String jsonOmdbResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieSearchUrl);

                    return JsonUtils.getOmdbDataFromJson(jsonOmdbResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<DatasetUtils> moviesData) {
                mMoviesData = moviesData;
                super.deliverResult(moviesData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<DatasetUtils>> loader, ArrayList<DatasetUtils> moviesData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (moviesData == null) {
            showErrorMessage();
        } else {
            showMovieDataView();
            mMovieDbAdapter.setMovieData(moviesData);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<DatasetUtils>> loader) {

    }
}
