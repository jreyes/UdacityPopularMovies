package com.vaporwarecorp.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.vaporwarecorp.popularmovies.R;
import com.vaporwarecorp.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {
// -------------------------- INNER CLASSES --------------------------

    static class ViewHolder {
        TextView author;
        TextView content;

        public ViewHolder(View view) {
            author = (TextView) view.findViewById(R.id.author_text);
            content = (TextView) view.findViewById(R.id.review_text);
        }
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, R.layout.review_list_item, reviews);
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Adapter ---------------------

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Review review = getItem(position);
        viewHolder.author.setText(review.author);
        viewHolder.content.setText(review.content);
        return convertView;
    }
}
