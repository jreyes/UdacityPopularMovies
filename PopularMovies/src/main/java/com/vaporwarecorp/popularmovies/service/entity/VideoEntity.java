package com.vaporwarecorp.popularmovies.service.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.Video;
import com.vaporwarecorp.popularmovies.service.MovieDB;

@Table(databaseName = MovieDB.NAME)
public class VideoEntity extends BaseModel {
// ------------------------------ FIELDS ------------------------------

    @Column @PrimaryKey public String id;
    @Column public String key;
    @Column public int movieId;
    @Column public String name;

// -------------------------- STATIC METHODS --------------------------

    public static VideoEntity newInstance(Movie movie, Video video) {
        VideoEntity entity = new VideoEntity();
        entity.id = video.id;
        entity.key = video.key;
        entity.movieId = movie.id;
        entity.name = video.name;
        return entity;
    }
}
