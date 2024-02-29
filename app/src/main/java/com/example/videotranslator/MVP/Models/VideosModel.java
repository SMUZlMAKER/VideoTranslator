package com.example.videotranslator.MVP.Models;

import android.os.Bundle;
import android.util.Base64;

import com.example.videotranslator.Items.ItemVideo;
import com.example.videotranslator.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideosModel {

    ArrayList<ItemVideo> videos = new ArrayList<>();

    public ArrayList<ItemVideo> LoadVideos(Bundle args) {
        Thread tr;
        tr = new Thread(() -> {
            try {
                switch (args.getInt("chapter")) {
                    case 0 -> SetTranslators(args);
                    case 1 -> {
                        if (!SetSeasons(args)) {
                            args.putString("season", "0");
                            args.putString("episode", "0");
                            args.putInt("chapter", 3);
                            SetVideos(args);
                        }
                    }
                    case 2 -> SetEpisodes(args);
                    case 3 -> SetVideos(args);
                }
            } catch (IOException | JSONException e) {
                videos=null;
            }
        });

        tr.start();
        try {
            tr.join();
        } catch (InterruptedException e) {
            videos=null;
        }
        return videos;
    }


    private void SetTranslators(Bundle args) throws IOException {
        Document doc = Jsoup.connect(args.getString("filmUrl")).get();

        Elements elements = doc.getElementsByClass("b-translator__item");
        if (!elements.isEmpty())
            elements.forEach(it -> {
                ItemVideo tmp = new ItemVideo();
                tmp.setId(args.getString("id"));
                tmp.setTranslator_id(it.attr("data-translator_id"));
                tmp.setTranslationSeasonEpisodeQuality(it.text());
                tmp.setLogo(R.drawable.baseline_language_24);
                videos.add(tmp);
            });
        else {
            String translator_id;
            String translationName = "Отсутствуют данные о переводе";
            elements = doc.getElementsByTag("script");

            String cdnMovies = null;
            for (Element el : elements)
                if (el.toString().startsWith("<script> $")) {
                    cdnMovies = el.toString();
                    break;
                }
            assert cdnMovies != null;
            int start = cdnMovies.indexOf(",") + 2;
            int end = cdnMovies.indexOf(",", start);
            translator_id = cdnMovies.substring(start, end);

            elements = Objects.requireNonNull(doc.getElementsByClass("b-post__infotable_right_inner")
                    .first()).getElementsByTag("tr");

            for (Element el : elements)
                if (el.getElementsByTag("h2").text().equals("В переводе"))
                    translationName = el.getElementsByTag("td").get(1).text();

            ItemVideo tmp = new ItemVideo();
            tmp.setId(args.getString("id"));
            tmp.setTranslator_id(translator_id);
            tmp.setTranslationSeasonEpisodeQuality(translationName);
            tmp.setLogo(R.drawable.baseline_language_24);
            videos.add(tmp);
        }
    }


    private boolean SetSeasons(Bundle args) throws JSONException, IOException {
        long t = new Date().getTime();
        String url = args.getString("mirror") + "/ajax/get_cdn_series/?t=" + t;
        okhttp3.RequestBody requestBody = new FormBody.Builder()
                .add("id", args.getString("id"))
                .add("translator_id", args.getString("translator_id"))
                .add("action", "get_episodes")
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String episodes;
        JSONObject json;
        try (Response response = httpClient.newCall(request).execute()) {
            assert response.body() != null;
            String responseBody = response.body().string();
            json = new JSONObject(responseBody);
        }
        if (json.getBoolean("success")) {
            episodes = json.getString("episodes");
            Document doc = Jsoup.parse(episodes);
            Elements elements = doc.getElementsByTag("ul");
            int size = elements.size();
            for (int i = 1; i <= size; i++) {
                ItemVideo tmp = new ItemVideo();
                tmp.setId(args.getString("id"));
                tmp.setTranslator_id(args.getString("translator_id"));
                tmp.setSeason(String.valueOf(i));
                tmp.setEpisode(String.valueOf(elements.get(i - 1).childrenSize()));
                tmp.setTranslationSeasonEpisodeQuality((i) + " Сезон");
                tmp.setLogo(R.drawable.baseline_folder_24);
                videos.add(tmp);
            }
        } else {
            return false;
        }
        return true;
    }

    private void SetEpisodes(Bundle args) {
        int size = Integer.parseInt(args.getString("episode"));
        for (int i = 1; i <= size; i++) {
            ItemVideo tmp = new ItemVideo();
            tmp.setId(args.getString("id"));
            tmp.setTranslator_id(args.getString("translator_id"));
            tmp.setSeason(args.getString("season"));
            tmp.setEpisode(String.valueOf(i));
            tmp.setTranslationSeasonEpisodeQuality(i + " Серия");
            tmp.setLogo(R.drawable.baseline_videocam_24);
            videos.add(tmp);
        }
    }

    private String setAction(String season) {
        if (season.equals("0"))
            return "get_movie";
        else
            return "get_stream";
    }

    private void SetVideos(Bundle args) throws JSONException, IOException {
        long t = new Date().getTime();
        String url = args.getString("mirror") + "/ajax/get_cdn_series/?t=" + t;
        okhttp3.RequestBody requestBody = new FormBody.Builder()
                .add("id", args.getString("id"))
                .add("translator_id", args.getString("translator_id"))
                .add("season", args.getString("season"))
                .add("episode", args.getString("episode"))
                .add("action", setAction(args.getString("season")))
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        String coded;
        try (Response response = httpClient.newCall(request).execute()) {
            assert response.body() != null;
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            if (json.getBoolean("success"))
                coded = json.getString("url").substring(2);
            else
                throw new IOException("Отсутствуют данные о сезонах и эпизодах");
        }
        coded = coded.replaceAll("//_//((JCQjISFAIyFAIyM=)|(Xl5eIUAjIyEhIyM=)|(IyMjI14hISMjIUBA)|(QEBAQEAhIyMhXl5e)|(JCQhIUAkJEBeIUAjJCRA))", "");
        String decoded = new String(Base64.decode(coded, Base64.DEFAULT));
        Pattern pattern = Pattern.compile("\\[(\\d*p)]([^ ]*)");
        Matcher matcher = pattern.matcher(decoded);
        int start = 0;
        while (matcher.find(start)) {
            start = decoded.indexOf(Objects.requireNonNull(matcher.group(1)));
            ItemVideo tmp = new ItemVideo();
            tmp.setId(args.getString("id"));
            tmp.setTranslator_id(args.getString("translator_id"));
            tmp.setSeason(args.getString("season"));
            tmp.setEpisode(args.getString("episode"));
            tmp.setTranslationSeasonEpisodeQuality(matcher.group(1));
            tmp.setM3u8(matcher.group(2));
            tmp.setLogo(R.drawable.baseline_hd_24);
            videos.add(tmp);
        }
    }
}
