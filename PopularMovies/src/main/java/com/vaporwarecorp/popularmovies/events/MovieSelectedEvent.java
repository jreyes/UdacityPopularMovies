package com.vaporwarecorp.popularmovies.events;

import com.vaporwarecorp.popularmovies.model.Movie;

public class MovieSelectedEvent {
// ------------------------------ FIELDS ------------------------------

    public final Movie movie;

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieSelectedEvent(Movie movie) {
        this.movie = movie;
    }
}
