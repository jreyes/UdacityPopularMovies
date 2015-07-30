package com.vaporwarecorp.popularmovies.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBOpenHelper extends SQLiteOpenHelper {
// ------------------------------ FIELDS ------------------------------

    private static final String DATABASE_NAME = "mobiedb";
    private static final int DATABASE_VERSION = 1;

// -------------------------- STATIC METHODS --------------------------

    public static String getMovieCreateQuery() {
        return "create table " +
                MovieDB.MovieEntry.TABLE_NAME + " (" +
                MovieDB.MovieEntry.COL_ID + " integer primary key, " +
                MovieDB.MovieEntry.COL_ORIGINAL_TITLE + " text not null, " +
                MovieDB.MovieEntry.COL_OVERVIEW + " text, " +
                MovieDB.MovieEntry.COL_BACKDROP_PATH + " text, " +
                MovieDB.MovieEntry.COL_POSTER_PATH + " text, " +
                MovieDB.MovieEntry.COL_RELEASE_DATE + " integer not null, " +
                MovieDB.MovieEntry.COL_VOTE_AVERAGE + " real not null, " +
                MovieDB.MovieEntry.COL_VOTE_COUNT + " integer not null);";
    }

    public static String getReviewCreateQuery() {
        return "create table " +
                MovieDB.ReviewEntry.TABLE_NAME + " (" +
                MovieDB.ReviewEntry.COL_ID + " text primary key, " +
                MovieDB.ReviewEntry.COL_AUTHOR + " text not null, " +
                MovieDB.ReviewEntry.COL_CONTENT + " text not null, " +
                MovieDB.ReviewEntry.COL_MOVIE_ID + " integer not null);";
    }

    public static String getVideoCreateQuery() {
        return "create table " +
                MovieDB.VideoEntry.TABLE_NAME + " (" +
                MovieDB.VideoEntry.COL_ID + " text primary key, " +
                MovieDB.VideoEntry.COL_KEY + " text not null, " +
                MovieDB.VideoEntry.COL_MOVIE_ID + " integer not null);";
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getMovieCreateQuery());
        db.execSQL(getReviewCreateQuery());
        db.execSQL(getVideoCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + MovieDB.MovieEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + MovieDB.ReviewEntry.TABLE_NAME);
        db.execSQL("drop table if exists " + MovieDB.VideoEntry.TABLE_NAME);
        onCreate(db);
    }
}
