package com.vaporwarecorp.popularmovies.events;

public class MovieTypeSelectedEvent {
// ------------------------------ FIELDS ------------------------------

    public final int type;

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieTypeSelectedEvent(int type) {
        this.type = type;
    }
}
