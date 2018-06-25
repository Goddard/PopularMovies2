package com.goddardlabs.popularmovies.popularmovies2.Parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import static com.goddardlabs.popularmovies.popularmovies2.Net.Network.IMAGE_URL_BACKDROP_BASE;
import static com.goddardlabs.popularmovies.popularmovies2.Net.Network.IMAGE_URL_BASE;

public class Movie implements Parcelable {
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    private String back_drop_path;
    public String getBack_drop_path() {
        return IMAGE_URL_BACKDROP_BASE + back_drop_path;
    }
    public void setBack_drop_path(String back_drop_path) {
        this.back_drop_path = back_drop_path;
    }

    ////////////////////////////////////////
    //required for stage 1
    ///////////////////////////////////////
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private String poster_path;
    public String getPosterPath() {
        return IMAGE_URL_BASE + poster_path;
    }
    public String getPosterJsonPath() { return poster_path; };
    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    private String overview;
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    private String vote_average;
    public String getVoteAverage() {
        return vote_average;
    }
    public void setVoteAverage(String vote_average) {
        this.vote_average = vote_average;
    }

    private String release_date;
    public String getReleaseDate() {
        return release_date;
    }
    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    ////////////////////////////////////////
    //required for stage 2
    ///////////////////////////////////////
    private String video_id;
    public String getVideo_id() { return video_id; }
    public void setVideo_id(String video_id) { this.video_id = video_id; }

    private ArrayList<Video> videos;
    public ArrayList<Video> getVideos() { return videos; }
    public void setVideos(ArrayList<Video> videos) { this.videos = videos; }

    private ArrayList<Review> reviews;
    public ArrayList<Review> getReviews() { return reviews; }
    public void setReviews(ArrayList<Review> reviews) { this.reviews = reviews; }

    public Movie() {
        this.id = 0;
        this.title = "";
        this.overview = "";
        this.poster_path = "";
        this.back_drop_path = "";
        this.release_date = "";
        this.vote_average = "0.0";
        this.videos = new ArrayList<Video>();
        this.reviews = new ArrayList<Review>();
    }

    private Movie(Parcel parcel) {
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.poster_path = parcel.readString();
        this.overview = parcel.readString();
        this.vote_average = parcel.readString();
        this.release_date = parcel.readString();
        this.back_drop_path = parcel.readString();

        this.videos = new ArrayList<Video>();
        this.reviews = new ArrayList<Review>();
        parcel.readTypedList(this.videos, Video.CREATOR);
        parcel.readTypedList(this.reviews, Review.CREATOR);

//        this.videos = parcel.readArrayList(this.videos, Video.class.getClassLoader());
//        this.reviews = parcel.readArrayList(Review.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.poster_path);
        parcel.writeString(this.overview);
        parcel.writeString(this.vote_average);
        parcel.writeString(this.release_date);
        parcel.writeString(this.back_drop_path);

        parcel.writeTypedList(this.videos);
        parcel.writeTypedList(this.reviews);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

