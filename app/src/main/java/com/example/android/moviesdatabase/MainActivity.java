package com.example.android.moviesdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emadabel.openmoviesdbapilibrary.OmdbApi;
import com.emadabel.openmoviesdbapilibrary.SearchResults;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MovieDbAdapter.MovieDbAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<SearchResults> {

    private static final String SEARCH_QUERY_EXTRA = "query";
    private static final int MOVIES_LOADER_ID = 11;
    static OmdbApi omdbApi;
    private MaterialSearchView searchView;
    private RecyclerView mRecyclerView;
    private MovieDbAdapter mMovieDbAdapter;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String apiKey = BuildConfig.OMDB_API_KEY;
        omdbApi = new OmdbApi(apiKey);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_data);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mMovieDbAdapter = new MovieDbAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieDbAdapter);

        LoaderManager.LoaderCallbacks<SearchResults> callback = MainActivity.this;

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, callback);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        //searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadMovieData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, true);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void loadMovieData(String query) {
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(MainActivity.this, "Please type something to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_EXTRA, query);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<SearchResults> moviesSearchLoader = loaderManager.getLoader(MOVIES_LOADER_ID);
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
    public Loader<SearchResults> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<SearchResults>(this) {

            SearchResults mMoviesData = null;

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
            public SearchResults loadInBackground() {
                String query = args.getString(SEARCH_QUERY_EXTRA);

                if (query == null || TextUtils.isEmpty(query)) {
                    return null;
                }

                try {
                    return omdbApi.getMovies(query);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(SearchResults moviesData) {
                mMoviesData = moviesData;
                super.deliverResult(moviesData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<SearchResults> loader, SearchResults moviesData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (moviesData == null) {
            showErrorMessage();
        } else {
            showMovieDataView();
            mMovieDbAdapter.setMovieData(moviesData.getMovies());
        }
    }

    @Override
    public void onLoaderReset(Loader<SearchResults> loader) {

    }
}
