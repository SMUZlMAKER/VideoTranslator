package com.example.videotranslator.MVP.Presenters;

import android.util.Pair;

import com.example.videotranslator.MVP.Models.SelectionModel;
import com.example.videotranslator.MVP.Views.SelectionOfFilmFragment;
import com.example.videotranslator.Items.ItemFilm;

import java.util.ArrayList;

public class SelectionPresenter {
    SelectionOfFilmFragment view;
    SelectionModel model;

    public SelectionPresenter(SelectionOfFilmFragment view) {
        this.model =new SelectionModel();
        this.view = view;
    }

    public ArrayList<ItemFilm> LoadFilms(String mirror, String page,int numberPage, String q){
        Pair<String,ArrayList<ItemFilm>> selection = model.LoadFilms(mirror,page,numberPage,q);
        view.ShowFilms(selection.first,selection.second);
        return selection.second;
    }
}
