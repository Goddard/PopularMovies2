package com.goddardlabs.popularmovies.popularmovies2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.goddardlabs.popularmovies.popularmovies2.Adapters.ReviewAdapter;
import com.goddardlabs.popularmovies.popularmovies2.Adapters.VideoAdapter;
import com.goddardlabs.popularmovies.popularmovies2.Data.MoviesContract;
import com.goddardlabs.popularmovies.popularmovies2.Data.MoviesDBHelper;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private MoviesDBHelper db_helper;
    private Movie movie;
    private Button fav_button;
    private MainActivity mainActivity;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.intent = new Intent();
        setResult(RESULT_CANCELED, intent);

        setContentView(R.layout.activity_movie_detail);

        final ConstraintLayout constraint_layout_main_activity = findViewById(R.id.activity_background);
        ImageView image_view_poster = findViewById(R.id.imageview_poster);
        TextView text_view_overview = findViewById(R.id.textview_overview);
        text_view_overview.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        TextView text_view_rating = findViewById(R.id.textview_rating);
        text_view_rating.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        TextView tv_rating_title = findViewById(R.id.textview_rating_title);
        tv_rating_title.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        TextView text_view_release_date = findViewById(R.id.textview_release_date);
        text_view_release_date.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        TextView text_view_release_date_title = findViewById(R.id.textview_release_date_title);
        text_view_release_date_title.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        TextView text_view_videos_title = findViewById(R.id.tvVideosTitle);
        text_view_videos_title.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        VideoAdapter video_adapter;

        TextView text_view_reviews_title = findViewById(R.id.tvReviewsTitle);
        text_view_reviews_title.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        ReviewAdapter review_adapter;


        Intent intent = getIntent();
        movie = intent.getParcelableExtra(getString(R.string.movie_type_key));
        Log.i("Movie Passed Video Size ", Integer.toString(movie.getVideos().size()));

        final ImageView img = new ImageView(this);
        Picasso.with(this)
                .load(movie.getBack_drop_path())
                .error(R.drawable.awesome)
                .placeholder(R.drawable.movieticket)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        constraint_layout_main_activity.setBackground(img.getDrawable());
                    }

                    @Override
                    public void onError() {

                    }});

        this.setTitle(movie.getTitle());

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(250,
                        325)
                .error(R.drawable.awesome)
                .placeholder(R.drawable.movieticket)
                .into(image_view_poster);

        String overView = movie.getOverview();
        if (overView == null) {
            text_view_overview.setTypeface(null, Typeface.ITALIC);
            overView = getString(R.string.no_summary);
        }

        text_view_overview.setText(overView);
        text_view_rating.setText(movie.getVoteAverage().toString());

        String release_date = movie.getReleaseDate();
        if(release_date == null) {
            text_view_release_date.setTypeface(null, Typeface.ITALIC);
            release_date = getString(R.string.no_release_date);
        }
        text_view_release_date.setText(release_date);

        ///////
        RecyclerView recycler_view_videos = findViewById(R.id.rvVideos);
        recycler_view_videos.setHasFixedSize(true);
        recycler_view_videos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        Log.i("Videos in Movie Object : ", Integer.toString(movie.getVideos().size()));
        video_adapter = new VideoAdapter(this, movie.getVideos());
        recycler_view_videos.setAdapter(video_adapter);
        video_adapter.notifyDataSetChanged();

        RecyclerView recycler_view_reviews = findViewById(R.id.rvReviews);
        recycler_view_reviews.setHasFixedSize(true);
        recycler_view_reviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Log.i("Reviews in Movie Object : ", Integer.toString(movie.getReviews().size()));
        review_adapter = new ReviewAdapter(this, movie.getReviews());
        recycler_view_reviews.setAdapter(review_adapter);
        review_adapter.notifyDataSetChanged();

        fav_button = findViewById(R.id.fav_button);
        fav_button.setTag(movie.getId());
        fav_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.i("On Button Click", v.getTag().toString());
                saveAndUnsave();
                checkSaved(movie.getId());
            }
        });

        checkSaved(movie.getId());
    }

    private void checkSaved(int movie_id) {
        this.db_helper = new MoviesDBHelper(this);

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

        Log.i("Before Cursor Movie Id", Integer.toString(movie.getId()));
        Cursor cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                MoviesContract.MoviesEntry.COLUMN_ID + " = ?",              // The columns for the WHERE clause
                new String[] { Integer.toString(movie.getId()) },          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (cursor.getCount() > 0) {
            this.fav_button.setText(R.string.saved);
            this.fav_button.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            this.fav_button.setText(R.string.save);
            this.fav_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        db.close();
    }

    private void saveAndUnsave() {
        this.db_helper = new MoviesDBHelper(this);

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

        Log.i("Before Cursor Movie Id", Integer.toString(movie.getId()));
        Cursor cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                MoviesContract.MoviesEntry.COLUMN_ID + " = ?",              // The columns for the WHERE clause
                new String[] { Integer.toString(movie.getId()) },          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        Log.i("Cursor", Integer.toString(cursor.getCount()));

        if (cursor.getCount() > 0) {
            db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_ID + "=" + movie.getId(), null);
        } else {
            db = db_helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_ID, movie.getId());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterJsonPath());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movie.getBack_drop_path());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, contentValues);
        }
        db.close();

        this.intent.putExtra("changed", "true");
        setResult(RESULT_OK, this.intent);
    }
}
