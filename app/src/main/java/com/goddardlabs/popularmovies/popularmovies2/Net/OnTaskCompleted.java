package com.goddardlabs.popularmovies.popularmovies2.Net;

import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;
import java.util.ArrayList;

public interface OnTaskCompleted {
    void onTaskCompleted(ArrayList<Movie> movies);
}
