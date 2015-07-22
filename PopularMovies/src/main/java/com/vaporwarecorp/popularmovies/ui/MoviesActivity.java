package com.vaporwarecorp.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.events.MovieSelectedEvent;
import com.vaporwarecorp.popularmovies.events.MovieTypeSelectedEvent;
import de.greenrobot.event.EventBus;

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

// -------------------------- OTHER METHODS --------------------------

    @SuppressWarnings("unused")
    public void onEvent(MovieSelectedEvent event) {
        MovieDetailsActivity.start(this, event.movie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setEventBus();
        setToolbar();
        setToolbarSpinner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setEventBus() {
        EventBus.getDefault().register(this);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setToolbarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.toolbar_actions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }
}
