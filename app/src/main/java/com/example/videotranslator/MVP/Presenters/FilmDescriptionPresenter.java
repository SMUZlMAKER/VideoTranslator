package com.example.videotranslator.MVP.Presenters;

import androidx.collection.ArrayMap;

import com.example.videotranslator.MVP.Models.FilmDescriptionModel;
import com.example.videotranslator.MVP.Views.FilmDescriptionFragment;

public class FilmDescriptionPresenter {

    FilmDescriptionFragment view;
    FilmDescriptionModel model;

    public FilmDescriptionPresenter(FilmDescriptionFragment filmDescriptionFragment){
        this.view=filmDescriptionFragment;
        this.model = new FilmDescriptionModel();
    }


    public void LoadDescription(String filmUrl){
        ArrayMap<String,String> descriptionMap= model.LoadDescription(filmUrl);
        view.ShowDescription(descriptionMap);
    }


}
