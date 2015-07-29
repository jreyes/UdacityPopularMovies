package com.vaporwarecorp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = "videos")
public class Video implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @StorIOSQLiteColumn(name = "id", key = true)
    String id;
    @StorIOSQLiteColumn(name = "key")
    String key;
    @StorIOSQLiteColumn(name = "movie_id")
    int movieId;

// -------------------------- STATIC METHODS --------------------------

    public static Video newInstance(String id, String key, int movieId) {
        Video video = new Video();
        video.id = id;
        video.key = key;
        video.movieId = movieId;
        return video;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    Video() {
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.movieId = in.readInt();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public int getMovieId() {
        return movieId;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", movieId='" + movieId + '\'' +
                '}';
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Parcelable ---------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeInt(this.movieId);
    }
}
