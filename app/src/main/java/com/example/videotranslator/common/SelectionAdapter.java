package com.example.videotranslator.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotranslator.Items.ItemFilm;
import com.example.videotranslator.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.ViewHolder>{
    private final OnFilmClickListener onFilmClickListener;
    private ArrayList<ItemFilm> films;

    public SelectionAdapter( OnFilmClickListener onFilmClickListener) {
        this.onFilmClickListener=onFilmClickListener;
    }
    @NonNull
    @Override
    public SelectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_film, parent, false);
        return new ViewHolder(view, onFilmClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemFilm film = films.get(position);
        holder.set(film);
    }


    public void SetData(ArrayList<ItemFilm> films){
        this.films=films;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView name,shortDescription,lastEpisode;
        public ViewHolder(View view,OnFilmClickListener onFilmClickListener){
            super(view);
            poster = view.findViewById(R.id.poster);
            name = view.findViewById(R.id.film_name);
            shortDescription = view.findViewById(R.id.short_description);
            lastEpisode = view.findViewById(R.id.last_episode);

            view.setOnClickListener(view1 -> {
                if(onFilmClickListener!=null){
                    int position = getAbsoluteAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        onFilmClickListener.onFilmClick(position);
                    }
                }
            });
        }
        void set(ItemFilm itemFilm){
            this.name.setText(itemFilm.getName());
            this.shortDescription.setText(itemFilm.getShortDescription());
            this.lastEpisode.setText(itemFilm.getLastEpisode());
            Picasso.with(itemView.getContext()).load(itemFilm.getPosterUrl()).into(poster);
        }
    }
}
