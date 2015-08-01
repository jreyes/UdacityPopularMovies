package com.vaporwarecorp.popularmovies.model;

import java.util.ArrayList;

public class MovieDetail {
// ------------------------------ FIELDS ------------------------------

    int movieId;
    ArrayList<Review> reviews;
    ArrayList<Video> videos;

// -------------------------- STATIC METHODS --------------------------

    public static MovieDetail newInstance(int movieId, ArrayList<Review> reviews, ArrayList<Video> videos) {
        MovieDetail movieDetails = new MovieDetail();
        movieDetails.movieId = movieId;
        movieDetails.reviews = reviews;
        movieDetails.videos = videos;
        return movieDetails;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    MovieDetail() {
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getMovieId() {
        return movieId;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }
}
