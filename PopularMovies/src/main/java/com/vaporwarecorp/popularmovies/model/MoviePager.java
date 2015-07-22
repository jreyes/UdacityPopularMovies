package com.vaporwarecorp.popularmovies.model;

import java.util.List;

public class MoviePager {
// ------------------------------ FIELDS ------------------------------

    public int page;
    public List<Movie> results;
    public int totalPages;
    public int totalResults;

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "MoviePager{" +
                "results=" + results +
                ", page=" + page +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                '}';
    }
}
