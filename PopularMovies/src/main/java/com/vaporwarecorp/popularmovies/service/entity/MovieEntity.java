package com.vaporwarecorp.popularmovies.service.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.service.MovieDB;

import java.util.Date;

@Table(databaseName = MovieDB.NAME)
public class MovieEntity extends BaseModel {
// ------------------------------ FIELDS ------------------------------

    @Column public String backdropPath;
    @Column @PrimaryKey public int id;
    @Column public String originalTitle;
    @Column public String overview;
    @Column public String posterPath;
    @Column public Date releaseDate;
    @Column public float voteAverage;
    @Column public String voteCount;

// -------------------------- STATIC METHODS --------------------------

    public static MovieEntity newInstance(Movie movie) {
        MovieEntity entity = new MovieEntity();
        entity.backdropPath = movie.backdropPath;
        entity.id = movie.id;
        entity.originalTitle = movie.originalTitle;
        entity.overview = movie.overview;
        entity.posterPath = movie.posterPath;
        entity.releaseDate = movie.releaseDate;
        entity.voteAverage = movie.voteAverage;
        entity.voteCount = movie.voteCount;
        return entity;
    }
}
