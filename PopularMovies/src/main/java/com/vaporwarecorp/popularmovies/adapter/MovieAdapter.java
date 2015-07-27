package com.vaporwarecorp.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Movie;

import java.util.ArrayList;

import static com.vaporwarecorp.popularmovies.util.ViewUtil.setPoster;

public class MovieAdapter extends BaseAdapter<Movie> {
// --------------------------- CONSTRUCTORS ---------------------------

    public MovieAdapter() {
        super(new ArrayList<>());
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Adapter ---------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        }
        setPoster((ImageView) convertView, getItem(position).posterPath);
        return convertView;
    }
}
