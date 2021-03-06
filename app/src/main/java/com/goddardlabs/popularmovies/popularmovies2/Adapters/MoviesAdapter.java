package com.goddardlabs.popularmovies.popularmovies2.Adapters;

import android.util.Log;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goddardlabs.popularmovies.popularmovies2.MainActivity;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Movie;
import com.goddardlabs.popularmovies.popularmovies2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private ArrayList<Movie> movies;
    private MainActivity mainActivity;

    public MoviesAdapter(MainActivity mainActivity, ArrayList<Movie> movies) {
        this.mainActivity = mainActivity;
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MovieViewHolder holder, int position) {
        final int pos = position;
        holder.mIvMovie.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Poster Path", movies.get(pos).getPosterPath());
                Picasso.with(holder.mIvMovie.getContext())
                        .load(movies.get(pos).getPosterPath())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.mIvMovie);
            }
        });

        holder.itemView.setTag(pos);

        if (position == movies.size() - 1) {
            this.mainActivity.getMovies();
        }
    }

    @SuppressWarnings("unused")
    public final void add(Movie movie) {
        this.movies.add(movie);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public final void addAll(ArrayList<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mIvMovie;
        private LinearLayout mProgressBarContainer;

        public MovieViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mProgressBarContainer = itemView.findViewById(R.id.progressBarContainer);
            mIvMovie = itemView.findViewById(R.id.cvVideo);
        }

        public void showProgress(Boolean show) {
            mProgressBarContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            String test = Integer.toString(movies.size());
            Log.i("OnClick", test);
            mainActivity.startDetailsAcitivty(movies.get(Integer.parseInt(v.getTag().toString())));
        }
    }
}
