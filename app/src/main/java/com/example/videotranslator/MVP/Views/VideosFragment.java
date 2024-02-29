package com.example.videotranslator.MVP.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotranslator.common.OnVideoClickListener;
import com.example.videotranslator.common.VideosAdapter;

import com.example.videotranslator.MVP.Presenters.VideosPresenter;
import com.example.videotranslator.R;
import com.example.videotranslator.Items.ItemVideo;

import java.util.ArrayList;

public class VideosFragment extends Fragment implements OnVideoClickListener {
    ArrayList<ItemVideo> videos = new ArrayList<>();
    VideosAdapter adapter;

    VideosPresenter presenter;


    public VideosFragment() {
        super(R.layout.videos_fragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.videos_list);
        adapter = new VideosAdapter(view.getContext(), videos, this);
        recyclerView.setAdapter(adapter);
        Bundle args=getArguments();
        presenter = new VideosPresenter(this);
        videos =presenter.LoadVideos(args);
    }

    public void ShowFilms(ArrayList<ItemVideo> videos) {
        adapter.SetData(videos);
    }
    public void ShowError(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this.requireContext());
        alert.setTitle("Ошибка загрузки");

        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            dialog.cancel();
            requireActivity().finish();
        });
        alert.show();
    }

    @Override
    public void onVideoClick(int position) {
        VideosFragment videosFragment = new VideosFragment();
        assert getArguments() != null;
        Bundle args = getArguments().deepCopy();
        assert args !=null;
        switch (args.getInt("chapter")) {
            case 0 -> {
                String a =videos.get(position).getTranslator_id();
                args.putString("translator_id", a);
                args.putInt("chapter", args.getInt("chapter") + 1);
            }
            case 1 -> {
                args.putString("season", videos.get(position).getSeason());
                //этот эпизод - последний в сезоне, для исключения повторного post запроса
                args.putString("episode", videos.get(position).getEpisode());
                args.putInt("chapter", args.getInt("chapter") + 1);
            }
            case 2 -> {
                args.putString("episode", videos.get(position).getEpisode());
                args.putInt("chapter", args.getInt("chapter") + 1);
            }
            case 3 -> {
                Uri address = Uri.parse(videos.get(position).getM3u8());

                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW);
                openLinkIntent.setDataAndType(address, "video/*");
                startActivity(openLinkIntent);
            }
        }
        videosFragment.setArguments(args);
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.film_container, videosFragment).commit();
    }


}
