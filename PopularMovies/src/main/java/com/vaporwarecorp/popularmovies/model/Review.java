package com.vaporwarecorp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String author;
    public String content;
    public String id;

// --------------------------- CONSTRUCTORS ---------------------------

    protected Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.id = in.readString();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Parcelable ---------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.id);
    }
}
