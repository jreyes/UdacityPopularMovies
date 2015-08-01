package com.vaporwarecorp.popularmovies.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.vaporwarecorp.popularmovies.BuildConfig;
import com.vaporwarecorp.popularmovies.model.MovieDetail;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import com.vaporwarecorp.popularmovies.model.ReviewPager;
import com.vaporwarecorp.popularmovies.model.VideoPager;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

import java.io.File;
import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MovieApi {
// ------------------------------ FIELDS ------------------------------

    public static final String API_ENDPOINT = "http://api.themoviedb.org/3";
    public static final String BACKDROP_PATH = "http://image.tmdb.org/t/p/w500";
    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185";
    public static final String VIDEO_PATH = "http://i.ytimg.com/vi/%s/default.jpg";

    private MovieService mMovieService;

    class AuthenticatorRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam("api_key", BuildConfig.MOVIEDB_API_KEY);
        }
    }

    interface MovieService {
        @GET("/movie/popular") Observable<MoviePager> getPopular(@Query("page") int page);

        @GET("/movie/{id}/reviews") Observable<ReviewPager> getReviews(@Path("id") int movieId);

        @GET("/movie/top_rated") Observable<MoviePager> getTopRated(@Query("page") int page);

        @GET("/movie/{id}/videos") Observable<VideoPager> getVideos(@Path("id") int movieId);
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieApi(File cacheDir) {
        this.mMovieService = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setClient(initOkClient(initCache(cacheDir)))
                .setEndpoint(API_ENDPOINT)
                .setConverter(gsonConverter())
                .setRequestInterceptor(new AuthenticatorRequestInterceptor())
                .build()
                .create(MovieService.class);
    }

// -------------------------- OTHER METHODS --------------------------

    public Observable<MovieDetail> getMovieDetail(int movieId) {
        return Observable.zip(
                mMovieService.getVideos(movieId),
                mMovieService.getReviews(movieId),
                (videoPager, reviewPager) -> MovieDetail.newInstance(movieId, reviewPager.results, videoPager.results)
        );
    }

    public Observable<MoviePager> getPopular(int page) {
        return mMovieService.getPopular(page);
    }

    public Observable<MoviePager> getTopRated(int page) {
        return mMovieService.getTopRated(page);
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
}
