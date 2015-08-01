package com.vaporwarecorp.popularmovies.events;

import com.vaporwarecorp.popularmovies.model.Movie;

public class FavoriteAddedEvent {
// ------------------------------ FIELDS ------------------------------

    public final Movie movie;

// --------------------------- CONSTRUCTORS ---------------------------

    public FavoriteAddedEvent(Movie movie) {
        this.movie = movie;
    }
}
