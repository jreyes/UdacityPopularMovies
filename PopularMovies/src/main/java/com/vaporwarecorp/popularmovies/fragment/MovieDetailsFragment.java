package com.vaporwarecorp.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.vaporwarecorp.popularmovies.BuildConfig;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.adapter.ReviewAdapter;
import com.vaporwarecorp.popularmovies.adapter.VideoAdapter;
import com.vaporwarecorp.popularmovies.model.*;
import com.vaporwarecorp.popularmovies.service.MovieApi;

import java.util.List;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.getMovie;
import static com.vaporwarecorp.popularmovies.util.ParcelUtil.setMovie;
import static com.vaporwarecorp.popularmovies.util.ViewUtil.*;

public class MovieDetailsFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    private static final String YOUTUBE_PATH = "http://www.youtube.com/watch?v=";

    MovieApi.Callback<ReviewPager> mReviewsCallback = new MovieApi.Callback<ReviewPager>() {
        @Override
        public void failure() {
        }

        @Override
        public void success(ReviewPager reviewPager) {
            updateReviewsView(reviewPager.results);
        }
    };
    MovieApi.Callback<VideoPager> mVideosCallback = new MovieApi.Callback<VideoPager>() {
        @Override
        public void failure() {
        }

        @Override
        public void success(VideoPager videoPager) {
            updateVideosView(videoPager.results);
        }
    };

    private Movie mMovie;
    private MovieApi mMovieApi;
    private TextView mReviewsTitle;
    private ListView mReviewsView;
    private TextView mVideosTitle;
    private GridView mVideosView;

// -------------------------- STATIC METHODS --------------------------

    public static void start(AppCompatActivity activity, Movie movie) {
        Bundle arguments = new Bundle();
        setMovie(arguments, movie);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(arguments);

        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movie_details_container, fragment)
                .commit();
    }

// -------------------------- OTHER METHODS --------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        // fix for Glide loading when the activity is being destroyed
        if (!getActivity().isDestroyed()) {
            initApi(savedInstanceState);
            initView(rootView);
            initVideosView(rootView);
            initReviewsView(rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PopularMoviesApp.watch(getActivity());
    }

    @Override
    public void onDestroyView() {
        if (mMovieApi != null) {
            mMovieApi.release();
        }
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setMovie(outState, mMovie);
    }

    private void initApi(Bundle savedInstanceState) {
        mMovieApi = PopularMoviesApp.getMovieApi(getActivity());
        mMovie = savedInstanceState != null ? getMovie(savedInstanceState) : getMovie(getArguments());
    }

    private void initReviewsView(View view) {
        mReviewsTitle = (TextView) view.findViewById(R.id.reviews_title);
        mReviewsView = (ListView) view.findViewById(R.id.reviews_view);
        mMovieApi.getReviews(mMovie.id, mReviewsCallback);
    }

    private void initVideosView(View view) {
        mVideosTitle = (TextView) view.findViewById(R.id.videos_title);
        mVideosView = (GridView) view.findViewById(R.id.videos_view);
        mMovieApi.getVideos(mMovie.id, mVideosCallback);
    }

    private void initView(View view) {
        setText(view, R.id.title_text, mMovie.originalTitle);
        setText(view, R.id.release_date_text, formatDate(mMovie.releaseDate));
        setMark(view, R.id.rating_circle, mMovie.voteAverage);
        setText(view, R.id.rating_text, R.string.vote_count, mMovie.voteCount);
        setText(view, R.id.overview_text, mMovie.overview);
        setBackdrop(view, mMovie.backdropPath);
        setPoster(view, mMovie.posterPath);
    }

    private void updateReviewsView(List<Review> reviews) {
        if (reviews.isEmpty()) {
            hide(mReviewsTitle);
            hide(mReviewsView);
        } else {
            show(mReviewsTitle);
            show(mReviewsView);
            mReviewsView.setAdapter(new ReviewAdapter(reviews));
        }
    }

    private void updateVideosView(List<Video> videos) {
        if (videos.isEmpty()) {
            hide(mVideosTitle);
            hide(mVideosView);
        } else {
            show(mVideosTitle);
            show(mVideosView);
            mVideosView.setAdapter(new VideoAdapter(videos));
            mVideosView.setOnItemClickListener((parent, view, position, id) -> {
                Video video = (Video) parent.getItemAtPosition(position);
                Intent intent;
                if (YouTubeIntents.canResolvePlayVideoIntent(getActivity())) {
                    intent = YouTubeStandalonePlayer.createVideoIntent(
                            getActivity(),
                            BuildConfig.YOUTUBE_API_KEY,
                            video.key,
                            0,
                            true,
                            false
                    );
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PATH + video.key));
                }
                startActivityForResult(intent, 1);
            });
        }
    }
}
