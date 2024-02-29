package com.example.videotranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.videotranslator.MVP.Views.FilmDescriptionFragment;

public class FilmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        FilmDescriptionFragment filmDescription = new FilmDescriptionFragment();
        Intent intent=getIntent();
        Bundle args = new Bundle();
        args.putString("filmUrl", intent.getStringExtra("filmUrl"));
        args.putString("id",intent.getStringExtra("id"));
        args.putString("mirror",intent.getStringExtra("mirror"));
        filmDescription.setArguments(args);

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.film_container, filmDescription).commit();
    }
}