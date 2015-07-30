package com.vaporwarecorp.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail {
// ------------------------------ FIELDS ------------------------------

    int movieId;
    ArrayList<Review> reviews;
    ArrayList<Video> videos;

// -------------------------- STATIC METHODS --------------------------

    public static MovieDetail newInstance(int movieId, List<Review> reviews, List<Video> videos) {
        ArrayList<Review> associatedReviews = new ArrayList<Review>();
        for (Review review : reviews) {
            associatedReviews.add(Review.newInstance(review.getId(), review.getAuthor(), review.getContent(), movieId));
        }
        ArrayList<Video> associatedVideos = new ArrayList<Video>();
        for (Video video : videos) {
            associatedVideos.add(Video.newInstance(video.getId(), video.getKey(), movieId));
        }

        MovieDetail movieDetails = new MovieDetail();
        movieDetails.movieId = movieId;
        movieDetails.reviews = associatedReviews;
        movieDetails.videos = associatedVideos;
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
