package com.vaporwarecorp.popularmovies.model;

import java.util.List;

public class MoviePager {
// ------------------------------ FIELDS ------------------------------

    public int page;
    public List<Movie> results;
    public int totalPages;
    public int totalResults;

// -------------------------- STATIC METHODS --------------------------

    public static MoviePager newInstance(int page, int totalPages, int totalResults, List<Movie> movies) {
        MoviePager moviePager = new MoviePager();
        moviePager.page = page;
        moviePager.totalPages = totalPages;
        moviePager.totalResults = totalResults;
        moviePager.results = movies;
        return moviePager;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    MoviePager() {
    }

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
