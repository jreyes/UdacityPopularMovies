package com.vaporwarecorp.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.events.MovieSelectedEvent;
import com.vaporwarecorp.popularmovies.model.Movie;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.vaporwarecorp.popularmovies.util.ViewUtil.setPoster;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
// ------------------------------ FIELDS ------------------------------

    private ArrayList<Movie> mMovies;

// -------------------------- INNER CLASSES --------------------------

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;

        public MovieViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new MovieSelectedEvent(getMovies().get(getAdapterPosition())));
        }
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public MovieAdapter() {
        this.mMovies = new ArrayList<>();
    }

// -------------------------- OTHER METHODS --------------------------

    public void addMovies(List<Movie> movies) {
        mMovies.addAll(movies);
        notifyItemRangeInserted(mMovies.size() - movies.size(), movies.size());
    }

    public void clearMovies() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public ArrayList<Movie> getMovies() {
        return mMovies;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int i) {
        setPoster(movieViewHolder.poster, mMovies.get(i).posterPath);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        return new MovieViewHolder(view);
    }
}
