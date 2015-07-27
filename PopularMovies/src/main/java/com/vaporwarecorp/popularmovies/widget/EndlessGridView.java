package com.vaporwarecorp.popularmovies.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vaporwarecorp.popularmovies.R;

public class EndlessGridView extends FrameLayout implements AbsListView.OnScrollListener {
// ------------------------------ FIELDS ------------------------------

    DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            stopLoadingMore();
        }
    };

    private GridView mGridView;
    private boolean mLoadingMore;
    private OnMoreListener mOnMoreListener;
    private ProgressWheel mProgressWheel;

// -------------------------- INNER CLASSES --------------------------

    public interface OnMoreListener {
        boolean canLoadMore();

        void onMore();
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public EndlessGridView(Context context) {
        this(context, null, 0);
    }

    public EndlessGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface OnScrollListener ---------------------

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mLoadingMore || mOnMoreListener == null || !mOnMoreListener.canLoadMore()) {
            return;
        }
        int lastVisibleItemPosition = mGridView.getLastVisiblePosition();
        if (((totalItemCount - lastVisibleItemPosition) <= itemsLeftToLoadMore() ||
                (totalItemCount - lastVisibleItemPosition) == 0 && totalItemCount > visibleItemCount)) {
            mLoadingMore = true;
            mProgressWheel.spin();
            mOnMoreListener.onMore();
        }
    }

// -------------------------- OTHER METHODS --------------------------

    public int itemsLeftToLoadMore() {
        return mGridView.getNumColumns() * 3;
    }

    public void release() {
        mGridView.getAdapter().unregisterDataSetObserver(mDataSetObserver);
        mGridView.setAdapter(null);
        mGridView.setOnItemClickListener(null);
        mGridView.setOnScrollListener(null);
    }

    public void setAdapter(@NonNull ListAdapter adapter) {
        mGridView.setAdapter(adapter);
        mGridView.getAdapter().registerDataSetObserver(mDataSetObserver);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mGridView.setOnItemClickListener(listener);
    }

    public void setOnMoreListener(@NonNull OnMoreListener onMoreListener) {
        mOnMoreListener = onMoreListener;
    }

    public void stopLoadingMore() {
        mLoadingMore = false;
        mProgressWheel.stopSpinning();
    }

    protected void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.endless_gridview, this);
        mProgressWheel = (ProgressWheel) view.findViewById(android.R.id.progress);
        mGridView = (GridView) view.findViewById(android.R.id.list);
        mGridView.setOnScrollListener(this);
    }
}
