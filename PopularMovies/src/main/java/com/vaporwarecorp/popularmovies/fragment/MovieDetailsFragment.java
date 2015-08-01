package com.vaporwarecorp.popularmovies.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.vaporwarecorp.popularmovies.BuildConfig;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.adapter.ReviewAdapter;
import com.vaporwarecorp.popularmovies.adapter.VideoAdapter;
import com.vaporwarecorp.popularmovies.databinding.FragmentMovieDetailsBinding;
import com.vaporwarecorp.popularmovies.events.FavoriteAddedEvent;
import com.vaporwarecorp.popularmovies.events.FavoriteRemovedEvent;
import com.vaporwarecorp.popularmovies.model.Movie;
import com.vaporwarecorp.popularmovies.model.Review;
import com.vaporwarecorp.popularmovies.model.Video;
import com.vaporwarecorp.popularmovies.service.MovieApi;
import com.vaporwarecorp.popularmovies.service.MovieDB;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;

import static com.vaporwarecorp.popularmovies.PopularMoviesApp.getMovieApi;
import static com.vaporwarecorp.popularmovies.util.ParcelUtil.*;

public class MovieDetailsFragment extends BaseFragment {
// ------------------------------ FIELDS ------------------------------

    public static final String YOUTUBE_PATH = "http://www.youtube.com/watch?v=";
    public static final int YOUTUBE_PLAY_INTENT = 1;

    private FragmentMovieDetailsBinding mBinding;
    private MovieDB mMovieDB;

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

    public static void stop(AppCompatActivity activity) {
        Fragment fragment = activity
                .getSupportFragmentManager()
                .findFragmentById(R.id.movie_details_container);
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        setHasOptionsMenu(true);
        initBindings(view, savedInstanceState);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            if (mBinding.getVideos().isEmpty()) {
                displayMessage(R.string.share_video_error);
                return false;
            }
            shareVideo(mBinding.getVideos().get(0));
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setMovie(outState, mBinding.getMovie());
        setVideos(outState, mBinding.getVideos());
        setReviews(outState, mBinding.getReviews());
    }

    public void shareVideo(Video video) {
        Intent intent = ShareCompat.IntentBuilder
                .from(getActivity())
                .setType("text/plain")
                .setText(getString(R.string.share_video, video.name, YOUTUBE_PATH + video.key))
                .getIntent();
        startActivity(Intent.createChooser(intent, getString(R.string.share_video_title)));
    }

    private void initBindings(View view, Bundle savedInstanceState) {
        // fix for Glide loading when the activity is being destroyed
        if (getActivity().isDestroyed()) {
            return;
        }

        mMovieDB = PopularMoviesApp.getMovieDb(getActivity());

        mBinding = DataBindingUtil.bind(view);
        mBinding.addFavorite.setOnClickListener(v -> onAddFavoriteClicked());
        mBinding.removeFavorite.setOnClickListener(v -> onRemoveFavoriteClicked());

        if (savedInstanceState == null) {
            mBinding.setMovie(getMovie(getArguments()));
            MovieApi movieApi = getMovieApi(getActivity());
            subscribe(
                    movieApi.getMovieDetail(mBinding.getMovie().id),
                    movieDetail -> updateMovieDetail(movieDetail.reviews, movieDetail.videos),
                    throwable -> displayError(throwable, R.string.movie_details_error)
            );
        } else {
            mBinding.setMovie(getMovie(savedInstanceState));
            updateMovieDetail(getReviews(savedInstanceState), getVideos(savedInstanceState));
        }
    }

    private void onAddFavoriteClicked() {
        subscribe(
                mMovieDB.addMovie(mBinding.getMovie(), mBinding.getReviews(), mBinding.getVideos()),
                this::onFavoriteAdded,
                throwable -> displayError(throwable, R.string.favorite_added_error)
        );
    }

    private void onFavoriteAdded(Movie movie) {
        mBinding.setFavorite(true);
        displayMessage(R.string.favorite_added);
        EventBus.getDefault().post(new FavoriteAddedEvent(movie));
    }

    private void onFavoriteRemoved(Movie movie) {
        mBinding.setFavorite(false);
        displayMessage(R.string.favorite_removed);
        EventBus.getDefault().post(new FavoriteRemovedEvent(movie));
    }

    private void onRemoveFavoriteClicked() {
        subscribe(
                mMovieDB.removeMovie(mBinding.getMovie()),
                this::onFavoriteRemoved,
                throwable -> displayError(throwable, R.string.favorite_removed_error)
        );
    }

    private void playVideo(Video video) {
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
        startActivityForResult(intent, YOUTUBE_PLAY_INTENT);
    }

    private void updateMovieDetail(ArrayList<Review> reviews, ArrayList<Video> videos) {
        mBinding.setFavorite(mMovieDB.movieExists(mBinding.getMovie().id));
        mBinding.setReviews(reviews);
        mBinding.setVideos(videos);
        if (!videos.isEmpty()) {
            mBinding.videosView.setOnItemClickListener(
                    (parent, view, position, id) -> playVideo((Video) parent.getItemAtPosition(position))
            );
            mBinding.playButton.setOnClickListener(v -> playVideo(mBinding.getVideos().get(0)));
        }
        mBinding.videosView.setAdapter(new VideoAdapter(videos));
        mBinding.reviewsView.setAdapter(new ReviewAdapter(reviews));
    }
}
