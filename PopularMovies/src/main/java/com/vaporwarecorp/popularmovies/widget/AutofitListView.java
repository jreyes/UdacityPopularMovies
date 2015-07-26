package com.vaporwarecorp.popularmovies.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AutofitListView extends ListView {
// --------------------------- CONSTRUCTORS ---------------------------

    public AutofitListView(Context context) {
        super(context);
    }

    public AutofitListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutofitListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        getLayoutParams().height = getMeasuredHeight();
    }
}
