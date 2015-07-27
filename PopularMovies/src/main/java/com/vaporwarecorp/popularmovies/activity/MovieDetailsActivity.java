package com.vaporwarecorp.popularmovies.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.fragment.MovieDetailsFragment;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.util.ViewUtil;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.KEY_MOVIE;
import static com.vaporwarecorp.popularmovies.util.ParcelUtil.getMovie;

public class MovieDetailsActivity extends AppCompatActivity {
// -------------------------- STATIC METHODS --------------------------

    public static void start(AppCompatActivity activity, Movie movie) {
        activity.startActivity(new Intent(activity, MovieDetailsActivity.class).putExtra(KEY_MOVIE, movie));
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        initView(savedInstanceState);
        initActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PopularMoviesApp.watch(this);
    }

    private void initActionBar() {
        ViewUtil.setActionBar(this, true);
    }

    private void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movie_details);
        if (savedInstanceState == null) {
            MovieDetailsFragment.start(this, getMovie(getIntent().getExtras()));
        }
    }
}
