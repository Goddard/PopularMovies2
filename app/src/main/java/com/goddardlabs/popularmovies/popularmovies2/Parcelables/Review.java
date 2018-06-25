package com.goddardlabs.popularmovies.popularmovies2.Parcelables;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String author;
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    private String content;
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    private String id;
    public String get_id() { return id; }
    public void set_id(String id) { this.id = id; }

    private String url;
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    private int total_pages = 1;
    public int getTotal_pages() { return total_pages; }
    public void setTotal_pages(int total_pages) { this.total_pages = total_pages; }

    private int total_results = 1;
    public int getTotal_results() { return total_results; }
    public void setTotal_results(int total_results) { this.total_results = total_results; }

    public Review() {

    }

    private Review(Parcel parcel) {
        this.id = parcel.readString();
        this.author = parcel.readString();
        this.content = parcel.readString();
        this.url = parcel.readString();
        this.total_pages = parcel.readInt();
        this.total_results = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.author);
        parcel.writeString(this.content);
        parcel.writeString(this.url);
        parcel.writeInt(this.total_pages);
        parcel.writeInt(this.total_results);
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
