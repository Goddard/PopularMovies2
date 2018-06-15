package com.goddardlabs.popularmovies.popularmovies2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.goddardlabs.popularmovies.popularmovies2.Adapters.MoviesAdapter;
import com.goddardlabs.popularmovies.popularmovies2.Net.MoviesAsyncTask;
import com.goddardlabs.popularmovies.popularmovies2.Net.OnTaskCompleted;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;

import java.util.ArrayList;

import static com.goddardlabs.popularmovies.popularmovies2.Net.Network.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {
    private MoviesAdapter movie_adapter;
    private RecyclerView recycler_view;
    private Parcelable list_state;

    int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_view = findViewById(R.id.card_recycler_view);
        recycler_view.setHasFixedSize(true);
        GridLayoutManager grid = new GridLayoutManager(this,2);
        recycler_view.setLayoutManager(grid);

        if (savedInstanceState == null) {
            getMovies();
        } else {
            Parcelable[] parcelable = savedInstanceState.getParcelableArray(getString(R.string.movie_type_key));

            if (parcelable != null) {
                list_state = savedInstanceState.getParcelable("ListState");
//                int numMovieObjects = parcelable.length;
//                totalItems = numMovieObjects;
//                Movie[] movies = new Movie[numMovieObjects];
//                for (int i = 0; i < numMovieObjects; i++) {
//                    movies[i] = (Movie) parcelable[i];
//                }
//
//                movie_adapter = new MoviesAdapter(movies);
//                recycler_view.setAdapter(movie_adapter);
            }
        }
    }

    @Override
    public void onTaskCompleted(ArrayList<Movie> movies) {
        if(movie_adapter == null) {
            Log.i("Movie Adapater : ", "loaded");
            movie_adapter = new MoviesAdapter(this, movies);
            recycler_view.setAdapter(movie_adapter);
            this.pageNumber++;
        }

        else {
            movie_adapter.addAll(movies);
            movie_adapter.notifyDataSetChanged();
            this.pageNumber++;
        }
    }

    public void getMovies() {
        if (isNetworkAvailable(this)) {
            MoviesAsyncTask movieTask = new MoviesAsyncTask(this);
            movieTask.execute("popularity.desc", Integer.toString(this.pageNumber));
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        int numMovieObjects = recycler_view.getChildCount();
//        if (numMovieObjects > 0) {
//            Movie[] movies = new Movie[numMovieObjects];
//            for (int i = 0; i < numMovieObjects; i++) {
////                movies[i] = (Movie) recycler_view.getItemAtPosition(i);
//            }
//
//            outState.putParcelableArray(getString(R.string.movie_type_key), movies);
//        }
        super.onSaveInstanceState(outState);

        outState.putParcelable("ListState", recycler_view.getLayoutManager().onSaveInstanceState());
    }
}
