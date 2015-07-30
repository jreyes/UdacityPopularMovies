package com.vaporwarecorp.popularmovies.model;

import java.util.List;

public class ReviewPager {
// ------------------------------ FIELDS ------------------------------

    public int id;
    public List<Review> results;

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "ReviewPager{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}
