package com.vaporwarecorp.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Movie;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.getMovie;
import static com.vaporwarecorp.popularmovies.util.ParcelUtil.setMovie;
import static com.vaporwarecorp.popularmovies.util.ViewUtil.*;

public class MovieDetailsFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    private Movie mMovie;

// -------------------------- OTHER METHODS --------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        if (savedInstanceState == null) {
            mMovie = getMovie(getActivity().getIntent().getExtras());
        } else {
            mMovie = getMovie(savedInstanceState);
        }

        setText(rootView, R.id.title_text, mMovie.originalTitle);
        setText(rootView, R.id.release_date_text, formatDate(mMovie.releaseDate));
        setMark(rootView, R.id.rating_circle, mMovie.voteAverage);
        setText(rootView, R.id.rating_text, R.string.vote_count, mMovie.voteCount);
        setText(rootView, R.id.overview_text, mMovie.overview);
        setImage(getActivity(), rootView, R.id.backdrop_image, R.drawable.backdrop_placeholder, mMovie.backdropPath);
        setImage(getActivity(), rootView, R.id.poster_image, R.drawable.poster_placeholder, mMovie.posterPath);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setMovie(outState, mMovie);
    }
}
