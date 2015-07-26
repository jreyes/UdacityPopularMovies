package com.vaporwarecorp.popularmovies;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import timber.log.Timber;

public class PopularMoviesApp extends Application {
// ------------------------------ FIELDS ------------------------------

    private RefWatcher mRefWatcher;

// -------------------------- STATIC METHODS --------------------------

    public static void watch(@NonNull Context context) {
        ((PopularMoviesApp) context.getApplicationContext()).mRefWatcher.watch(context);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate() {
        super.onCreate();

        initLeakCanary();
        initTimber();
    }

    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void initTimber() {
        Timber.plant(new Timber.DebugTree());
    }
}
