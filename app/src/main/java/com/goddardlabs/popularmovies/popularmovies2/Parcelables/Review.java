package com.goddardlabs.popularmovies.popularmovies2.Parcelables;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String video_site_key;
    private String video_name;
    private String site;
    private String size;
    private String type;

    public Review() {

    }

    private Review(Parcel parcel) {
        this.id = parcel.readString();
        this.iso_639_1 = parcel.readString();
        this.iso_3166_1 = parcel.readString();
        this.video_site_key = parcel.readString();
        this.video_name = parcel.readString();
        this.site = parcel.readString();
        this.size = parcel.readString();
        this.type = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.iso_639_1);
        parcel.writeString(this.iso_3166_1);
        parcel.writeString(this.video_site_key);
        parcel.writeString(this.video_name);
        parcel.writeString(this.site);
        parcel.writeString(this.size);
        parcel.writeString(this.type);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
