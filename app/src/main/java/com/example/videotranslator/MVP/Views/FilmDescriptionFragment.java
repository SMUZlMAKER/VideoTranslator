package com.example.videotranslator.MVP.Views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.videotranslator.MVP.Presenters.FilmDescriptionPresenter;
import com.example.videotranslator.R;
import com.squareup.picasso.Picasso;

public class FilmDescriptionFragment extends Fragment {
    ArrayMap<String,TextView> texts = new ArrayMap<>();
    ImageView poster;
    Context context;
    FilmDescriptionPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.film_description_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        texts.put("name",view.findViewById(R.id.name));
        texts.put("eng_name",view.findViewById(R.id.eng_name));
        texts.put("rate",view.findViewById(R.id.rate));
        texts.put("date",view.findViewById(R.id.date));
        texts.put("country",view.findViewById(R.id.country));
        texts.put("director",view.findViewById(R.id.director));
        texts.put("genre",view.findViewById(R.id.genre));
        texts.put("duration",view.findViewById(R.id.duration));
        texts.put("cast",view.findViewById(R.id.cast));
        texts.put("description",view.findViewById(R.id.description));

        poster = view.findViewById(R.id.film_poster);
        context = view.getContext();

        presenter = new FilmDescriptionPresenter(this);
        Bundle args=getArguments();
        assert args != null;


        presenter.LoadDescription(args.getString("filmUrl"));

        view.findViewById(R.id.watch).setOnClickListener(view1 -> {
            FragmentTransaction ft= getParentFragmentManager().beginTransaction();
            VideosFragment fragment = new VideosFragment();
            Bundle newargs=args.deepCopy();
            newargs.putInt("chapter",0);
            fragment.setArguments(newargs);
            ft.replace(R.id.film_container,fragment).commit();
        });
    }

    public void ShowDescription(ArrayMap<String,String> descriptionMap){
        descriptionMap.forEach((key,value)->{
            TextView tmp=texts.get(key);
            if(tmp!=null) {
                tmp.setText(value);
            }
            else if(key.equals("posterUrl"))
                Picasso.with(context).load(value)
                        .into(poster);
        });
    }
}