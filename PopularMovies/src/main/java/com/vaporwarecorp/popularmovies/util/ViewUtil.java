package com.vaporwarecorp.popularmovies.util;

import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.service.MovieApi;
import com.vaporwarecorp.popularmovies.widget.MarkView;
import timber.log.Timber;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewUtil {
// ------------------------------ FIELDS ------------------------------

    private static final DateFormat OUTPUT_DATE = SimpleDateFormat.getDateInstance();

// -------------------------- STATIC METHODS --------------------------

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return OUTPUT_DATE.format(date);
    }

    public static String formatString(Resources resources, int resourceId, String arg) {
        return String.format(resources.getString(resourceId), arg);
    }

    public static void hide(View view) {
        view.setVisibility(View.GONE);
    }

    public static void loadImage(ImageView imageView, int placeholderId, String url) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .placeholder(placeholderId)
                .into(imageView);
    }

    public static void setActionBar(AppCompatActivity activity, boolean displayHomeAsUp) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUp);
        }
    }

    public static void setBackdrop(View view, String backdropPath) {
        if (backdropPath != null) {
            loadImage((ImageView) view.findViewById(R.id.backdrop), R.drawable.backdrop_placeholder, MovieApi.BACKDROP_PATH + backdropPath);
        }
    }

    public static void setMark(View view, int resourceId, float value) {
        ((MarkView) view.findViewById(resourceId)).setMark(value);
    }

    public static void setPoster(View view, String posterPath) {
        setPoster((ImageView) view.findViewById(R.id.poster), posterPath);
    }

    public static void setPoster(ImageView imageView, String posterPath) {
        if (posterPath != null) {
            loadImage(imageView, R.drawable.poster_placeholder, MovieApi.POSTER_PATH + posterPath);
        }
    }

    public static void setSpinner(AppCompatActivity activity, int arrayResourceId, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) activity.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public static void setText(View view, int resourceId, String value) {
        ((TextView) view.findViewById(resourceId)).setText(value);
    }

    public static void setText(View view, int resourceId, int stringResourceId, String arg) {
        setText(view, resourceId, formatString(view.getResources(), stringResourceId, arg));
    }

    public static void setVideo(ImageView imageView, String videoPath) {
        if (videoPath != null) {
            String video = String.format(MovieApi.VIDEO_PATH, videoPath);
            Timber.d("setVideo - " + video);
            loadImage(imageView, R.drawable.video_placeholder, String.format(MovieApi.VIDEO_PATH, videoPath));
        }
    }

    public static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }
}
