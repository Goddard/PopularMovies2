package com.goddardlabs.popularmovies.popularmovies2.Parcelables;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    private String id;
    public String get_id() { return id; }
    public void set_id(String id) { this.id = id; }

    private String iso_639_1;
    public String getIso_639_1() { return iso_639_1; }
    public void setIso_639_1(String iso_639_1) { this.iso_639_1 = iso_639_1; }

    private String iso_3166_1;
    public String getIso_3166_1() { return iso_3166_1; }
    public void setIso_3166_1(String iso_3166_1) { this.iso_3166_1 = iso_3166_1; }

    private String video_site_key;
    public String getVideo_site_key() { return video_site_key; }
    public void setVideo_site_key(String video_site_key) { this.video_site_key = video_site_key; }

    private String video_name;
    public String getVideo_name() { return video_name; }
    public void setVideo_name(String video_name) { this.video_name = video_name; }

    private String site;
    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    private String size;
    public String get_size() { return size; }
    public void set_size(String size) { this.size = size; }

    private String type;
    public String get_type() { return type; }
    public void set_type(String type) { this.type = type; }

    public Video() {

    }

    private Video(Parcel parcel) {
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

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
