package com.vaporwarecorp.popularmovies.service.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.Review;
import com.vaporwarecorp.popularmovies.service.MovieDB;

@Table(databaseName = MovieDB.PopularMovies.NAME)
public class ReviewEntity extends BaseModel {
// ------------------------------ FIELDS ------------------------------

    @Column public String author;
    @Column public String content;
    @Column @PrimaryKey public String id;
    @Column public int movieId;

// -------------------------- STATIC METHODS --------------------------

    public static ReviewEntity newInstance(Movie movie, Review review) {
        ReviewEntity entity = new ReviewEntity();
        entity.author = review.author;
        entity.content = review.content;
        entity.id = review.id;
        entity.movieId = movie.id;
        return entity;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }
}
