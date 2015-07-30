package com.vaporwarecorp.popularmovies.service;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.vaporwarecorp.popularmovies.model.*;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.List;

public class MovieDB {
    private StorIOSQLite mStorIOSQLite;

// -------------------------- INNER CLASSES --------------------------

    public static class MovieEntry {
        public static final String TABLE_NAME = "movies";
        public static final String COL_ID = "_id";
        public static final String COL_ORIGINAL_TITLE = "original_title";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_POSTER_PATH = "poster_path";
        public static final String COL_BACKDROP_PATH = "backdrop_path";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_VOTE_COUNT = "vote_count";
    }

    public static class ReviewEntry {
        public static final String TABLE_NAME = "reviews";
        public static final String COL_ID = "_id";
        public static final String COL_AUTHOR = "author";
        public static final String COL_CONTENT = "content";
        public static final String COL_MOVIE_ID = "movie_id";
    }

    public static class VideoEntry {
        public static final String TABLE_NAME = "videos";
        public static final String COL_ID = "_id";
        public static final String COL_KEY = "key";
        public static final String COL_MOVIE_ID = "movie_id";
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieDB(Context context) {
        mStorIOSQLite = provideStorIOSQLite(new MovieDBOpenHelper(context));
    }

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

    public Observable<MoviePager> getMovies() {
        return mStorIOSQLite
                .get()
                .listOfObjects(Movie.class)
                .withQuery(Query.builder().table(MovieEntry.TABLE_NAME).build())
                .prepare()
                .createObservable()
                .flatMap(new Func1<List<Movie>, Observable<MoviePager>>() {
                    @Override
                    public Observable<MoviePager> call(List<Movie> movies) {
                        return Observable.just(MoviePager.newInstance(1, 1, movies.size(), movies));
                    }
                });
    }

    private Observable<List<Review>> getReviews(int movieId) {
        return mStorIOSQLite
                .get()
                .listOfObjects(Review.class)
                .withQuery(Query
                        .builder()
                        .table(ReviewEntry.TABLE_NAME)
                        .where(ReviewEntry.COL_MOVIE_ID + "=?")
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
                        .table(VideoEntry.TABLE_NAME)
                        .where(VideoEntry.COL_MOVIE_ID + "=?")
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
