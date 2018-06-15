package com.goddardlabs.popularmovies.popularmovies2.Net;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.goddardlabs.popularmovies.popularmovies2.BuildConfig;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MoviesAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String LOG = MoviesAsyncTask.class.getSimpleName();
    private final OnTaskCompleted listener;
    //might not need
    private String page = "1";

    public MoviesAsyncTask(OnTaskCompleted listener) {
        super();

        this.listener = listener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = getApiMovieUrl(params);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            if (builder.length() == 0) {
                return null;
            }

            try {
                return getMoviesJson(builder.toString());
            }

            catch (JSONException e) {
                Log.e(LOG, e.getMessage(), e);
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.e(LOG, "Error ", e);
            return null;
        }

        return null;
    }

//    private Movie[] getMoreMoviesJson(String moviesJsonStr) {
//
//    }

    private ArrayList<Movie> getMoviesJson(String moviesJsonStr) throws JSONException {
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray("results");

//        Movie[] movies = new Movie[resultsArray.length()];
        ArrayList<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            movies.add(new Movie());
            movies.get(i).setTitle(movieInfo.getString("title"));
            movies.get(i).setPosterPath(movieInfo.getString("poster_path"));
            movies.get(i).setOverview(movieInfo.getString("overview"));
            movies.get(i).setVoteAverage(movieInfo.getString("vote_average"));
            movies.get(i).setReleaseDate(movieInfo.getString("release_date"));
            movies.get(i).setBack_drop_path(movieInfo.getString("backdrop_path"));
        }

        return movies;
    }

    private URL getApiMovieUrl(String[] parameters) throws MalformedURLException {
        Uri base = Uri.parse("https://api.themoviedb.org/3/discover/movie?");
        Uri.Builder buildUpon = base.buildUpon();
        buildUpon.appendQueryParameter("api_key", BuildConfig.API_KEY);
        buildUpon.appendQueryParameter("sort_by", parameters[0]);
        buildUpon.appendQueryParameter("page", parameters[1]);
        Uri returnUri = buildUpon.build();

        return new URL(returnUri.toString());
    }

    private URL getApiMovieDetailUrl(String[] parameters) throws MalformedURLException {
        Uri base = Uri.parse("https://api.themoviedb.org/3/movie/");
        Uri.Builder buildUpon = base.buildUpon();
//        550?append_to_response=videos,reviews&&api_key=d01c7731919bd4efcaf19322b51aa3ee
        //movie id
        buildUpon.appendPath(parameters[0]);
        buildUpon.appendQueryParameter("append_to_response", "videos,reviews");
        buildUpon.appendQueryParameter("api_key", BuildConfig.API_KEY);
        Uri returnUri = buildUpon.build();

        return new URL(returnUri.toString());
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        this.listener.onTaskCompleted(movies);
    }
}
