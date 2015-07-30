package com.vaporwarecorp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.util.Date;

import static com.vaporwarecorp.popularmovies.service.MovieDB.MovieEntry.*;

@StorIOSQLiteType(table = "movies")
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

    @StorIOSQLiteColumn(name = COL_BACKDROP_PATH)
    String backdropPath;
    @StorIOSQLiteColumn(name = COL_ID, key = true)
    int id;
    @StorIOSQLiteColumn(name = COL_ORIGINAL_TITLE)
    String originalTitle;
    @StorIOSQLiteColumn(name = COL_OVERVIEW)
    String overview;
    @StorIOSQLiteColumn(name = COL_POSTER_PATH)
    String posterPath;
    Date releaseDate;
    @StorIOSQLiteColumn(name = COL_RELEASE_DATE)
    Long releaseDateTime;
    @StorIOSQLiteColumn(name = COL_VOTE_AVERAGE)
    float voteAverage;
    @StorIOSQLiteColumn(name = COL_VOTE_COUNT)
    String voteCount;

// --------------------------- CONSTRUCTORS ---------------------------

    Movie() {
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

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Date getReleaseDate() {
        if (releaseDate == null) {
            return new Date(releaseDateTime);
        }
        return releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
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
        dest.writeLong(getReleaseDate().getTime());
        dest.writeFloat(voteAverage);
        dest.writeString(voteCount);
    }
}
