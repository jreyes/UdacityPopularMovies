package com.vaporwarecorp.popularmovies.service;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.vaporwarecorp.popularmovies.model.*;
import rx.Observable;
import rx.functions.Func2;

import java.util.List;

public class MovieDB {
// ------------------------------ FIELDS ------------------------------

    private StorIOSQLite mStorIOSQLite;

// -------------------------- OTHER METHODS --------------------------

    public Observable<MovieDetail> getMovieDetail(final int movieId) {
        return Observable.zip(
                getVideos(movieId),
                getReviews(movieId),
                new Func2<List<Video>, List<Review>, MovieDetail>() {
                    @Override
                    public MovieDetail call(List<Video> videos, List<Review> reviews) {
                        return MovieDetail.newInstance(movieId, reviews, videos);
                    }
                }
        );
    }

    public Observable<List<Movie>> getMovies() {
        return mStorIOSQLite
                .get()
                .listOfObjects(Movie.class)
                .withQuery(Query.builder().table("movies").build())
                .prepare()
                .createObservable();
    }

    private Observable<List<Review>> getReviews(int movieId) {
        return mStorIOSQLite
                .get()
                .listOfObjects(Review.class)
                .withQuery(Query
                        .builder()
                        .table("reviews")
                        .where("movie_id=?")
                        .whereArgs(movieId)
                        .build())
                .prepare()
                .createObservable();
    }

    private Observable<List<Video>> getVideos(int movieId) {
        return mStorIOSQLite
                .get()
                .listOfObjects(Video.class)
                .withQuery(Query
                        .builder()
                        .table("videos")
                        .where("movie_id=?")
                        .whereArgs(movieId)
                        .build())
                .prepare()
                .createObservable();
    }

    private StorIOSQLite provideStorIOSQLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(Movie.class, SQLiteTypeMapping.<Movie>builder()
                        .putResolver(new MovieStorIOSQLitePutResolver())
                        .getResolver(new MovieStorIOSQLiteGetResolver())
                        .deleteResolver(new MovieStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(Video.class, SQLiteTypeMapping.<Video>builder()
                        .putResolver(new VideoStorIOSQLitePutResolver())
                        .getResolver(new VideoStorIOSQLiteGetResolver())
                        .deleteResolver(new VideoStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(Review.class, SQLiteTypeMapping.<Review>builder()
                        .putResolver(new ReviewStorIOSQLitePutResolver())
                        .getResolver(new ReviewStorIOSQLiteGetResolver())
                        .deleteResolver(new ReviewStorIOSQLiteDeleteResolver())
                        .build())
                .build();
    }

    private Observable<PutResult> putMovie(Movie movie) {
        return mStorIOSQLite
                .put()
                .object(movie)
                .prepare()
                .createObservable();
    }

    private Observable<PutResult> putMovie(List<Review> reviews) {
        return mStorIOSQLite
                .put()
                .object(reviews)
                .prepare()
                .createObservable();
    }

    private Observable<PutResult> putVideos(List<Video> videos) {
        return mStorIOSQLite
                .put()
                .object(videos)
                .prepare()
                .createObservable();
    }
}
