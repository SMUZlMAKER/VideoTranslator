package com.example.videotranslator.MVP.Models;

import android.util.Pair;

import com.example.videotranslator.Items.ItemFilm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectionModel {
    ArrayList<ItemFilm> films = new ArrayList<>();
    String title;

    public Pair<String, ArrayList<ItemFilm>> LoadFilms(String mirror, String page,int numberPage, String q) {
        Thread tr;
        tr = new Thread(() -> {
            try {
                if (q != null)
                    SetSearch(mirror, page, q);
                else
                    SetSelection(mirror, page,numberPage);
            } catch (IOException e) {
                title = "Ошибка загрузки, проверьте интернет или смените зеркало";
            }
        });
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException e) {
            title = "Загрузка данных была прервана";
        }
        return new Pair<>(title, films);
    }

    /*метод поиска реализован не лучшим способом, ввиду того что при загрузке
     страницы поиска (зеркало/search...) в doc в ней отсутсвуют результаты этого поиска*/
    private void SetSearch(String mirror, String page, String q) throws IOException {
        okhttp3.RequestBody requestBody = new FormBody.Builder()
                .add("q", q)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mirror + page)
                .post(requestBody)
                .build();

        Document doc;
        try (Response response = httpClient.newCall(request).execute()) {
            assert response.body() != null;
            doc = Jsoup.parse(response.body().string());
        }
        Element element = doc.getElementsByClass("b-search__section_list").first();
        //assert element != null;
        if(element!=null) {
            ArrayList<String> links = (ArrayList<String>) element.getElementsByTag("a").eachAttr("href");
            for (String it : links) {
                ItemFilm tmp = new ItemFilm();
                tmp.setFilmUrl(it);

                Document tempdoc = Jsoup.connect(it).get();

                Element tmpEl = tempdoc.getElementsByAttributeValue("id", "send-video-issue").first();
                assert tmpEl != null;
                tmp.setId(tmpEl.attr("data-id"));
                tmpEl = tempdoc.getElementsByClass("b-sidecover").first();
                if (tmpEl != null)
                    tmp.setPosterUrl(tmpEl.getElementsByTag("img").attr("src"));
                tmpEl = tempdoc.getElementsByClass("b-post__title").first();

                if (tmpEl != null)
                    tmp.setName(tmpEl.text());
                else
                    tmp.setName("Ошибка при загрузке");
                tmpEl = tempdoc.getElementsByClass("b-post__info").first();

                if (tmpEl != null) {
                    Elements elements = tmpEl.getElementsByTag("tr");
                    StringBuilder shortDescription = new StringBuilder();
                    String[] descrEl = {"????", "????", "?????"};
                    elements.forEach(el -> {
                        switch (el.getElementsByTag("h2").text()) {
                            case "Дата выхода" -> {
                                Element temp = el.getElementsByTag("a").first();
                                if (temp != null)
                                    descrEl[0] = temp.text().substring(0, 4);
                            }
                            case "Страна" -> {
                                Element temp = el.getElementsByTag("a").first();
                                if (temp != null)
                                    descrEl[1] = temp.text();
                            }
                            case "Жанр" -> {
                                Element temp = el.getElementsByTag("a").first();
                                if (temp != null)
                                    descrEl[2] = temp.text();
                            }
                        }
                    });
                    shortDescription.append(descrEl[0]).append(",").append(descrEl[1]).append(",").append(descrEl[2]);
                    tmp.setShortDescription(shortDescription.toString());
                    StringBuilder lastepisode = new StringBuilder();
                    element = tempdoc.getElementsByClass("b-simple_episodes__list clearfix").last();
                    if (element != null) {
                        element = element.lastElementChild();
                        if (element != null) {
                            lastepisode.append(element.attr("data-season_id")).append(" Сезон,").append(element.text());
                            tmp.setLastEpisode(lastepisode.toString());
                        }
                    }
                }
                films.add(tmp);
            }
        }else{
            title="Ошибка поиска, исправьте запрос";
        }
    }

    private void SetSelection(String mirror, String page,int numberPage) throws IOException {
        Document doc;
        doc = Jsoup.connect(mirror + page+numberPage+"/").get();
        Elements elements = doc.getElementsByClass("b-content__inline_item");

        elements.forEach(it -> {
            ItemFilm tmpFilm = new ItemFilm();
            tmpFilm.setId(it.attr("data-id"));
            Element nameUrlDescr = it.getElementsByClass("b-content__inline_item-link").first(),
                    posterUrl = it.getElementsByClass("b-content__inline_item-cover").first();

            if (nameUrlDescr != null && posterUrl != null) {
                tmpFilm.setName(nameUrlDescr.select("a").text());
                tmpFilm.setFilmUrl(nameUrlDescr.select("a").attr("href"));
                tmpFilm.setShortDescription(nameUrlDescr.getElementsByTag("div").get(1).text());
                tmpFilm.setPosterUrl(posterUrl.getElementsByTag("img").attr("src"));

                Element lastEpisode = posterUrl.getElementsByClass("info").first();
                if (lastEpisode != null)
                    tmpFilm.setLastEpisode(lastEpisode.text());
            } else
                tmpFilm.setName("Ошибка при загрузке");
            films.add(tmpFilm);
        });

    }
}
