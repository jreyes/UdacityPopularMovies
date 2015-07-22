package com.vaporwarecorp.popularmovies.service;

import com.google.gson.*;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.lang.reflect.Type;

public class MovieApi {
// ------------------------------ FIELDS ------------------------------

    public static final String THEMOVIEDB_API_ENDPOINT = "http://api.themoviedb.org/3";
    public static final String THEMOVIEDB_API_KEY = "";
    public static final String THEMOVIEDB_BACKDROP_PATH = "http://image.tmdb.org/t/p/w500";
    public static final String THEMOVIEDB_POSTER_PATH = "http://image.tmdb.org/t/p/w185";

    private final CompositeSubscription mCompositeSubscription;
    private final MovieService mMovieService;

// -------------------------- INNER CLASSES --------------------------

    private class ApiKeyAuthenticator implements RequestInterceptor {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            request.addQueryParam("api_key", THEMOVIEDB_API_KEY);
        }
    }

    private class MovieDeserializer implements JsonDeserializer<Movie> {
        @Override
        public Movie deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            if (object == null) {
                return null;
            }
            return new Movie(
                    fromJsonString(object, "backdrop_path", THEMOVIEDB_BACKDROP_PATH),
                    object.get("id").getAsInt(),
                    fromJsonString(object, "original_title"),
                    fromJsonString(object, "overview"),
                    fromJsonString(object, "poster_path", THEMOVIEDB_POSTER_PATH),
                    fromJsonString(object, "release_date"),
                    object.get("vote_average").getAsFloat(),
                    fromJsonString(object, "vote_count")
            );
        }
    }

    private class MoviePagerObserver implements Observer<MoviePager> {
        private Callback mCallback;

        public MoviePagerObserver(Callback callback) {
            mCallback = callback;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            if (mCallback != null) {
                mCallback.failure();
            }
        }

        @Override
        public void onNext(MoviePager moviePager) {
            if (mCallback != null) {
                mCallback.success(moviePager);
            }
        }
    }

    public interface Callback {
        void failure();

        void success(MoviePager moviePager);
    }

    private interface MovieService {
        @GET("/movie/popular")
        Observable<MoviePager> getPopular(@Query("page") int page);

        @GET("/movie/top_rated")
        Observable<MoviePager> getTopRated(@Query("page") int page);
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Movie.class, new MovieDeserializer())
                .create();

        this.mMovieService = new RestAdapter.Builder()
                .setEndpoint(THEMOVIEDB_API_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new ApiKeyAuthenticator())
                .build()
                .create(MovieService.class);

        this.mCompositeSubscription = new CompositeSubscription();
    }

// -------------------------- OTHER METHODS --------------------------

    public void getPopular(int page, Callback callback) {
        mCompositeSubscription.add(mMovieService
                .getPopular(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MoviePagerObserver(callback)));
    }

    public void getTopRated(int page, Callback callback) {
        mCompositeSubscription.add(mMovieService
                .getTopRated(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MoviePagerObserver(callback)));
    }

    public void release() {
        mCompositeSubscription.unsubscribe();
    }

    private String fromJsonString(JsonObject object, String key) {
        return fromJsonString(object, key, "");
    }

    private String fromJsonString(JsonObject object, String key, String append) {
        return object.get(key).isJsonNull() ? null : append + object.get(key).getAsString();
    }
}
