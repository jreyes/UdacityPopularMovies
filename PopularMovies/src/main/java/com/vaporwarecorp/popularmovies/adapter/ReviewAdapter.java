package com.vaporwarecorp.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
// ------------------------------ FIELDS ------------------------------

    private List<Review> mReviews;

// -------------------------- INNER CLASSES --------------------------

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;

        public ReviewViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.author_text);
            content = (TextView) view.findViewById(R.id.review_text);
        }
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public ReviewAdapter() {
        this.mReviews = new ArrayList<>();
    }

// -------------------------- OTHER METHODS --------------------------

    public void addReviews(List<Review> reviews) {
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int i) {
        Review review = mReviews.get(i);
        reviewViewHolder.author.setText(review.author);
        reviewViewHolder.content.setText(review.content);
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewViewHolder(view);
    }
}
