package com.vaporwarecorp.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.events.MovieTypeSelectedEvent;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import com.vaporwarecorp.popularmovies.service.MovieApi;
import com.vaporwarecorp.popularmovies.widget.AutofitRecyclerView;
import com.vaporwarecorp.popularmovies.widget.EndlessRecyclerView;
import de.greenrobot.event.EventBus;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.*;

public class MoviesFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    MovieApi.Callback mCallback = new MovieApi.Callback() {
        @Override
        public void failure() {
            mRecyclerView.stopLoadingMore();
        }

        @Override
        public void success(MoviePager moviePager) {
            mPage = moviePager.page;
            mTotalPages = moviePager.totalPages;
            mMovieAdapter.addMovies(moviePager.results);
        }
    };
    EndlessRecyclerView.OnMoreListener mOnMoreListener = new EndlessRecyclerView.OnMoreListener() {
        @Override
        public void onMore() {
            refreshMovies();
        }
    };

    private int mAction;
    private MovieAdapter mMovieAdapter;
    private MovieApi mMovieApi;
    private int mPage;
    private AutofitRecyclerView mRecyclerView;
    private int mTotalPages;

// -------------------------- OTHER METHODS --------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        setEventBus();
        setRecyclerView(rootView);
        setApi(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        mRecyclerView.release();
        mMovieApi.release();
        super.onDestroyView();
    }

    @SuppressWarnings("unused")
    public void onEvent(MovieTypeSelectedEvent event) {
        if (event.type != mAction) {
            mAction = event.type;
            mPage = 0;
            mTotalPages = 1;
            mMovieAdapter.clearMovies();
            refreshMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setAction(outState, mAction);
        setMovies(outState, mMovieAdapter.getMovies());
        setPage(outState, mPage);
        setTotalPages(outState, mTotalPages);
    }

    private void refreshMovies() {
        if (mPage < mTotalPages) {
            switch (mAction) {
                case 0:
                    mMovieApi.getTopRated(mPage + 1, mCallback);
                    break;
                case 1:
                    mMovieApi.getPopular(mPage + 1, mCallback);
                    break;
                default:
                    break;
            }
        }
    }

    private void setApi(Bundle savedInstanceState) {
        mMovieApi = new MovieApi();
        if (savedInstanceState != null) {
            mAction = getAction(savedInstanceState);
            mPage = getPage(savedInstanceState);
            mTotalPages = getTotalPages(savedInstanceState);
            mMovieAdapter.addMovies(getMovies(savedInstanceState));
        } else {
            mAction = -1;
        }
    }

    private void setEventBus() {
        EventBus.getDefault().register(this);
    }

    private void setRecyclerView(View view) {
        mMovieAdapter = new MovieAdapter();

        mRecyclerView = (AutofitRecyclerView) view.findViewById(R.id.movie_view);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setOnMoreListener(mOnMoreListener);
    }
}
