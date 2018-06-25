package com.goddardlabs.popularmovies.popularmovies2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goddardlabs.popularmovies.popularmovies2.MovieDetailActivity;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Review;
import com.goddardlabs.popularmovies.popularmovies2.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviews;
    private MovieDetailActivity MovieDetailActivity;

    public ReviewAdapter(MovieDetailActivity MovieDetailActivity, ArrayList<Review> reviews) {
        this.MovieDetailActivity = MovieDetailActivity;
        this.reviews = reviews;

        Log.i("Review Adapter", Integer.toString(reviews.size()));
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_item, viewGroup, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.mTvReviewAuthor.setText(review.getAuthor());
        holder.mTvReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvReviewAuthor;
        public TextView mTvReviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            mTvReviewAuthor = itemView.findViewById(R.id.tvReviewAuthor);
            mTvReviewContent = itemView.findViewById(R.id.tvReviewContent);
        }
    }
}
