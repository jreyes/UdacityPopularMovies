package com.vaporwarecorp.popularmovies.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.events.MovieTypeSelectedEvent;
import com.vaporwarecorp.popularmovies.model.MoviePager;
import com.vaporwarecorp.popularmovies.service.MovieApi;
import de.greenrobot.event.EventBus;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.*;

public class MoviesFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    private static final int RECYCLER_COLUMNS = 2;
    private static final int RECYCLER_SPACING = 10;

    MovieApi.Callback mCallback = new MovieApi.Callback() {
        @Override
        public void failure() {
            mLoading = false;
            mProgressWheel.stopSpinning();
        }

        @Override
        public void success(MoviePager moviePager) {
            mLoading = false;
            mProgressWheel.stopSpinning();
            mPage = moviePager.page;
            mTotalPages = moviePager.totalPages;
            mMovieAdapter.addMovies(moviePager.results);
        }
    };
    RecyclerView.ItemDecoration mItemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % RECYCLER_COLUMNS;
            outRect.left = column * RECYCLER_SPACING / RECYCLER_COLUMNS;
            outRect.right = RECYCLER_SPACING - (column + 1) * RECYCLER_SPACING / RECYCLER_COLUMNS;
            if (position >= RECYCLER_COLUMNS) {
                outRect.top = RECYCLER_SPACING;
            }
        }
    };
    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (!mLoading && mPage < mTotalPages &&
                    mLayoutManager.findLastCompletelyVisibleItemPosition() > mMovieAdapter.getItemCount() - 8) {
                refreshMovies();
            }
        }
    };

    private int mAction;
    private GridLayoutManager mLayoutManager;
    private boolean mLoading;
    private MovieAdapter mMovieAdapter;
    private MovieApi mMovieApi;
    private int mPage;
    private ProgressWheel mProgressWheel;
    private RecyclerView mRecyclerView;
    private int mTotalPages;

// -------------------------- OTHER METHODS --------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        setEventBus();
        setProgressWheel(rootView);
        setRecyclerView(rootView);
        setApi(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        mRecyclerView.removeItemDecoration(mItemDecoration);
        mRecyclerView.clearOnScrollListeners();
        mMovieApi.release();
        super.onDestroyView();
    }

    @SuppressWarnings("unused")
    public void onEvent(MovieTypeSelectedEvent event) {
        if (event.type != mAction) {
            mAction = event.type;
            mPage = 0;
            mTotalPages = 0;
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
        mLoading = true;
        mProgressWheel.spin();
        if (mAction == 1) {
            mMovieApi.getPopular(mPage + 1, mCallback);
        } else {
            mMovieApi.getTopRated(mPage + 1, mCallback);
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

    private void setProgressWheel(View view) {
        mProgressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
    }

    private void setRecyclerView(View view) {
        mMovieAdapter = new MovieAdapter();
        mLayoutManager = new GridLayoutManager(this.getActivity(), RECYCLER_COLUMNS);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnScrollListener(mScrollListener);
    }
}
