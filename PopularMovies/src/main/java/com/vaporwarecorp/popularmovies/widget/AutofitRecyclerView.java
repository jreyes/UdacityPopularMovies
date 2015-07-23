package com.vaporwarecorp.popularmovies.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.vaporwarecorp.popularmovies.R;

public class AutofitRecyclerView extends EndlessRecyclerView {
// ------------------------------ FIELDS ------------------------------

    RecyclerView.ItemDecoration mItemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % mSpanCount;
            outRect.left = column * mGutterWidth / mSpanCount;
            outRect.right = mGutterWidth - (column + 1) * mGutterWidth / mSpanCount;
            if (position >= mSpanCount) {
                outRect.top = mGutterWidth;
            }
        }
    };

    private int mColumnWidth;
    private int mGutterWidth;
    private int mSpanCount;

// --------------------------- CONSTRUCTORS ---------------------------

    public AutofitRecyclerView(Context context) {
        super(context);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutofitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public int itemsLeftToLoadMore() {
        return super.itemsLeftToLoadMore() * mSpanCount;
    }

    @Override
    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AutofitRecyclerView);
        try {
            mColumnWidth = a.getDimensionPixelSize(R.styleable.AutofitRecyclerView_arv_columnWidth, convertDpToPixel(185));
            mGutterWidth = a.getDimensionPixelSize(R.styleable.AutofitRecyclerView_arv_gutterWidth, convertDpToPixel(2));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void initLayoutManager() {
        setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (mColumnWidth > 0) {
            mSpanCount = Math.max(1, getMeasuredWidth() / mColumnWidth);
            ((GridLayoutManager) getLayoutManager()).setSpanCount(mSpanCount);
            addItemDecoration(mItemDecoration);
        }
    }

    private int convertDpToPixel(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
