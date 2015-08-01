package com.vaporwarecorp.popularmovies.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import com.vaporwarecorp.popularmovies.PopularMoviesApp;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public abstract class BaseFragment extends Fragment {
    private CompositeSubscription mCompositeSubscription;
    private NetworkInfo mNetworkInfo;

// -------------------------- OTHER METHODS --------------------------

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        mCompositeSubscription = new CompositeSubscription();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        PopularMoviesApp.getRefWatcher(getActivity()).watch(this);
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        mCompositeSubscription.unsubscribe();
        super.onDestroyView();
    }

    protected void displayError(Throwable throwable, int resourceId) {
        Timber.e(throwable, "Exception thrown");
        if (getView() != null) {
            Snackbar.make(getView(), resourceId, Snackbar.LENGTH_SHORT).show();
        }
    }

    protected void displayMessage(int resourceId) {
        if (getView() != null) {
            Snackbar.make(getView(), resourceId, Snackbar.LENGTH_SHORT).show();
        }
    }

    protected boolean isNetworkAvailable() {
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    protected final <T> void subscribe(final Observable<T> source, Action1<T> onSuccess, Action1<Throwable> onError) {
        mCompositeSubscription.add(AppObservable.bindSupportFragment(this, source).subscribe(onSuccess, onError));
    }
}
