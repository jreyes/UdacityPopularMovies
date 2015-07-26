package com.vaporwarecorp.popularmovies.model;

import java.util.List;

public class VideoPager {
// ------------------------------ FIELDS ------------------------------

    public int id;
    public List<Video> results;

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "VideoPager{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}
