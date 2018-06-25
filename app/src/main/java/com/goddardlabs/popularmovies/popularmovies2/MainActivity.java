package com.goddardlabs.popularmovies.popularmovies2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.goddardlabs.popularmovies.popularmovies2.Adapters.MoviesAdapter;
import com.goddardlabs.popularmovies.popularmovies2.Data.MoviesContract;
import com.goddardlabs.popularmovies.popularmovies2.Data.MoviesDBHelper;
import com.goddardlabs.popularmovies.popularmovies2.Data.MoviesProvider;
import com.goddardlabs.popularmovies.popularmovies2.Data.Preferences;
import com.goddardlabs.popularmovies.popularmovies2.Net.MoviesAsyncTask;
import com.goddardlabs.popularmovies.popularmovies2.Net.OnTaskCompleted;
import com.goddardlabs.popularmovies.popularmovies2.Net.TransferContainer;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;

import java.util.ArrayList;

import static com.goddardlabs.popularmovies.popularmovies2.Net.Network.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {
    private final String favorites = "favorite.desc";
    public MoviesAdapter movie_adapter;
    private RecyclerView recycler_view;
    private Parcelable list_state;

    private MoviesProvider movie_provider;
    private MoviesDBHelper db_helper;
    private ArrayList<Movie> favorite_movies = new ArrayList<>();

    private int fav_counter = 0;

    int pageNumber = 1;
    private Preferences preferences = new Preferences();

    public String changed;
    public boolean force_restart = false;

    @Override
    public void onResume() {
        super.onResume();

        if((changed != preferences.getSortType(this)) || force_restart) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            this.changed = preferences.getSortType(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int insert_num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.movie_provider = new MoviesProvider();
        this.db_helper = new MoviesDBHelper(this);

        this.changed = preferences.getSortType(this);

        Log.i("Changed Sort Type", this.changed);
        SQLiteDatabase db;

//        if(insert_num == 0) {
//            insert_num++;
//
//            db = db_helper.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(MoviesContract.MoviesEntry.COLUMN_ID, "550");
//            values.put(MoviesContract.MoviesEntry.COLUMN_TITLE, "Fight Club");
//            values.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.");
//            values.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, "/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg");
//            values.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, "/87hTDiay2N2qWyX4Ds7ybXi9h8I.jpg");
//            values.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, "1999-10-15");
//            values.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, "8.4");
//            db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
//        }

        recycler_view = findViewById(R.id.card_recycler_view);
        recycler_view.setHasFixedSize(true);
        GridLayoutManager grid = new GridLayoutManager(this,2);
        recycler_view.setLayoutManager(grid);

        if (savedInstanceState == null) {

            if(preferences.getSortType(this).equals(this.favorites)) {
                Log.i("MainAcitivty", "Favorite recognized in onCreate");
                getMovies();
                movie_adapter = new MoviesAdapter(this, favorite_movies);
                recycler_view.setAdapter(movie_adapter);
                this.favorite_movies = new ArrayList<>();
            } else {
                getMovies();
            }
        } else {
            ArrayList<Parcelable> parcelable = savedInstanceState.getParcelableArrayList(getString(R.string.movie_type_key));

            if (parcelable != null) {
                int numMovieObjects = parcelable.size();
                ArrayList<Movie> movies = new ArrayList<Movie>();
                for (int i = 0; i < numMovieObjects; i++) {
                    movies.add((Movie) parcelable.get(i));
                }

                movie_adapter = new MoviesAdapter(this, movies);
                recycler_view.setAdapter(movie_adapter);
                getMovies();

                list_state = savedInstanceState.getParcelable("ListState");
            }
        }
    }

    @Override
    public void onTaskCompleted(TransferContainer transferContainer) {
        if(movie_adapter == null) {
            movie_adapter = new MoviesAdapter(this, transferContainer.getMovies());
            recycler_view.setAdapter(movie_adapter);
            this.pageNumber++;
        } else {
            if(transferContainer != null && transferContainer.getRequest_type() == 0) {
                movie_adapter.addAll(transferContainer.getMovies());
                this.pageNumber++;
            } else if(transferContainer != null && transferContainer.getRequest_type() == 1) {
                Log.i("Movie Details", "movie details called");
//                movie_adapter.addAll(transferContainer.getMovies());

                Intent intent = new Intent(this, MovieDetailActivity.class);
                Log.i("Movie Details", Integer.toString(transferContainer.getMovies().size()));
                Log.i("Movie Details", transferContainer.getMovies().get(0).getOverview());
                if(transferContainer.getMovies().size() > 0) {
                    intent.putExtra(getString(R.string.movie_type_key), transferContainer.getMovies().get(0));
                }

                startActivity(intent);
            }
        }
    }

    public void startDetailsAcitivty(Movie movie) {
        getMovie(movie.getId());
    }

    public void getMovies() {
        if(preferences.getSortType(this).equals(this.favorites)) {
            SQLiteDatabase db = db_helper.getReadableDatabase();

            String[] projection = {
                    MoviesContract.MoviesEntry.COLUMN_ID,
                    MoviesContract.MoviesEntry.COLUMN_TITLE,
                    MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
                    MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
                    MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
                    MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
                    MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE
            };

            Cursor cursor = db.query(
                    MoviesContract.MoviesEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            if(fav_counter < cursor.getCount()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    Log.i("fav count", Integer.toString(cursor.getCount()));
                    int movieIdIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ID);
                    int titleIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
                    int overviewIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW);
                    int posterPathIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
                    int backdropPathIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH);
                    int releaseDateIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE);
                    int voteAverageIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE);

                    cursor.moveToPosition(i);

                    Movie movie = new Movie();
                    movie.setId(cursor.getInt(movieIdIndex));
                    movie.setTitle(cursor.getString(titleIndex));
                    movie.setOverview(cursor.getString(overviewIndex));
                    movie.setPosterPath(cursor.getString(posterPathIndex));
                    movie.setBack_drop_path(cursor.getString(backdropPathIndex));
                    movie.setReleaseDate(cursor.getString(releaseDateIndex));
                    movie.setVoteAverage(cursor.getString(voteAverageIndex));

                    favorite_movies.add(movie);
                }

                fav_counter = cursor.getCount();
            }
            db.close();

        } else {
            if (isNetworkAvailable(this)) {
                MoviesAsyncTask movieTask = new MoviesAsyncTask(this);
                movieTask.execute("movies", preferences.getSortType(this), Integer.toString(this.pageNumber));
            } else {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getMovie(int movie_id) {
        if (isNetworkAvailable(this)) {
            MoviesAsyncTask movieTask = new MoviesAsyncTask(this);
            movieTask.execute("movie", Integer.toString(movie_id));
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.movie_type_key), movie_adapter.getMovies());
        outState.putParcelable("ListState", recycler_view.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
