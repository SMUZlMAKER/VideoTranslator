package com.example.videotranslator.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotranslator.Items.ItemVideo;
import com.example.videotranslator.R;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    private  ArrayList<ItemVideo> videos;
    private final Context context;
    private final OnVideoClickListener onVideoClickListener;
    public VideosAdapter(Context context, ArrayList<ItemVideo> videos,OnVideoClickListener onVideoClickListener){
        this.context=context;
        this.videos=videos;
        this.onVideoClickListener=onVideoClickListener;
    }
    @NonNull
    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view,onVideoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ViewHolder holder, int position) {
        ItemVideo video = videos.get(position);
        holder.set(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void SetData(ArrayList<ItemVideo> videos){
        this.videos=videos;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView translationSeasonEpisodeQuality;
        public ViewHolder(View view,OnVideoClickListener onVideoClickListener){
            super(view);
            logo = view.findViewById(R.id.video_season_logo);
            translationSeasonEpisodeQuality = view.findViewById(R.id.translation_season_episode_quality);

            view.setOnClickListener(view1 -> {
                if(onVideoClickListener!=null){
                    int pos = getAbsoluteAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION){
                        onVideoClickListener.onVideoClick(pos);
                    }
                }
            });
        }
        void set(ItemVideo itemVideo){
            //this.logo.setText(itemFilm.getName());
            this.logo.setImageResource(itemVideo.getLogo());
            this.translationSeasonEpisodeQuality.setText(itemVideo.getTranslationSeasonEpisodeQuality());
        }
    }
}
