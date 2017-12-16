package com.example.android.moviesdatabase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements MovieDbAdapter.MovieDbAdapterOnClickHandler {

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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieDbAdapter);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovieSearchEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please type something to search", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadMovieData();
            }
        });
    }

    private void loadMovieData() {
        showMovieDataView();

        String query = mMovieSearchEditText.getText().toString();
        new FetchMovieTask().execute(query);
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

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<DatasetUtils>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<DatasetUtils> doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            String query = strings[0];
            URL movieSearchUrl = NetworkUtils.buildUrl(MainActivity.this, query, NetworkUtils.SearchType.BY_SEARCH);

            try {
                String jsonOmdbResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieSearchUrl);

                ArrayList<DatasetUtils> moviesList = JsonUtils
                        .getOmdbDataFromJson(jsonOmdbResponse);

                return moviesList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<DatasetUtils> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieDbAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }
}
