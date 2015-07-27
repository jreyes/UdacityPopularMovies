package com.vaporwarecorp.popularmovies.adapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
// ------------------------------ FIELDS ------------------------------

    private List<T> mItems;

// --------------------------- CONSTRUCTORS ---------------------------

    public BaseAdapter(List<T> items) {
        mItems = items;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Adapter ---------------------

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addItems(List<T> movies) {
        mItems.addAll(movies);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public ArrayList<T> getItems() {
        return new ArrayList<>(mItems);
    }
}
