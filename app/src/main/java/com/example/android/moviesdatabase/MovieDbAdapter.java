package com.example.android.moviesdatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesdatabase.utilities.DatasetUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Emad on 12/12/2017.
 */

public class MovieDbAdapter extends RecyclerView.Adapter<MovieDbAdapter.MovieViewHolder> {

    private ArrayList<DatasetUtils> mMovieData;
    private Context context;

    public MovieDbAdapter() {

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position) {

        String movieName = mMovieData.get(position).getMovieTitle();
        String movieYear = mMovieData.get(position).getYear();
        String moviePoster = mMovieData.get(position).getMoviePoster();

        movieViewHolder.mMovieNameTextView.setText(movieName + " (" + movieYear + ")");

        Picasso.with(context).load(moviePoster)
                .into(movieViewHolder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null)return 0;
        return mMovieData.size();
    }

    public ArrayList<DatasetUtils> getMovieData() {
        return mMovieData;
    }

    public void setMovieData(ArrayList<DatasetUtils> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mMoviePosterImageView;
        public final TextView mMovieNameTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            mMovieNameTextView = (TextView) itemView.findViewById(R.id.tv_movie_name);
        }
    }
}
