package com.vaporwarecorp.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import rx.Observable;
import rx.Observer;
import rx.android.app.AppObservable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public abstract class BaseFragment extends Fragment {
// ------------------------------ FIELDS ------------------------------

    private CompositeSubscription mCompositeSubscription;

// -------------------------- INNER CLASSES --------------------------

    interface Callback<T> {
        void failure();

        void success(T value);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PopularMoviesApp.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
    }

    protected final <T> void subscribe(final Observable<T> source, final Callback<T> callback) {
        mCompositeSubscription.add(AppObservable.bindSupportFragment(this, source)
                .subscribe(new Observer<T>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "error on observable");
                        callback.failure();
                    }

                    @Override
                    public void onNext(T t) {
                        callback.success(t);
                    }
                }));
    }
}
