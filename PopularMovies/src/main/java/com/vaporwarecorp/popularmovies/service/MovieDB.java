package com.vaporwarecorp.popularmovies.service;

import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.vaporwarecorp.popularmovies.model.*;
import com.vaporwarecorp.popularmovies.service.entity.*;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

@Database(name = MovieDB.NAME, version = MovieDB.VERSION, consistencyCheckEnabled = true)
public class MovieDB {
// ------------------------------ FIELDS ------------------------------

    public static final String NAME = "popular_movies";
    public static final int VERSION = 2;

    private MovieService mMovieService;

    class MovieService {
        Movie addMovie(Movie movie, List<Review> reviews, List<Video> videos) {
            MovieEntity.newInstance(movie).save();
            addReviews(movie, reviews);
            addVideos(movie, videos);
            return movie;
        }

        void removeReviews(Movie movie) {
            new Delete()
                    .from(ReviewEntity.class)
                    .where(Condition.column(ReviewEntity$Table.MOVIEID).is(movie.id))
                    .query();
        }

        void removeVideos(Movie movie) {
            new Delete()
                    .from(VideoEntity.class)
                    .where(Condition.column(VideoEntity$Table.MOVIEID).is(movie.id))
                    .query();
        }

        Movie removeMovie(Movie movie) {
            removeReviews(movie);
            removeVideos(movie);
            MovieEntity.newInstance(movie).delete();
            return movie;
        }

        void addReviews(Movie movie, List<Review> reviews) {
            for (Review review : reviews) {
                ReviewEntity.newInstance(movie, review).save();
            }
        }

        void addVideos(Movie movie, List<Video> videos) {
            for (Video video : videos) {
                VideoEntity.newInstance(movie, video).save();
            }
        }

        List<ReviewEntity> getReviewsByMovieId(int movieId) {
            return new Select()
                    .from(ReviewEntity.class)
                    .where(Condition.column(ReviewEntity$Table.MOVIEID).is(movieId))
                    .queryList();
        }

        List<VideoEntity> getVideosByMovieId(int movieId) {
            return new Select()
                    .from(VideoEntity.class)
                    .where(Condition.column(VideoEntity$Table.MOVIEID).is(movieId))
                    .queryList();
        }

        MovieDetail getMovieDetail(int movieId) {
            ArrayList<Review> reviews = new ArrayList<>();
            for (ReviewEntity entity : getReviewsByMovieId(movieId)) {
                Review review = new Review();
                review.author = entity.author;
                review.content = entity.content;
                review.id = entity.id;
                reviews.add(review);
            }

            ArrayList<Video> videos = new ArrayList<>();
            for (VideoEntity entity : getVideosByMovieId(movieId)) {
                Video video = new Video();
                video.id = entity.id;
                video.key = entity.key;
                videos.add(video);
            }

            return MovieDetail.newInstance(movieId, reviews, videos);
        }

        MoviePager getMoviePager() {
            ArrayList<Movie> movies = new ArrayList<>();
            for (MovieEntity entity : new Select().from(MovieEntity.class).queryList()) {
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

            MoviePager moviePager = new MoviePager();
            moviePager.results = movies;
            moviePager.totalPages = 1;
            moviePager.page = 1;
            return moviePager;
        }
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieDB(Context context) {
        FlowManager.init(context);
        this.mMovieService = new MovieService();
    }

// -------------------------- OTHER METHODS --------------------------

    public Observable<Movie> addMovie(Movie movie, ArrayList<Review> reviews, ArrayList<Video> videos) {
        return Observable.defer(() -> Observable.just(mMovieService.addMovie(movie, reviews, videos)));
    }

    public Observable<MovieDetail> getMovieDetail(int movieId) {
        return Observable.defer(() -> Observable.just(mMovieService.getMovieDetail(movieId)));
    }

    public Observable<MoviePager> getMovies() {
        return Observable.defer(() -> Observable.just(mMovieService.getMoviePager()));
    }

    public boolean movieExists(int movieId) {
        MovieEntity entity = new MovieEntity();
        entity.id = movieId;
        return entity.exists();
    }

    public Observable<Movie> removeMovie(Movie movie) {
        return Observable.defer(() -> Observable.just(mMovieService.removeMovie(movie)));
    }
}
