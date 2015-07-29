package com.vaporwarecorp.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Video;

import java.util.List;

import static com.vaporwarecorp.popularmovies.util.ViewUtil.setVideo;

public class VideoAdapter extends BaseAdapter<Video> {
// --------------------------- CONSTRUCTORS ---------------------------

    public VideoAdapter(List<Video> videos) {
        super(videos);
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Adapter ---------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        }
        setVideo((ImageView) convertView, getItem(position).getKey());
        return convertView;
    }
}
