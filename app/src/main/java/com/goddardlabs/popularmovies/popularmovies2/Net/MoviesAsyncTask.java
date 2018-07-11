package com.goddardlabs.popularmovies.popularmovies2.Net;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.goddardlabs.popularmovies.popularmovies2.BuildConfig;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Review;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Video;
import com.goddardlabs.popularmovies.popularmovies2.R;

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

public class MoviesAsyncTask extends AsyncTask<String, Void, TransferContainer> {
    private final String LOG = MoviesAsyncTask.class.getSimpleName();
    private final OnTaskCompleted listener;

    public MoviesAsyncTask(OnTaskCompleted listener) {
        super();

        this.listener = listener;
    }

    @Override
    protected TransferContainer doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = null;
            if(params[0] == "movies") {
                url = getApiMovieUrl(params);
                Log.i("Movies API URL", url.toString());
            } else {
                url = getApiMovieDetailUrl(params);
                Log.i("Movie API URL", url.toString());
            }

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
                if(params[0].equals("movies")) {
                    Log.i("movies json call", "returning movies");
                    TransferContainer transferContainer = new TransferContainer();
                    transferContainer.setMovies(getMoviesJson(builder.toString()));
                    transferContainer.setRequest_type(0);
                    return transferContainer;
                } else {
                    Log.i("movie json call", "returning a movie");
                    TransferContainer transferContainer = new TransferContainer();
                    transferContainer.setMovies(getMovieJson(builder.toString()));
                    transferContainer.setRequest_type(1);
                    return transferContainer;
                }
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

    private ArrayList<Movie> getMoviesJson(String moviesJsonStr) throws JSONException {
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray("results");

        ArrayList<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            movies.add(new Movie());
            movies.get(i).setId(movieInfo.getInt("id"));
            movies.get(i).setTitle(movieInfo.getString("title"));
            movies.get(i).setPosterPath(movieInfo.getString("poster_path"));
            movies.get(i).setOverview(movieInfo.getString("overview"));
            movies.get(i).setVoteAverage(movieInfo.getString("vote_average"));
            movies.get(i).setReleaseDate(movieInfo.getString("release_date"));
            movies.get(i).setBack_drop_path(movieInfo.getString("backdrop_path"));
        }

        return movies;
    }

    private ArrayList<Movie> getMovieJson(String moviesJsonStr) throws JSONException {
        JSONObject movieInfo = new JSONObject(moviesJsonStr);
        Log.i("Movie Detail Info", movieInfo.toString());

        ArrayList<Movie> movies = new ArrayList<Movie>();

        movies.add(new Movie());
        movies.get(0).setId(movieInfo.getInt("id"));
        movies.get(0).setTitle(movieInfo.getString("title"));
        movies.get(0).setPosterPath(movieInfo.getString("poster_path"));
        movies.get(0).setOverview(movieInfo.getString("overview"));
        movies.get(0).setVoteAverage(movieInfo.getString("vote_average"));
        movies.get(0).setReleaseDate(movieInfo.getString("release_date"));
        movies.get(0).setBack_drop_path(movieInfo.getString("backdrop_path"));

        JSONArray videosArray = movieInfo.getJSONObject("videos").getJSONArray("results");
        ArrayList<Video> videos = new ArrayList<Video>();
        for (int i = 0; i < videosArray.length(); i++) {
            JSONObject videoJson = videosArray.getJSONObject(i);
            Log.i("Vidceo Json ", videoJson.toString());

            videos.add(new Video());
            videos.get(i).set_id(videoJson.getString("id"));
            videos.get(i).setIso_639_1(videoJson.getString("iso_639_1"));
            videos.get(i).setIso_3166_1(videoJson.getString("iso_3166_1"));
            videos.get(i).setVideo_site_key(videoJson.getString("key"));
            videos.get(i).setVideo_name(videoJson.getString("name"));
            videos.get(i).setSite(videoJson.getString("site"));
            videos.get(i).set_size(videoJson.getString("size"));
            videos.get(i).set_type(videoJson.getString("type"));
        }
        movies.get(0).setVideos(videos);

        JSONArray reviewArray = movieInfo.getJSONObject("reviews").getJSONArray("results");
        ArrayList<Review> reviews = new ArrayList<Review>();
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject reviewJson = reviewArray.getJSONObject(i);

            reviews.add(new Review());
            reviews.get(i).set_id(reviewJson.getString("id"));
            reviews.get(i).setAuthor(reviewJson.getString("author"));
            reviews.get(i).setContent(reviewJson.getString("content"));
            reviews.get(i).setUrl(reviewJson.getString("url"));
//            reviews.get(i).setTotal_pages(reviewJson.getInt("total_pages"));
//            reviews.get(i).setTotal_results(reviewJson.getInt("total_results"));
        }
        movies.get(0).setReviews(reviews);

        return movies;
    }

    private URL getApiMovieUrl(String[] parameters) throws MalformedURLException {
        Uri base = Uri.parse("https://api.themoviedb.org/3/discover/movie?");
        Log.i("param", parameters[1]);
        if(parameters[1].equals(R.string.sort_popular_key)) {
            base = Uri.parse("https://api.themoviedb.org/3/movie/popular?");
        } else if(parameters[1].equals("vote_average.desc")) {
            Log.i("test", parameters[1]);
            base = Uri.parse("https://api.themoviedb.org/3/movie/top_rated?");
            parameters[1] = "popularity.desc";
        }
        Uri.Builder buildUpon = base.buildUpon();
        buildUpon.appendQueryParameter("api_key", BuildConfig.API_KEY);
        buildUpon.appendQueryParameter("sort_by", parameters[1]);
        buildUpon.appendQueryParameter("page", parameters[2]);
        Uri returnUri = buildUpon.build();

        return new URL(returnUri.toString());
    }

    private URL getApiMovieDetailUrl(String[] parameters) throws MalformedURLException {
        Uri base = Uri.parse("https://api.themoviedb.org/3/movie/");
        Uri.Builder buildUpon = base.buildUpon();
//        550?append_to_response=videos,reviews&&api_key=
        //movie id
        buildUpon.appendPath(parameters[1]);
        buildUpon.appendQueryParameter("append_to_response", "videos,reviews");
        buildUpon.appendQueryParameter("api_key", BuildConfig.API_KEY);
        Uri returnUri = buildUpon.build();

        return new URL(returnUri.toString());
    }

    @Override
    protected void onPostExecute(TransferContainer transferContainer) {
        super.onPostExecute(transferContainer);

        this.listener.onTaskCompleted(transferContainer);
    }
}
