package com.goddardlabs.popularmovies.popularmovies2.Net;

import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;

public interface OnTaskCompleted {
    void onTaskCompleted(Movie[] movies);
}
