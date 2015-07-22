package com.vaporwarecorp.popularmovies.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.vaporwarecorp.popularmovies.R;

/**
 * @author Ihsan Isik
 *         <p/>
 *         An android custom view that displays a circle with a colored arc given a mark.
 *         <p/>
 */
public class MarkView extends View {
// ------------------------------ FIELDS ------------------------------

    private static final String STATE_MARK = "mark";
    private static final String STATE_MAX = "max";
    private static final String STATE_STATE = "saved_instance";
    private static final String STATE_STROKE_COLORS = "stroke_colors";
    private static final String STATE_STROKE_WIDTH = "stroke_width";
    private static final String STATE_TEXT_COLOR = "text_color";
    private static final String STATE_TEXT_SIZE = "text_size";

    private final int DEFAULT_BG_RING_COLOR = Color.parseColor("#647d7d7d");
    private final float DEFAULT_BG_RING_WIDTH;
    private final float DEFAULT_MAX = 10f;
    private final int DEFAULT_STROKE_COLOR = Color.parseColor("#FBC02D");
    private final float DEFAULT_STROKE_WIDTH;
    private final int DEFAULT_TEXT_COLOR = Color.parseColor("#FBC02D");
    private final float DEFAULT_TEXT_SIZE;
    private final int MIN_SIZE;
    private int mBgRingColor;
    private Paint mBgRingPaint;
    private float mBgRingWidth;
    private Paint mInnerCirclePaint;
    private float mMark = 0;
    private float mMax = DEFAULT_MAX;
    private RectF mOuterRect = new RectF();
    private Paint mPaint;
    private int mStartPoint;
    private int[] mStrokeColors;
    private float mStrokeWidth;
    private int mTextColor;
    private Paint mTextPaint;
    private float mTextSize;

// --------------------------- CONSTRUCTORS ---------------------------

    public MarkView(Context context) {
        this(context, null);
    }

    public MarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DEFAULT_TEXT_SIZE = spToPx(getResources(), 16);
        MIN_SIZE = (int) dpToPx(getResources(), 48);
        DEFAULT_STROKE_WIDTH = dpToPx(getResources(), 2);
        DEFAULT_BG_RING_WIDTH = dpToPx(getResources(), 2);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MarkView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Returns the current mark
     */
    public float getMark() {
        return mMark;
    }

    /**
     * Returns the highest mark this MarkView accepts
     */
    public float getMax() {
        return mMax;
    }

    /**
     * Returns the color of the background ring
     */
    public int getRingColor() {
        return mBgRingColor;
    }

    /**
     * Returns the width of the background ring
     */
    public float getRingWidth() {
        return mBgRingWidth;
    }

    /**
     * Returns the stroke color depending on the current mark.
     * <p/>
     * May return:
     * - DEFAULT_STROKE_COLOR if the mark is invalid or there are no colors in mStrokeColors.
     * - The color at the position of the mark in mStrokeColors if it contains the mark.
     * - The last color in mStrokeColors if it doesn't contain the mark.
     */
    public int getStrokeColor() {
        if (mStrokeColors == null || mStrokeColors.length == 0 || !isMarkValid(mMark)) {
            return DEFAULT_STROKE_COLOR;
        }
        if (mStrokeColors.length < mMark) {
            return mStrokeColors[mStrokeColors.length - 1];
        }
        return mStrokeColors[(int) mMark - 1];
    }

    /**
     * Returns the current stroke colors array
     */
    public int[] getStrokeColors() {
        return mStrokeColors;
    }

    /**
     * Returns stroke width
     */
    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * Returns the current text color
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Returns the current text size
     */
    public float getTextSize() {
        return mTextSize;
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    /**
     * Checks whether a given mark is valid according to the current configuration
     */
    public boolean isMarkValid(double mark) {
        return mark > 0 && mark <= mMax;
    }

    /**
     * Sets the mark
     */
    public void setMark(float mark) {
        mMark = mark;
        invalidate();
    }

    /**
     * Sets the highest mark this MarkView accepts
     */
    public void setMax(float max) {
        if (max > 0) {
            mMax = max;
            invalidate();
        }
    }

    /**
     * Sets the color of the background ring
     */
    public void setRingColor(int color) {
        mBgRingColor = color;
    }

    /**
     * Sets the width of the background ring
     */
    public void setRingWidth(float width) {
        mBgRingWidth = width;
    }

    /**
     * Sets the stroke colors.
     * <p/>
     * See {@link #getStrokeColor()} to see how stroke colors are handled.
     */
    public void setStrokeColors(int... strokeColors) {
        mStrokeColors = strokeColors;
        invalidate();
    }

    /**
     * Sets stroke width
     */
    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }

    /**
     * Sets the text size to a given color
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    /**
     * Sets the text size
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float delta = mStrokeWidth;
        mOuterRect.set(delta,
                delta,
                getWidth() - delta,
                getHeight() - delta);

        float innerCircleRadius = (getWidth() - mStrokeWidth) / 2f;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, innerCircleRadius, mInnerCirclePaint);
        canvas.drawArc(mOuterRect, 0, 360f, false, mBgRingPaint);
        canvas.drawArc(mOuterRect, mStartPoint, getMarkAngle(), false, mPaint);

        String text = isMarkValid(mMark) ? String.valueOf(mMark) : "?";
        if (!TextUtils.isEmpty(text)) {
            float textHeight = mTextPaint.descent() + mTextPaint.ascent();
            canvas.drawText(text, (getWidth() - mTextPaint.measureText(text)) / 2.0f, (getWidth() - textHeight) / 2.0f, mTextPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mTextColor = bundle.getInt(STATE_TEXT_COLOR);
            mTextSize = bundle.getFloat(STATE_TEXT_SIZE);
            mStrokeColors = bundle.getIntArray(STATE_STROKE_COLORS);
            mStrokeWidth = bundle.getFloat(STATE_STROKE_WIDTH);
            initPainters();
            setMax(bundle.getFloat(STATE_MAX));
            setMark(bundle.getFloat(STATE_MARK));
            super.onRestoreInstanceState(bundle.getParcelable(STATE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_STATE, super.onSaveInstanceState());
        bundle.putInt(STATE_TEXT_COLOR, getTextColor());
        bundle.putFloat(STATE_TEXT_SIZE, getTextSize());
        bundle.putIntArray(STATE_STROKE_COLORS, getStrokeColors());
        bundle.putFloat(STATE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(STATE_MAX, getMax());
        bundle.putFloat(STATE_MARK, getMark());
        return bundle;
    }

    private float dpToPx(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float getMarkAngle() {
        if (!isMarkValid(mMark)) return 360f;
        return getMark() / mMax * 360f;
    }

    private void initByAttributes(TypedArray attributes) {
        mStrokeWidth = attributes.getDimension(R.styleable.MarkView_mv_strokeWidth, DEFAULT_STROKE_WIDTH);
        if (!isInEditMode()) {
            int id = attributes.getResourceId(R.styleable.MarkView_mv_strokeColors, R.array.mv_mark_colors);
            String[] colors = getResources().getStringArray(id);
            mStrokeColors = new int[colors.length];
            for (int i = 0; i < colors.length; i++) {
                mStrokeColors[i] = Color.parseColor(colors[i]);
            }
        }

        mBgRingWidth = attributes.getDimension(R.styleable.MarkView_mv_ringWidth,
                DEFAULT_BG_RING_WIDTH);
        mBgRingColor = attributes.getColor(R.styleable.MarkView_mv_ringColor,
                DEFAULT_BG_RING_COLOR);

        mTextColor = attributes.getColor(R.styleable.MarkView_mv_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = attributes.getDimension(R.styleable.MarkView_mv_textSize, DEFAULT_TEXT_SIZE);
        mStartPoint = attributes.getInt(R.styleable.MarkView_mv_startPoint, 0);


        setMax(attributes.getFloat(R.styleable.MarkView_mv_max, DEFAULT_MAX));
        setMark(attributes.getInt(R.styleable.MarkView_mv_mark, 0));
    }

    private void initPainters() {
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        mPaint = new Paint();
        mPaint.setColor(getStrokeColor());
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth);

        mBgRingPaint = new Paint();
        mBgRingPaint.setColor(mBgRingColor);
        mBgRingPaint.setStyle(Paint.Style.STROKE);
        mBgRingPaint.setAntiAlias(true);
        mBgRingPaint.setStrokeWidth(mBgRingWidth);

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(Color.TRANSPARENT);
        mInnerCirclePaint.setAntiAlias(true);
    }

    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = MIN_SIZE;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private float spToPx(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}