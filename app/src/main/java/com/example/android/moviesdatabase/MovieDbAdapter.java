package com.example.android.moviesdatabase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.emadabel.openmoviesdbapilibrary.OmdbMovies;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Emad on 12/12/2017.
 */

public class MovieDbAdapter extends RecyclerView.Adapter<MovieDbAdapter.MovieViewHolder> {

    private final MovieDbAdapterOnClickHandler mClickHandler;
    private List<OmdbMovies> mMovieData;
    private Context context;

    public MovieDbAdapter(MovieDbAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
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

        //String movieName = mMovieData.get(position).getMovieTitle();
        //String movieYear = mMovieData.get(position).getReleaseYear();
        String moviePoster = mMovieData.get(position).getPoster();

        //movieViewHolder.mMovieNameTextView.setText(movieName + " (" + movieYear + ")");

        Picasso.with(context).load(moviePoster)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(movieViewHolder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    public List<OmdbMovies> getMovieData() {
        return mMovieData;
    }

    public void setMovieData(List<OmdbMovies> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public interface MovieDbAdapterOnClickHandler {
        void onClick(String movieId);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMoviePosterImageView;
        //public final TextView mMovieNameTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            //mMovieNameTextView = (TextView) itemView.findViewById(R.id.tv_movie_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String movieId = mMovieData.get(adapterPosition).getImdbId();
            mClickHandler.onClick(movieId);
        }
    }
}
