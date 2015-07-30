package com.vaporwarecorp.popularmovies.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.vaporwarecorp.popularmovies.BuildConfig;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.adapter.ReviewAdapter;
import com.vaporwarecorp.popularmovies.adapter.VideoAdapter;
import com.vaporwarecorp.popularmovies.databinding.FragmentMovieDetailsBinding;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.MovieDetail;
import com.vaporwarecorp.popularmovies.model.Review;
import com.vaporwarecorp.popularmovies.model.Video;
import com.vaporwarecorp.popularmovies.service.MovieApi;

import java.util.ArrayList;

import timber.log.Timber;

import static com.vaporwarecorp.popularmovies.PopularMoviesApp.getMovieApi;
import static com.vaporwarecorp.popularmovies.util.ParcelUtil.*;

public class MovieDetailsFragment extends BaseFragment {
// ------------------------------ FIELDS ------------------------------

    private static final String YOUTUBE_PATH = "http://www.youtube.com/watch?v=";

    Callback<MovieDetail> mCallback = new Callback<MovieDetail>() {
        @Override
        public void failure() {
        }

        @Override
        public void success(MovieDetail movieDetail) {
            updateMovieDetail(movieDetail.getVideos(), movieDetail.getReviews());
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

    private FragmentMovieDetailsBinding mBinding;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        initBindings(view, savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setMovie(outState, mBinding.getMovie());
        setVideos(outState, mBinding.getVideos());
        setReviews(outState, mBinding.getReviews());
    }

    private void initBindings(View view, Bundle savedInstanceState) {
        // fix for Glide loading when the activity is being destroyed
        if (getActivity().isDestroyed()) {
            Timber.d("called fragment when activity is being destroyed");
            return;
        }

        mBinding = DataBindingUtil.bind(view);
        if (savedInstanceState == null) {
            mBinding.setMovie(getMovie(getArguments()));
            MovieApi movieApi = getMovieApi(getActivity());
            subscribe(movieApi.getMovieDetail(mBinding.getMovie().getId()), mCallback);
        } else {
            mBinding.setMovie(getMovie(savedInstanceState));
            updateMovieDetail(getVideos(savedInstanceState), getReviews(savedInstanceState));
        }
    }

    private void updateMovieDetail(ArrayList<Video> videos, ArrayList<Review> reviews) {
        mBinding.setVideos(videos);
        if (!videos.isEmpty()) {
            mBinding.videosView.setOnItemClickListener(mOnItemClickListener);
        }
        mBinding.videosView.setAdapter(new VideoAdapter(videos));

        mBinding.setReviews(reviews);
        mBinding.reviewsView.setAdapter(new ReviewAdapter(reviews));
    }
}
