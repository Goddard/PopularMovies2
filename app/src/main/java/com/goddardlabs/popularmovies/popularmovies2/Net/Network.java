package com.goddardlabs.popularmovies.popularmovies2.Net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.goddardlabs.popularmovies.popularmovies2.BuildConfig;

public class Network {
    public static final String API_URL = "http://api.themoviedb.org/3/";
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";

    //this is the base of the url
    public static final String IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w1280/"; //w1280 //w185 //w1000_and_h563_face
    public static final String IMAGE_URL_BACKDROP_BASE = "https://image.tmdb.org/t/p/w1000_and_h563_face";
    //https://www.youtube.com/watch?v= //BdJKm16Co6M
    public static final String YOU_TUBE_BASE = "https://www.youtube.com/watch";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
