package com.vaporwarecorp.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Video implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String id;
    public String key;
    public String name;

// --------------------------- CONSTRUCTORS ---------------------------

    public Video() {
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Parcelable ---------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
    }
}
