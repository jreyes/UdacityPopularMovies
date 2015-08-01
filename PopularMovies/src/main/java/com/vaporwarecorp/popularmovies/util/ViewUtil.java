package com.vaporwarecorp.popularmovies.util;

import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.service.MovieApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@BindingMethods({
        @BindingMethod(type = com.vaporwarecorp.popularmovies.widget.MarkView.class,
                attribute = "app:mv_mark",
                method = "setMark")
})
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

    public static void loadImage(ImageView imageView, int placeholderId, String url) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
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

    @BindingAdapter({"bind:backdropPath"})
    public static void setBackdrop(ImageView imageView, String backdropPath) {
        if (backdropPath != null) {
            loadImage(imageView, R.drawable.backdrop_placeholder, MovieApi.BACKDROP_PATH + backdropPath);
        }
    }

    public static void setPoster(View view, String posterPath) {
        setPoster((ImageView) view.findViewById(R.id.poster), posterPath);
    }

    @BindingAdapter({"bind:posterPath"})
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

    public static void setVideo(ImageView imageView, String videoPath) {
        if (videoPath != null) {
            loadImage(imageView, R.drawable.video_placeholder, String.format(MovieApi.VIDEO_PATH, videoPath));
        }
    }
}
