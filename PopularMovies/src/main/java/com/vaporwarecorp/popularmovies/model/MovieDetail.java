package com.vaporwarecorp.popularmovies.model;

import java.util.ArrayList;

public class MovieDetail {
// ------------------------------ FIELDS ------------------------------

    public int movieId;
    public ArrayList<Review> reviews;
    public ArrayList<Video> videos;

// -------------------------- STATIC METHODS --------------------------

    public static MovieDetail newInstance(int movieId, ArrayList<Review> reviews, ArrayList<Video> videos) {
        MovieDetail movieDetails = new MovieDetail();
        movieDetails.movieId = movieId;
        movieDetails.reviews = reviews;
        movieDetails.videos = videos;
        return movieDetails;
    }
}
