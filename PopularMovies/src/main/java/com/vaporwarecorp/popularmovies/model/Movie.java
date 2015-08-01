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

    public String backdropPath;
    public int id;
    public String originalTitle;
    public String overview;
    public String posterPath;
    public Date releaseDate;
    public float voteAverage;
    public String voteCount;

// --------------------------- CONSTRUCTORS ---------------------------

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.backdropPath = in.readString();
        this.id = in.readInt();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = new Date(in.readLong());
        this.voteAverage = in.readFloat();
        this.voteCount = in.readString();
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id == movie.id && Float.compare(movie.voteAverage, voteAverage) == 0 &&
                !(backdropPath != null ? !backdropPath.equals(movie.backdropPath) : movie.backdropPath != null) &&
                originalTitle.equals(movie.originalTitle) &&
                !(overview != null ? !overview.equals(movie.overview) : movie.overview != null) &&
                !(posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null) &&
                releaseDate.equals(movie.releaseDate) &&
                !(voteCount != null ? !voteCount.equals(movie.voteCount) : movie.voteCount != null);
    }

    @Override
    public int hashCode() {
        int result = backdropPath != null ? backdropPath.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + originalTitle.hashCode();
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + (voteAverage != +0.0f ? Float.floatToIntBits(voteAverage) : 0);
        result = 31 * result + (voteCount != null ? voteCount.hashCode() : 0);
        return result;
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
