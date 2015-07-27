package com.vaporwarecorp.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.events.MovieSelectedEvent;
import com.vaporwarecorp.popularmovies.events.MovieTypeSelectedEvent;
import com.vaporwarecorp.popularmovies.fragment.MovieDetailsFragment;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.util.ViewUtil;
import de.greenrobot.event.EventBus;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.getMovie;
import static com.vaporwarecorp.popularmovies.util.ParcelUtil.setMovie;

public class MoviesActivity extends AppCompatActivity {
// ------------------------------ FIELDS ------------------------------

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EventBus.getDefault().post(new MovieTypeSelectedEvent(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private Movie mMovie;
    private boolean mTwoPane;

// -------------------------- OTHER METHODS --------------------------

    @SuppressWarnings("unused")
    public void onEvent(MovieSelectedEvent event) {
        displayMovieDetails(event.movie, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        initActionBar();
        initToolbarSpinner();
        initEventBus();
    }

    @Override
    protected void onDestroy() {
        PopularMoviesApp.watch(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setMovie(outState, mMovie);
    }

    private void displayMovieDetails(Movie movie, boolean clicked) {
        mMovie = movie;
        if (mTwoPane) {
            MovieDetailsFragment.start(this, mMovie);
        } else if (clicked) {
            MovieDetailsActivity.start(this, mMovie);
        }
    }

    private void initActionBar() {
        ViewUtil.setActionBar(this, false);
    }

    private void initEventBus() {
        EventBus.getDefault().register(this);
    }

    private void initToolbarSpinner() {
        ViewUtil.setSpinner(this, R.array.toolbar_actions, onItemSelectedListener);
    }

    private void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movies);
        mTwoPane = findViewById(R.id.movie_details_container) != null;
        if (savedInstanceState != null) {
            displayMovieDetails(getMovie(savedInstanceState), false);
        }
    }
}