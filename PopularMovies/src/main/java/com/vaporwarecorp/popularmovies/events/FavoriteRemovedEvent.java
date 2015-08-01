package com.vaporwarecorp.popularmovies.events;

import com.vaporwarecorp.popularmovies.model.Movie;

public class FavoriteRemovedEvent {
// ------------------------------ FIELDS ------------------------------

    public final Movie movie;

// --------------------------- CONSTRUCTORS ---------------------------

    public FavoriteRemovedEvent(Movie movie) {
        this.movie = movie;
    }
}
