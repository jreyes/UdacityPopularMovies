package com.vaporwarecorp.popularmovies.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vaporwarecorp.popularmovies.R;

public class EndlessRecyclerView extends FrameLayout {
// ------------------------------ FIELDS ------------------------------

    RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            stopLoadingMore();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            stopLoadingMore();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            stopLoadingMore();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            stopLoadingMore();
        }

        @Override
        public void onChanged() {
            super.onChanged();
            stopLoadingMore();
        }
    };
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
            if (((totalItemCount - lastVisibleItemPosition) <= itemsLeftToLoadMore() ||
                    (totalItemCount - lastVisibleItemPosition) == 0 && totalItemCount > visibleItemCount) &&
                    !mLoadingMore) {
                mLoadingMore = true;
                if (mOnMoreListener != null) {
                    mProgressWheel.spin();
                    mOnMoreListener.onMore();
                }
            }
        }
    };

    private RecyclerView.ItemDecoration mItemDecoration;
    private LinearLayoutManager mLayoutManager;
    private boolean mLoadingMore;
    private OnMoreListener mOnMoreListener;
    private ProgressWheel mProgressWheel;
    private RecyclerView mRecyclerView;

// -------------------------- INNER CLASSES --------------------------

    public interface OnMoreListener {
        void onMore();
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public EndlessRecyclerView(Context context) {
        this(context, null, 0);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initLayoutManager();
        initView();
    }

// -------------------------- OTHER METHODS --------------------------

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mItemDecoration = itemDecoration;
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public boolean isLoadingMore() {
        return mLoadingMore;
    }

    public int itemsLeftToLoadMore() {
        return 3;
    }

    public void release() {
        mRecyclerView.removeItemDecoration(mItemDecoration);
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.setAdapter(null);

        mOnMoreListener = null;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mProgressWheel.stopSpinning();
        adapter.registerAdapterDataObserver(mAdapterDataObserver);
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public void setOnMoreListener(OnMoreListener onMoreListener) {
        mOnMoreListener = onMoreListener;
    }

    public void stopLoadingMore() {
        mLoadingMore = false;
        mProgressWheel.stopSpinning();
    }

    protected void initAttrs(AttributeSet attrs) {
    }

    protected void initLayoutManager() {
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void initView() {
        if (isInEditMode()) {
            return;
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.endless_recyclerview, this);
        mProgressWheel = (ProgressWheel) view.findViewById(android.R.id.progress);
        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }
}
