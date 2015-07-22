package com.vaporwarecorp.popularmovies.util;

import android.os.Bundle;
import com.vaporwarecorp.popularmovies.model.Movie;

import java.util.ArrayList;

public class ParcelUtil {
// ------------------------------ FIELDS ------------------------------

    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_MOVIE = "KEY_MOVIE";
    public static final String KEY_MOVIES = "KEY_MOVIES";
    public static final String KEY_PAGE = "KEY_PAGE";
    public static final String KEY_TOTAL_PAGES = "KEY_TOTAL_PAGES";

// -------------------------- STATIC METHODS --------------------------

    public static int getAction(Bundle bundle) {
        return bundle.getInt(KEY_ACTION);
    }

    public static Movie getMovie(Bundle bundle) {
        return bundle.getParcelable(KEY_MOVIE);
    }

    public static ArrayList<Movie> getMovies(Bundle bundle) {
        return bundle.getParcelableArrayList(KEY_MOVIES);
    }

    public static int getPage(Bundle bundle) {
        return bundle.getInt(KEY_PAGE);
    }

    public static int getTotalPages(Bundle bundle) {
        return bundle.getInt(KEY_TOTAL_PAGES);
    }

    public static void setAction(Bundle bundle, int value) {
        bundle.putInt(KEY_ACTION, value);
    }

    public static void setMovie(Bundle bundle, Movie value) {
        bundle.putParcelable(KEY_MOVIE, value);
    }

    public static void setMovies(Bundle bundle, ArrayList<Movie> value) {
        bundle.putParcelableArrayList(KEY_MOVIES, value);
    }

    public static void setPage(Bundle bundle, int value) {
        bundle.putInt(KEY_PAGE, value);
    }

    public static void setTotalPages(Bundle bundle, int value) {
        bundle.putInt(KEY_TOTAL_PAGES, value);
    }
}
