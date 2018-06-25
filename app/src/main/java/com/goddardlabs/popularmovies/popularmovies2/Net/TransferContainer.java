package com.goddardlabs.popularmovies.popularmovies2.Net;

import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;

import java.util.ArrayList;

public class TransferContainer {

    private ArrayList<Movie> movies;
    public ArrayList<Movie> getMovies() { return movies; }
    public void setMovies(ArrayList<Movie> movies) { this.movies = movies; }

    //0 is movies
    //1 is movie detail
    private int request_type = 0;
    public int getRequest_type() { return request_type; }
    public void setRequest_type(int request_type) { this.request_type = request_type; }
}
