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

    public DatasetUtils(String movieTitle, String posterUrl, String year, String imdbId) {
        this.title = movieTitle;
        this.poster = posterUrl;
        this.year = year;
        this.id = imdbId;
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
