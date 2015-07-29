package com.vaporwarecorp.popularmovies.events;

import com.vaporwarecorp.popularmovies.model.Movie;

public class MovieSelectedEvent {
// ------------------------------ FIELDS ------------------------------

    public final boolean clicked;
    public final Movie movie;

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieSelectedEvent(Movie movie, boolean clicked) {
        this.movie = movie;
        this.clicked = clicked;
    }
}
