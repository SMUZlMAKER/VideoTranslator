package com.example.videotranslator.MVP.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotranslator.FilmActivity;
import com.example.videotranslator.MVP.Presenters.SelectionPresenter;
import com.example.videotranslator.R;
import com.example.videotranslator.common.OnFilmClickListener;
import com.example.videotranslator.common.SelectionAdapter;
import com.example.videotranslator.Items.ItemFilm;

import java.util.ArrayList;

public class SelectionOfFilmFragment extends Fragment implements OnFilmClickListener {
    ArrayList<ItemFilm> films = new ArrayList<>();
    int numberPage;
    String page;
    String q;
    String mirror;
    String title;
    SelectionAdapter adapter;
    TextView titleView;
    public SelectionOfFilmFragment(){
        super(R.layout.selection_of_films_fragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleView = view.findViewById(R.id.selection);

        Bundle args = this.getArguments();
        assert args != null;
        page = args.getString("page", null);
        numberPage = args.getInt("numberPage");
        q = args.getString("q", null);
        mirror = args.getString("mirror", null);
        title = args.getString("title");

        titleView.setText(title);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        adapter = new SelectionAdapter(this);
        recyclerView.setAdapter(adapter);

        SelectionPresenter presenter = new SelectionPresenter(this);

        if (q == null)
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    assert layoutManager != null;
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisible = layoutManager.findLastVisibleItemPosition();
                    if (totalItemCount - 1 == lastVisible) {
                        numberPage++;
                        films = presenter.LoadFilms(mirror, page, numberPage, q);
                    }
                }
            });

        films = presenter.LoadFilms(mirror, page, numberPage, q);


    }

    public void ShowFilms(String title,ArrayList<ItemFilm> films){
        if(title!=null) {
            this.title = title;
            titleView.setText(title);
        }
        adapter.SetData(films);
    }


    @Override
    public void onFilmClick(int position) {
        Intent intent = new Intent(getActivity(), FilmActivity.class);
        intent.putExtra("filmUrl",films.get(position).getFilmUrl())
                .putExtra("id",films.get(position).getId())
                .putExtra("mirror",mirror);
        startActivity(intent);

    }
}
