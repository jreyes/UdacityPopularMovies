package com.vaporwarecorp.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.adapter.MovieAdapter;
import com.vaporwarecorp.popularmovies.events.FavoriteAddedEvent;
import com.vaporwarecorp.popularmovies.events.FavoriteRemovedEvent;
import com.vaporwarecorp.popularmovies.events.MovieSelectedEvent;
import com.vaporwarecorp.popularmovies.events.MovieTypeSelectedEvent;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import com.vaporwarecorp.popularmovies.service.MovieApi;
import com.vaporwarecorp.popularmovies.service.MovieDB;
import com.vaporwarecorp.popularmovies.widget.EndlessGridView;
import de.greenrobot.event.EventBus;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.*;

public class MoviesFragment extends BaseFragment
        implements EndlessGridView.OnMoreListener, AdapterView.OnItemClickListener {
// ------------------------------ FIELDS ------------------------------

    public static final int VIEW_TYPE_FAVORITES = 2;
    public static final int VIEW_TYPE_HIGHEST_RATED = 0;
    public static final int VIEW_TYPE_MOST_POPULAR = 1;

    private MovieAdapter mMovieAdapter;
    private MovieApi mMovieApi;
    private MovieDB mMovieDB;
    private int mPage;
    private EndlessGridView mRecyclerView;
    private int mTotalPages;
    private int mViewType;

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface OnItemClickListener ---------------------

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        postMovieSelected((Movie) parent.getItemAtPosition(position), true);
    }

// --------------------- Interface OnMoreListener ---------------------

    @Override
    public boolean canLoadMore() {
        return mPage < mTotalPages && (isNetworkAvailable() || mViewType == VIEW_TYPE_FAVORITES);
    }

    @Override
    public void onMore() {
        switch (mViewType) {
            case VIEW_TYPE_HIGHEST_RATED:
                subscribe(mMovieApi.getTopRated(mPage + 1), this::onMoviePagerSuccess, this::onMoviePagerError);
                break;
            case VIEW_TYPE_MOST_POPULAR:
                subscribe(mMovieApi.getPopular(mPage + 1), this::onMoviePagerSuccess, this::onMoviePagerError);
                break;
            case VIEW_TYPE_FAVORITES:
                subscribe(mMovieDB.getMovies(), this::onMoviePagerSuccess, this::onMoviePagerError);
                break;
            default:
                throw new IllegalArgumentException("Unknown view type: " + mViewType);
        }
    }

// -------------------------- OTHER METHODS --------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        initRecyclerView(rootView);
        initApi(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.release();
        super.onDestroyView();
    }

    @SuppressWarnings("unused")
    public void onEvent(MovieTypeSelectedEvent event) {
        if (event.type == mViewType) {
            return;
        }

        mViewType = event.type;
        mPage = 0;
        mTotalPages = 1;
        mMovieAdapter.clearItems();
    }

    @SuppressWarnings("unused")
    public void onEvent(FavoriteRemovedEvent event) {
        if (mViewType != VIEW_TYPE_FAVORITES) {
            return;
        }
        mMovieAdapter.removeItem(event.movie);
        if (!mMovieAdapter.getItems().isEmpty()) {
            postMovieSelected(mMovieAdapter.getItems().get(0), false);
        } else {
            postMovieSelected(null, false);
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(FavoriteAddedEvent event) {
        if (mViewType != VIEW_TYPE_FAVORITES) {
            return;
        }
        mMovieAdapter.addItem(event.movie);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setEventType(outState, mViewType);
        setMovies(outState, mMovieAdapter.getItems());
        setPage(outState, mPage);
        setTotalPages(outState, mTotalPages);
    }

    private void initApi(Bundle savedInstanceState) {
        mMovieDB = PopularMoviesApp.getMovieDb(getActivity());
        mMovieApi = PopularMoviesApp.getMovieApi(getActivity());
        if (savedInstanceState == null) {
            mViewType = -1;
        } else {
            mViewType = getEventType(savedInstanceState);
            mPage = getPage(savedInstanceState);
            mTotalPages = getTotalPages(savedInstanceState);
            mMovieAdapter.addItems(getMovies(savedInstanceState));
        }
    }

    private void initRecyclerView(View view) {
        mMovieAdapter = new MovieAdapter();

        mRecyclerView = (EndlessGridView) view.findViewById(R.id.movie_view);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setOnItemClickListener(this);
        mRecyclerView.setOnMoreListener(this);
    }

    private void onMoviePagerError(Throwable throwable) {
        mRecyclerView.stopLoadingMore();
    }

    private void onMoviePagerSuccess(MoviePager moviePager) {
        mPage = moviePager.page;
        mTotalPages = moviePager.totalPages;
        mMovieAdapter.addItems(moviePager.results);
        if (mPage == 1 && !moviePager.results.isEmpty()) {
            postMovieSelected(moviePager.results.get(0), false);
        }
    }

    private void postMovieSelected(Movie movie, boolean clicked) {
        EventBus.getDefault().post(new MovieSelectedEvent(movie, clicked));
    }
}
