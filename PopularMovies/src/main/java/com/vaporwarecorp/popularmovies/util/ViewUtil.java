package com.vaporwarecorp.popularmovies.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vaporwarecorp.popularmovies.widget.MarkView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ViewUtil {
// ------------------------------ FIELDS ------------------------------

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat INPUT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat OUTPUT_DATE = SimpleDateFormat.getDateInstance();

// -------------------------- STATIC METHODS --------------------------

    public static String formatDate(String date) {
        try {
            return OUTPUT_DATE.format(INPUT_DATE.parse(date));
        } catch (ParseException e) {
            return "";
        }
    }

    public static String formatString(Resources resources, int resourceId, String arg) {
        return String.format(resources.getString(resourceId), arg);
    }

    public static ImageView setImage(Context context, ImageView imageView, int placeholderId, String value) {
        Glide
                .with(context)
                .load(value)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderId)
                .into(imageView);
        return imageView;
    }

    public static ImageView setImage(Context context, View view, int resourceId, int placeholderId, String value) {
        return setImage(context, (ImageView) view.findViewById(resourceId), placeholderId, value);
    }

    public static MarkView setMark(View view, int resourceId, float value) {
        MarkView markView = (MarkView) view.findViewById(resourceId);
        markView.setMark(value);
        return markView;
    }

    public static TextView setText(View view, int resourceId, String value) {
        TextView textView = (TextView) view.findViewById(resourceId);
        textView.setText(value);
        return textView;
    }

    public static TextView setText(View view, int resourceId, int stringResourceId, String arg) {
        return setText(view, resourceId, formatString(view.getResources(), stringResourceId, arg));
    }
}
