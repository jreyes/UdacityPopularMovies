package com.vaporwarecorp.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.MovieDetail;
import com.vaporwarecorp.popularmovies.model.Video;
import com.vaporwarecorp.popularmovies.service.MovieApi;

import static com.vaporwarecorp.popularmovies.util.ParcelUtil.*;
import static com.vaporwarecorp.popularmovies.util.ViewUtil.*;

public class MovieDetailsFragment extends BaseFragment {
// ------------------------------ FIELDS ------------------------------

    private static final String YOUTUBE_PATH = "http://www.youtube.com/watch?v=";

    Callback<MovieDetail> mMovieDetailCallback = new Callback<MovieDetail>() {
        @Override
        public void failure() {
        }

        @Override
        public void success(MovieDetail movieDetail) {
            updateMovieDetail(movieDetail);
        }
    };
    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Video video = (Video) parent.getItemAtPosition(position);
            Intent intent;
            if (YouTubeIntents.canResolvePlayVideoIntent(getActivity())) {
                intent = YouTubeStandalonePlayer.createVideoIntent(
                        getActivity(),
                        BuildConfig.YOUTUBE_API_KEY,
                        video.getKey(),
                        0,
                        true,
                        false
                );
            } else {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PATH + video.getKey()));
            }
            startActivityForResult(intent, 1);
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
        }
        return rootView;
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

    private void initView(View view) {
        setText(view, R.id.title_text, mMovie.getOriginalTitle());
        setText(view, R.id.release_date_text, formatDate(mMovie.getReleaseDate()));
        setMark(view, R.id.rating_circle, mMovie.getVoteAverage());
        setText(view, R.id.rating_text, R.string.vote_count, mMovie.getVoteCount());
        setText(view, R.id.overview_text, mMovie.getOverview());
        setBackdrop(view, mMovie.getBackdropPath());
        setPoster(view, mMovie.getPosterPath());

        mReviewsTitle = (TextView) view.findViewById(R.id.reviews_title);
        mReviewsView = (ListView) view.findViewById(R.id.reviews_view);
        mVideosTitle = (TextView) view.findViewById(R.id.videos_title);
        mVideosView = (GridView) view.findViewById(R.id.videos_view);

        subscribe(mMovieApi.getMovieDetail(mMovie.getId()), mMovieDetailCallback);
    }

    private void updateMovieDetail(MovieDetail movieDetail) {
        if (!movieDetail.getReviews().isEmpty()) {
            show(mReviewsTitle);
            show(mReviewsView);
            mReviewsView.setAdapter(new ReviewAdapter(movieDetail.getReviews()));
        }
        if (!movieDetail.getVideos().isEmpty()) {
            show(mVideosTitle);
            show(mVideosView);
            mVideosView.setAdapter(new VideoAdapter(movieDetail.getVideos()));
            mVideosView.setOnItemClickListener(mOnItemClickListener);
        }
    }
}
