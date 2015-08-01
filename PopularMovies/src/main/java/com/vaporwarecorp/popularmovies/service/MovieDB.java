package com.vaporwarecorp.popularmovies.service;

import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import com.vaporwarecorp.popularmovies.model.Review;
import com.vaporwarecorp.popularmovies.model.Video;
import com.vaporwarecorp.popularmovies.service.entity.MovieEntity;
import com.vaporwarecorp.popularmovies.service.entity.ReviewEntity;
import com.vaporwarecorp.popularmovies.service.entity.VideoEntity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

import java.util.ArrayList;
import java.util.List;

public class MovieDB {
// --------------------------- CONSTRUCTORS ---------------------------

    public MovieDB(Context context) {
        FlowManager.init(context);
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    private MoviePager getMoviePager() {
        MoviePager moviePager = new MoviePager();
        moviePager.results = convert(new Select().from(MovieEntity.class).queryList());
        moviePager.totalPages = 1;
        moviePager.page = 1;
        return moviePager;
    }

// -------------------------- OTHER METHODS --------------------------

    public Observable<Void> addMovie(final Movie movie, final List<Video> videos, final List<Review> reviews) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                MovieEntity.newInstance(movie).save();
                for (Review review : reviews) {
                    ReviewEntity.newInstance(movie, review).save();
                }
                for (Video video : videos) {
                    VideoEntity.newInstance(movie, video).save();
                }
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<MoviePager> getMovies() {
        return Observable.defer(new Func0<Observable<MoviePager>>() {
            @Override
            public Observable<MoviePager> call() {
                return Observable.just(getMoviePager());
            }
        });
    }

    public boolean movieExists(int movieId) {
        MovieEntity entity = new MovieEntity();
        entity.id = movieId;
        return entity.exists();
    }

    public Observable<Movie> removeMovie(final Movie movie) {
        return Observable.create(new Observable.OnSubscribe<Movie>() {
            @Override
            public void call(Subscriber<? super Movie> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                MovieEntity entity = MovieEntity.newInstance(movie);
                entity.deleteReviews();
                entity.deleteVideos();
                entity.delete();

                subscriber.onNext(movie);
                subscriber.onCompleted();
            }
        });
    }

    private ArrayList<Movie> convert(List<MovieEntity> entities) {
        ArrayList<Movie> movies = new ArrayList<>();
        for (MovieEntity entity : entities) {
            Movie movie = new Movie();
            movie.backdropPath = entity.backdropPath;
            movie.id = entity.id;
            movie.originalTitle = entity.originalTitle;
            movie.overview = entity.overview;
            movie.posterPath = entity.posterPath;
            movie.releaseDate = entity.releaseDate;
            movie.voteAverage = entity.voteAverage;
            movie.voteCount = entity.voteCount;
            movies.add(movie);
        }
        return movies;
    }

// -------------------------- INNER CLASSES --------------------------

    @Database(name = PopularMovies.NAME, version = PopularMovies.VERSION)
    public static class PopularMovies {
        public static final String NAME = "popular_movies";
        public static final int VERSION = 1;
    }
}
