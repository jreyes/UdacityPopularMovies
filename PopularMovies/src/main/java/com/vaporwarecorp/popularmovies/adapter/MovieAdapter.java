package com.vaporwarecorp.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.vaporwarecorp.popularmovies.util.ViewUtil.setPoster;

public class MovieAdapter extends BaseAdapter {
// ------------------------------ FIELDS ------------------------------

    private Context mContext;
    private ArrayList<Movie> mMovies;

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieAdapter(Context context) {
        mContext = context;
        mMovies = new ArrayList<>();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Adapter ---------------------

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        }
        setPoster((ImageView) convertView, getItem(position).posterPath);
        return convertView;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addItems(List<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getItems() {
        return mMovies;
    }
}
