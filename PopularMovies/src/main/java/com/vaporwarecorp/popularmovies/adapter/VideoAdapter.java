package com.vaporwarecorp.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Video;

import java.util.List;

import static com.vaporwarecorp.popularmovies.util.ViewUtil.setVideo;

public class VideoAdapter extends ArrayAdapter<Video> {
// --------------------------- CONSTRUCTORS ---------------------------

    public VideoAdapter(Context context, List<Video> videos) {
        super(context, R.layout.video_list_item, videos);
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Adapter ---------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.video_list_item, parent, false);
        }
        setVideo((ImageView) convertView, getItem(position).key);
        return convertView;
    }
}
