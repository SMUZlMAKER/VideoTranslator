package com.example.videotranslator.MVP.Models;

import androidx.collection.ArrayMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
public class FilmDescriptionModel {
    ArrayMap<String,String> descriptionMap =new ArrayMap<>();

    public ArrayMap<String,String> LoadDescription(String filmUrl){
        Thread tr = new Thread(() -> {
            try {
                    SetDescription(filmUrl);
            } catch (IOException e) {
                descriptionMap.put("name","Ошибка загрузки, проверьте интернет или смените зеркало");
            }
        });
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException e) {
            descriptionMap.put("name","Загрузка данных была прервана");
        }
        return descriptionMap;
    }

    private int getPos(String[] array,String elem) {
        for(int i=0;i<array.length;i++)
            if (elem.equals(array[i]))
                return i;
        return -1;
    }

    private void SetDescription(String filmUrl) throws IOException {

        Document doc = Jsoup.connect(filmUrl).get();

        String[] descriptionTags={"Рейтинги","Дата выхода","Страна","Режиссер","Жанр","Время","В ролях актеры"};
        String[] textViews={"name","eng_name","rate", "date", "country", "director", "genre", "duration", "cast","description","posterUrl"};

        Element element = doc.getElementsByClass("b-post__title").first();
        if(element!=null)
            descriptionMap.put(textViews[0],element.text());
        else
            throw new IOException();
        element=doc.getElementsByClass("b-post__origtitle").first();
        if(element!=null)
            descriptionMap.put(textViews[1],element.text());

        element=doc.getElementsByClass("b-post__info").first();
        assert element!=null;
        Elements elements=element.getElementsByTag("tr");

        int size=elements.size();
        for(int i=0;i<size;i++) {
            int pos = getPos(descriptionTags, elements.get(i).getElementsByTag("h2").text());
            if (pos != -1) {
                String replacement;
                if (pos <= 1)
                    replacement = ":\n";
                else
                    replacement = ": ";

                descriptionMap.put(textViews[pos + 2], elements.get(i).text().replaceFirst("( : )|( Кинопоиск)", replacement));
            }
        }

        elements=doc.getElementsByClass("b-post__description_text");
        descriptionMap.put(textViews[9],elements.text());
        element=doc.getElementsByClass("b-sidecover").first();
        if(element!=null)
            descriptionMap.put(textViews[10], element.getElementsByTag("img").attr("src"));
    }
}
