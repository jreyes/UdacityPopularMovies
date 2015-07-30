package com.vaporwarecorp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.vaporwarecorp.popularmovies.service.MovieDB.ReviewEntry.*;

@StorIOSQLiteType(table = "reviews")
public class Review implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @StorIOSQLiteColumn(name = COL_AUTHOR)
    String author;
    @StorIOSQLiteColumn(name = COL_CONTENT)
    String content;
    @StorIOSQLiteColumn(name = COL_ID, key = true)
    String id;
    @StorIOSQLiteColumn(name = COL_MOVIE_ID)
    int movieId;

// -------------------------- STATIC METHODS --------------------------

    public static Review newInstance(String id, String author, String content, int movieId) {
        Review review = new Review();
        review.id = id;
        review.author = author;
        review.content = content;
        review.movieId = movieId;
        return review;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    Review() {
    }

    protected Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.movieId = in.readInt();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
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
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeInt(this.movieId);
    }
}
