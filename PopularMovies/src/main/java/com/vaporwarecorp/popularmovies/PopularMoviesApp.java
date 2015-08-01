package com.vaporwarecorp.popularmovies;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vaporwarecorp.popularmovies.service.MovieApi;
import com.vaporwarecorp.popularmovies.service.MovieDB;
import timber.log.Timber;

public class PopularMoviesApp extends Application {
    private MovieApi mMovieApi;
    private MovieDB mMovieDB;
    private RefWatcher mRefWatcher;

// -------------------------- STATIC METHODS --------------------------

    public static PopularMoviesApp getApplication(@NonNull Context context) {
        return (PopularMoviesApp) context.getApplicationContext();
    }

    public static MovieApi getMovieApi(@NonNull Context context) {
        return getApplication(context).mMovieApi;
    }

    public static MovieDB getMovieDb(@NonNull Context context) {
        return getApplication(context).mMovieDB;
    }

    public static RefWatcher getRefWatcher(@NonNull Context context) {
        return getApplication(context).mRefWatcher;
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority > Log.WARN) {
                Log.e(tag, message, t);
            }
        }
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate() {
        super.onCreate();

        initLeakCanary();
        initMovieApi();
        initMovieDB();
        initTimber();
    }

    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void initMovieApi() {
        mMovieApi = new MovieApi(getApplicationContext().getCacheDir());
    }

    private void initMovieDB() {
       mMovieDB = new MovieDB(this);
    }

    private void initTimber() {
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new CrashReportingTree());
    }
}
