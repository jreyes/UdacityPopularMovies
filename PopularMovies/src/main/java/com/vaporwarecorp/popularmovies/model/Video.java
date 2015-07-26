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
    public static final Video EMPTY = new Video(null, null, null, null);

    public String id;
    public String key;
    public String name;
    public String site;

// --------------------------- CONSTRUCTORS ---------------------------

    protected Video(Parcel in) {
        this(in.readString(), in.readString(), in.readString(), in.readString());
    }

    public Video(String id, String key, String name, String site) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                '}';
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
        dest.writeString(this.site);
    }
}
