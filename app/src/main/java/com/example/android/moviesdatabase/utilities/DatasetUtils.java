package com.example.android.moviesdatabase.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Emad on 12/12/2017.
 */

public class DatasetUtils implements Parcelable {

    public static final Creator<DatasetUtils> CREATOR = new Creator<DatasetUtils>() {
        @Override
        public DatasetUtils createFromParcel(Parcel in) {
            return new DatasetUtils(in);
        }

        @Override
        public DatasetUtils[] newArray(int size) {
            return new DatasetUtils[size];
        }
    };

    private String title;
    private String poster;
    private String year;
    private String id;
    private String rated;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String metascore;
    private String rating;
    private String votes;

    public DatasetUtils(String movieTitle, String posterUrl, String year, String imdbId) {
        this.title = movieTitle;
        this.poster = posterUrl;
        this.year = year;
        this.id = imdbId;
    }

    public DatasetUtils(String title, String poster, String year, String rated, String runtime,
                        String genre, String director, String writer, String actors, String plot,
                        String metascore, String rating, String votes) {
        this.title = title;
        this.poster = poster;
        this.year = year;
        this.rated = rated;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.metascore = metascore;
        this.rating = rating;
        this.votes = votes;
    }

    private DatasetUtils(Parcel in) {
        title = in.readString();
        poster = in.readString();
        year = in.readString();
        id = in.readString();
    }

    public String getMovieTitle() {
        return title;
    }

    public String getMoviePoster() {
        return poster;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return id;
    }

    public String getRated() {
        return rated;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getMetascore() {
        return metascore;
    }

    public String getImdbRating() {
        return rating;
    }

    public String getImdbVotes() {
        return votes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(year);
        parcel.writeString(id);
    }
}
