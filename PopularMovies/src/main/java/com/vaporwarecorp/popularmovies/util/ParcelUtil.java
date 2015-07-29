package com.vaporwarecorp.popularmovies.util;

import android.os.Bundle;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.Review;
import com.vaporwarecorp.popularmovies.model.Video;

import java.util.ArrayList;

public class ParcelUtil {
// ------------------------------ FIELDS ------------------------------

    public static final String KEY_EVENT_TYPE = "KEY_EVENT_TYPE";
    public static final String KEY_MOVIE = "KEY_MOVIE";
    public static final String KEY_MOVIES = "KEY_MOVIES";
    public static final String KEY_PAGE = "KEY_PAGE";
    public static final String KEY_REVIEWS = "KEY_REVIEWS";
    public static final String KEY_TOTAL_PAGES = "KEY_TOTAL_PAGES";
    public static final String KEY_VIDEOS = "KEY_VIDEOS";

// -------------------------- STATIC METHODS --------------------------

    public static int getEventType(Bundle bundle) {
        return bundle.getInt(KEY_EVENT_TYPE);
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

    public static ArrayList<Review> getReviews(Bundle bundle) {
        return bundle.getParcelableArrayList(KEY_REVIEWS);
    }

    public static int getTotalPages(Bundle bundle) {
        return bundle.getInt(KEY_TOTAL_PAGES);
    }

    public static ArrayList<Video> getVideos(Bundle bundle) {
        return bundle.getParcelableArrayList(KEY_VIDEOS);
    }

    public static void setEventType(Bundle bundle, int value) {
        bundle.putInt(KEY_EVENT_TYPE, value);
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

    public static void setReviews(Bundle bundle, ArrayList<Review> value) {
        bundle.putParcelableArrayList(KEY_REVIEWS, value);
    }

    public static void setTotalPages(Bundle bundle, int value) {
        bundle.putInt(KEY_TOTAL_PAGES, value);
    }

    public static void setVideos(Bundle bundle, ArrayList<Video> videos) {
        bundle.putParcelableArrayList(KEY_VIDEOS, videos);
    }
}
