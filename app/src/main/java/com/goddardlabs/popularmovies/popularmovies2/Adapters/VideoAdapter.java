package com.goddardlabs.popularmovies.popularmovies2.Adapters;

import android.util.Log;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goddardlabs.popularmovies.popularmovies2.MovieDetailActivity;
import com.goddardlabs.popularmovies.popularmovies2.Parcelables.Video;
import com.goddardlabs.popularmovies.popularmovies2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private ArrayList<Video> videos;
    private MovieDetailActivity MovieDetailActivity;

    public VideoAdapter(MovieDetailActivity MovieDetailActivity, ArrayList<Video> videos) {
        this.MovieDetailActivity = MovieDetailActivity;
        this.videos = videos;

        Log.i("Video Adapter", Integer.toString(videos.size()));
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.video_item, viewGroup, false);
        return new VideoAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video video = videos.get(position);
        Log.i("On Bind View Holder", videoImageUrl(video.getVideo_site_key()));

        Picasso.with(MovieDetailActivity)
                .load(videoImageUrl(video.getVideo_site_key()))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mIvVideoThumb);

        holder.mTvVideoTitle.setText(videos.get(position).getVideo_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=".concat(video.getVideo_site_key())));
                MovieDetailActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return this.videos.size(); }

    private String videoImageUrl(String video_id) {
        return "https://img.youtube.com/vi/" + video_id + "/hqdefault.jpg";
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvVideoThumb;
        public TextView mTvVideoTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);

            mIvVideoThumb = itemView.findViewById(R.id.cvVideo);
            mTvVideoTitle = itemView.findViewById(R.id.tvVideoTitle);
        }
    }
}
