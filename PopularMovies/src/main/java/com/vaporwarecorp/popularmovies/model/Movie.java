package com.vaporwarecorp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public final String backdropPath;
    public final int id;
    public final String originalTitle;
    public final String overview;
    public final String posterPath;
    public final Date releaseDate;
    public final float voteAverage;
    public final String voteCount;

// --------------------------- CONSTRUCTORS ---------------------------

    protected Movie(Parcel in) {
        this(
                in.readString(),
                in.readInt(),
                in.readString(),
                in.readString(),
                in.readString(),
                new Date(in.readLong()),
                in.readFloat(),
                in.readString()
        );
    }

    public Movie(String backdropPath,
                 int id,
                 String originalTitle,
                 String overview,
                 String posterPath,
                 Date releaseDate,
                 float voteAverage,
                 String voteCount) {
        this.backdropPath = backdropPath;
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Movie{" +
                "backdropPath='" + backdropPath + '\'' +
                ", id=" + id +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage + '\'' +
                ", voteCount=" + voteCount +
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
        dest.writeString(backdropPath);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeLong(releaseDate.getTime());
        dest.writeFloat(voteAverage);
        dest.writeString(voteCount);
    }
}
