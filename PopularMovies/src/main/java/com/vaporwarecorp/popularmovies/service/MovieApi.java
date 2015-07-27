package com.vaporwarecorp.popularmovies.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.vaporwarecorp.popularmovies.BuildConfig;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import com.vaporwarecorp.popularmovies.model.ReviewPager;
import com.vaporwarecorp.popularmovies.model.VideoPager;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.io.File;
import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MovieApi {
// ------------------------------ FIELDS ------------------------------

    public static final String API_ENDPOINT = "http://api.themoviedb.org/3";
    public static final String BACKDROP_PATH = "http://image.tmdb.org/t/p/w500";
    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String VIDEO_PATH = "http://i.ytimg.com/vi/%s/default.jpg";

    private CompositeSubscription mCompositeSubscription;
    private MovieService mMovieService;

// -------------------------- INNER CLASSES --------------------------

    public interface Callback<T> {
        void failure();

        void success(T value);
    }

    public interface MovieService {
        @GET("/movie/popular")
        Observable<MoviePager> getPopular(@Query("page") int page);

        @GET("/movie/{id}/reviews")
        Observable<ReviewPager> getReviews(@Path("id") int movieId);

        @GET("/movie/top_rated")
        Observable<MoviePager> getTopRated(@Query("page") int page);

        @GET("/movie/{id}/videos")
        Observable<VideoPager> getVideos(@Path("id") int movieId);
    }

    public class MovieObserver<T> implements Observer<T> {
        Callback<T> mCallback;

        public MovieObserver(Callback<T> callback) {
            mCallback = callback;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mCallback.failure();
        }

        @Override
        public void onNext(T t) {
            mCallback.success(t);
        }
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieApi(File cacheDir) {
        this.mMovieService = new RestAdapter.Builder()
                .setClient(initOkClient(initCache(cacheDir)))
                .setEndpoint(API_ENDPOINT)
                .setConverter(gsonConverter())
                .setRequestInterceptor(request -> request.addQueryParam("api_key", BuildConfig.MOVIEDB_API_KEY))
                .build()
                .create(MovieService.class);
        this.mCompositeSubscription = new CompositeSubscription();
    }

// -------------------------- OTHER METHODS --------------------------

    public void getPopular(int page, Callback<MoviePager> callback) {
        subscribe(mMovieService.getPopular(page), callback);
    }

    public void getReviews(int movieId, Callback<ReviewPager> callback) {
        subscribe(mMovieService.getReviews(movieId), callback);
    }

    public void getTopRated(int page, Callback<MoviePager> callback) {
        subscribe(mMovieService.getTopRated(page), callback);
    }

    public void getVideos(int movieId, Callback<VideoPager> callback) {
        subscribe(mMovieService.getVideos(movieId), callback);
    }

    public void release() {
        mCompositeSubscription.unsubscribe();
    }

    private GsonConverter gsonConverter() {
        return new GsonConverter(
                new GsonBuilder()
                        .serializeNulls()
                        .setDateFormat("yyyy-MM-dd")
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
        );
    }

    private Cache initCache(File cacheDir) {
        try {
            return new Cache(new File(cacheDir, "http"), 10 * 1024 * 1024);
        } catch (IOException e) {
            return null;
        }
    }

    private OkClient initOkClient(Cache cache) {
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.setConnectTimeout(10, SECONDS);
        client.setReadTimeout(10, SECONDS);
        client.setWriteTimeout(10, SECONDS);
        return new OkClient(client);
    }

    private <T> void subscribe(Observable<T> observable, Callback<T> callback) {
        mCompositeSubscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MovieObserver<>(callback))
        );
    }
}
