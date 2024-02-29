package com.example.videotranslator.MVP.Presenters;

import android.os.Bundle;

import com.example.videotranslator.Items.ItemVideo;
import com.example.videotranslator.MVP.Models.VideosModel;
import com.example.videotranslator.MVP.Views.VideosFragment;

import java.util.ArrayList;

public class VideosPresenter {
    VideosFragment view;
    VideosModel model;

    public VideosPresenter(VideosFragment view) {
        this.model =new VideosModel();
        this.view = view;
    }

    public ArrayList<ItemVideo> LoadVideos(Bundle args){
        ArrayList<ItemVideo> videos =model.LoadVideos(args);
        if(videos!=null)
            view.ShowFilms(videos);
        else
            view.ShowError();
        return videos;
    }
}
