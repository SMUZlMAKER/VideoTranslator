package com.example.videotranslator;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import org.junit.Test;

import androidx.collection.ArrayMap;

import com.example.videotranslator.Items.ItemVideo;
import com.example.videotranslator.MVP.Models.FilmDescriptionModel;
import com.example.videotranslator.MVP.Models.VideosModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class FilmDescriptionTest {

    public boolean descriptionComparison() {
        ArrayMap<String, String> expected = new ArrayMap<>();
        expected.put("name", "Криминальное чтиво");
        expected.put("eng_name", "Pulp Fiction");
        expected.put("date", "Дата выхода:\n21 мая 1994 года");
        expected.put("country", "Страна: США");
        expected.put("director", "Режиссер: Квентин Тарантино");
        expected.put("genre", "Жанр: Драмы, Триллеры, Криминал, Зарубежные");
        expected.put("duration", "Время: 154 мин.");
        expected.put("cast", "В ролях актеры: Джон Траволта, Сэмюэл Л. Джексон, Брюс Уиллис," +
                " Ума Турман, Винг Реймз, Тим Рот, Харви Кейтель, Квентин Тарантино, Питер Грин," +
                " Аманда Пламмер и другие");
        expected.put("description", "Сюжет картины состоит из нескольких историй, периодически " +
                "пересекающихся между собой. Два головореза – Джулс и Винсент, работающие на " +
                "крупного криминального авторитета Марселласа Уоллеса, получают очередное " +
                "задание: им нужно забрать чемодан босса. Джулс постоянно заводит философские " +
                "беседы о боге, однако Винсенту не до этого, ведь вечером ему предстоит " +
                "развлекать жену босса, а это дело может грозить ему серьезными неприятностями." +
                " В это самое время один человек рискует попасть в немилость к безжалостному " +
                "Марселласу – боксер Бутч, который за деньги гангстера должен проиграть в " +
                "боксерском поединке.");
        expected.put("posterUrl", "https://static.hdrezka.ac/i/2021/3/17/q6e275b47b126xt59x65c.jpeg");
        ArrayMap<String, String> received = new FilmDescriptionModel().LoadDescription(
                "https://hdrezkawer.org/films/drama/822-kriminalnoe-chtivo-1994.html");
        if (expected.size() + 1 != received.size())
            return false;
        Set<String> keys = expected.keySet();
        for (String key : keys)
            if (!Objects.equals(expected.get(key), received.get(key)))
                return false;
        String rate = received.get("rate");
        if (rate == null)
            return false;
        return rate.matches("Рейтинги:\nIMDb:[\\d.( )]*Кинопоиск:[\\d.( )]*");
    }

    @Test
    public void descriptionTest() {
        assertTrue(descriptionComparison());
        assertEquals("Ошибка загрузки, проверьте интернет или смените зеркало", new FilmDescriptionModel().LoadDescription(
                "https://hdrezkawer.org/films/drama/822-kriminalnoe-cht").get("name"));
    }

    public boolean translationComparison() {
        ArrayMap<String, String> tmp1 = new ArrayMap<>();
        tmp1.put("episode", "0");
        tmp1.put("id", "2090");
        tmp1.put("season", "0");
        tmp1.put("translationSeasonEpisodeQuality", "Кириллица");
        tmp1.put("translator_id", "117");

        ArrayMap<String, String> tmp2 = new ArrayMap<>();
        tmp2.put("episode", "0");
        tmp2.put("id", "2090");
        tmp2.put("season", "0");
        tmp2.put("translationSeasonEpisodeQuality", "TVShows");
        tmp2.put("translator_id", "232");
        ArrayMap<String, String> tmp3 = new ArrayMap<>();
        tmp3.put("episode", "0");
        tmp3.put("id", "2090");
        tmp3.put("season", "0");
        tmp3.put("translationSeasonEpisodeQuality", "лостфильм (LostFilm)");
        tmp3.put("translator_id", "1");
        ArrayMap<String, String> tmp4 = new ArrayMap<>();
        tmp4.put("episode", "0");
        tmp4.put("id", "2090");
        tmp4.put("season", "0");
        tmp4.put("translationSeasonEpisodeQuality", "ньюстудио (NewStudio)");
        tmp4.put("translator_id", "3");
        ArrayMap<String, String> tmp5 = new ArrayMap<>();
        tmp5.put("episode", "0");
        tmp5.put("id", "2090");
        tmp5.put("season", "0");
        tmp5.put("translationSeasonEpisodeQuality", "Украинский многоголосый");
        tmp5.put("translator_id", "359");
        ArrayMap<String, String> tmp6 = new ArrayMap<>();
        tmp6.put("episode", "0");
        tmp6.put("id", "2090");
        tmp6.put("season", "0");
        tmp6.put("translationSeasonEpisodeQuality", "Оригинал (+субтитры)");
        tmp6.put("translator_id", "238");

        ArrayList<ArrayMap<String, String>> expected = new ArrayList<>();
        expected.add(tmp1);
        expected.add(tmp2);
        expected.add(tmp3);
        expected.add(tmp4);
        expected.add(tmp5);
        expected.add(tmp6);

        Bundle args = mock(Bundle.class);
        when(args.getInt("chapter")).thenReturn(0);
        when(args.getString("id")).thenReturn("2090");
        when(args.getString("mirror")).thenReturn("https://hdrezkawer.org");
        when(args.getString("filmUrl")).thenReturn("https://hdrezkawer.org/series/fiction/2090-flesh-2014.html");

        ArrayList<ItemVideo> received = new VideosModel().LoadVideos(args);

        for (int i = 0; i < received.size(); i++) {
            if (!received.get(i).getEpisode().equals(expected.get(i).get("episode")) ||
                    !received.get(i).getId().equals(expected.get(i).get("id")) ||
                    !received.get(i).getSeason().equals(expected.get(i).get("season")) ||
                    !received.get(i).getTranslationSeasonEpisodeQuality().
                            equals(expected.get(i).get("translationSeasonEpisodeQuality")) ||
                    !received.get(i).getTranslator_id().equals(expected.get(i).get("translator_id"))
            )
                return false;
        }
        return true;
    }

    @Test
    public void translationTest() {
        assertTrue(translationComparison());
    }

    public boolean seasonComparison() {
        ArrayMap<String, String> tmp1 = new ArrayMap<>();
        tmp1.put("episode", "23");
        tmp1.put("id", "2090");
        tmp1.put("season", "1");
        tmp1.put("translationSeasonEpisodeQuality", "1 Сезон");
        tmp1.put("translator_id", "1");

        ArrayMap<String, String> tmp2 = new ArrayMap<>();
        tmp2.put("episode", "23");
        tmp2.put("id", "2090");
        tmp2.put("season", "2");
        tmp2.put("translationSeasonEpisodeQuality", "2 Сезон");
        tmp2.put("translator_id", "1");
        ArrayMap<String, String> tmp3 = new ArrayMap<>();
        tmp3.put("episode", "23");
        tmp3.put("id", "2090");
        tmp3.put("season", "3");
        tmp3.put("translationSeasonEpisodeQuality", "3 Сезон");
        tmp3.put("translator_id", "1");
        ArrayMap<String, String> tmp4 = new ArrayMap<>();
        tmp4.put("episode", "23");
        tmp4.put("id", "2090");
        tmp4.put("season", "4");
        tmp4.put("translationSeasonEpisodeQuality", "4 Сезон");
        tmp4.put("translator_id", "1");
        ArrayMap<String, String> tmp5 = new ArrayMap<>();
        tmp5.put("episode", "22");
        tmp5.put("id", "2090");
        tmp5.put("season", "0");
        tmp5.put("translationSeasonEpisodeQuality", "5 Сезон");
        tmp5.put("translator_id", "1");
        ArrayMap<String, String> tmp6 = new ArrayMap<>();
        tmp6.put("episode", "19");
        tmp6.put("id", "2090");
        tmp6.put("season", "6");
        tmp6.put("translationSeasonEpisodeQuality", "6 Сезон");
        tmp6.put("translator_id", "1");
        ArrayMap<String, String> tmp7 = new ArrayMap<>();
        tmp7.put("episode", "18");
        tmp7.put("id", "2090");
        tmp7.put("season", "7");
        tmp7.put("translationSeasonEpisodeQuality", "7 Сезон");
        tmp7.put("translator_id", "1");
        ArrayMap<String, String> tmp8 = new ArrayMap<>();
        tmp8.put("episode", "20");
        tmp8.put("id", "2090");
        tmp8.put("season", "8");
        tmp8.put("translationSeasonEpisodeQuality", "8 Сезон");
        tmp8.put("translator_id", "1");
        ArrayMap<String, String> tmp9 = new ArrayMap<>();
        tmp9.put("episode", "13");
        tmp9.put("id", "2090");
        tmp9.put("season", "9");
        tmp9.put("translationSeasonEpisodeQuality", "9 Сезон");
        tmp9.put("translator_id", "1");

        ArrayList<ArrayMap<String, String>> expected = new ArrayList<>();
        expected.add(tmp1);
        expected.add(tmp2);
        expected.add(tmp3);
        expected.add(tmp4);
        expected.add(tmp5);
        expected.add(tmp6);
        expected.add(tmp7);
        expected.add(tmp8);
        expected.add(tmp9);

        Bundle args = mock(Bundle.class);
        when(args.getInt("chapter")).thenReturn(1);
        when(args.getString("id")).thenReturn("2090");
        when(args.getString("mirror")).thenReturn("https://hdrezkawer.org");
        when(args.getString("filmUrl")).thenReturn("https://hdrezkawer.org/series/fiction/2090-flesh-2014.html");
        when(args.getString("translator_id")).thenReturn("1");

        ArrayList<ItemVideo> received = new VideosModel().LoadVideos(args);

        for (int i = 0; i < received.size(); i++) {
            if (!received.get(i).getEpisode().equals(expected.get(i).get("episode")) ||
                    !received.get(i).getId().equals(expected.get(i).get("id")) ||
                    !received.get(i).getSeason().equals(expected.get(i).get("season")) ||
                    !received.get(i).getTranslationSeasonEpisodeQuality().
                            equals(expected.get(i).get("translationSeasonEpisodeQuality")) ||
                    !received.get(i).getTranslator_id().equals(expected.get(i).get("translator_id"))
            )
                return false;
        }
        return true;
    }

    @Test
    public void seasonTest() {
        assertTrue(seasonComparison());
    }


    public boolean episodesComparison() {
        ArrayMap<String, String> tmp1 = new ArrayMap<>();
        tmp1.put("episode", "1");
        tmp1.put("id", "57880");
        tmp1.put("season", "1");
        tmp1.put("translationSeasonEpisodeQuality", "1 Серия");
        tmp1.put("translator_id", "238");

        ArrayMap<String, String> tmp2 = new ArrayMap<>();
        tmp2.put("episode", "2");
        tmp2.put("id", "57880");
        tmp2.put("season", "1");
        tmp2.put("translationSeasonEpisodeQuality", "2 Серия");
        tmp2.put("translator_id", "238");
        ArrayMap<String, String> tmp3 = new ArrayMap<>();
        tmp3.put("episode", "3");
        tmp3.put("id", "57880");
        tmp3.put("season", "1");
        tmp3.put("translationSeasonEpisodeQuality", "3 Серия");
        tmp3.put("translator_id", "238");
        ArrayMap<String, String> tmp4 = new ArrayMap<>();
        tmp4.put("episode", "4");
        tmp4.put("id", "57880");
        tmp4.put("season", "1");
        tmp4.put("translationSeasonEpisodeQuality", "4 Серия");
        tmp4.put("translator_id", "238");
        ArrayMap<String, String> tmp5 = new ArrayMap<>();
        tmp5.put("episode", "5");
        tmp5.put("id", "57880");
        tmp5.put("season", "1");
        tmp5.put("translationSeasonEpisodeQuality", "5 Серия");
        tmp5.put("translator_id", "238");


        ArrayList<ArrayMap<String, String>> expected = new ArrayList<>();
        expected.add(tmp1);
        expected.add(tmp2);
        expected.add(tmp3);
        expected.add(tmp4);
        expected.add(tmp5);

        Bundle args = mock(Bundle.class);
        when(args.getInt("chapter")).thenReturn(2);
        when(args.getString("id")).thenReturn("57880");
        when(args.getString("mirror")).thenReturn("https://hdrezkawer.org");
        when(args.getString("filmUrl")).thenReturn("https://hdrezkawer.org/series/realtv/57880-sirena-vyzhit-na-ostrove-2023.html");
        when(args.getString("translator_id")).thenReturn("238");
        when(args.getString("season")).thenReturn("1");
        when(args.getString("episode")).thenReturn("5");

        ArrayList<ItemVideo> received = new VideosModel().LoadVideos(args);

        for (int i = 0; i < received.size(); i++) {
            if (!received.get(i).getEpisode().equals(expected.get(i).get("episode")) ||
                    !received.get(i).getId().equals(expected.get(i).get("id")) ||
                    !received.get(i).getSeason().equals(expected.get(i).get("season")) ||
                    !received.get(i).getTranslationSeasonEpisodeQuality().
                            equals(expected.get(i).get("translationSeasonEpisodeQuality")) ||
                    !received.get(i).getTranslator_id().equals(expected.get(i).get("translator_id"))
            )
                return false;
        }
        return true;
    }

    @Test
    public void episodesTest() {
        assertTrue(episodesComparison());
    }


    public boolean videosComparison() {
        ArrayMap<String, String> tmp1 = new ArrayMap<>();
        tmp1.put("episode", "0");
        tmp1.put("id", "57880");
        tmp1.put("season", "1");
        tmp1.put("translationSeasonEpisodeQuality", "360p");
        tmp1.put("translator_id", "238");

        ArrayMap<String, String> tmp2 = new ArrayMap<>();
        tmp2.put("episode", "0");
        tmp2.put("id", "57880");
        tmp2.put("season", "1");
        tmp2.put("translationSeasonEpisodeQuality", "480p");
        tmp2.put("translator_id", "238");
        ArrayMap<String, String> tmp3 = new ArrayMap<>();
        tmp3.put("episode", "0");
        tmp3.put("id", "57880");
        tmp3.put("season", "1");
        tmp3.put("translationSeasonEpisodeQuality", "720p");
        tmp3.put("translator_id", "238");
        ArrayMap<String, String> tmp4 = new ArrayMap<>();
        tmp4.put("episode", "0");
        tmp4.put("id", "57880");
        tmp4.put("season", "1");
        tmp4.put("translationSeasonEpisodeQuality", "1080p");
        tmp4.put("translator_id", "238");


        ArrayList<ArrayMap<String, String>> expected = new ArrayList<>();
        expected.add(tmp1);
        expected.add(tmp2);
        expected.add(tmp3);
        expected.add(tmp4);

        Bundle args = mock(Bundle.class);
        when(args.getInt("chapter")).thenReturn(3);
        when(args.getString("id")).thenReturn("57880");
        when(args.getString("mirror")).thenReturn("https://hdrezkawer.org");
        when(args.getString("filmUrl")).thenReturn("https://hdrezkawer.org/series/realtv/57880-sirena-vyzhit-na-ostrove-2023.html");
        when(args.getString("translator_id")).thenReturn("238");
        when(args.getString("season")).thenReturn("1");
        when(args.getString("episode")).thenReturn("1");

        ArrayList<ItemVideo> received = new VideosModel().LoadVideos(args);

        for (int i = 0; i < received.size(); i++) {
            if (!received.get(i).getEpisode().equals(expected.get(i).get("episode")) ||
                    !received.get(i).getId().equals(expected.get(i).get("id")) ||
                    !received.get(i).getSeason().equals(expected.get(i).get("season")) ||
                    !received.get(i).getTranslationSeasonEpisodeQuality().
                            equals(expected.get(i).get("translationSeasonEpisodeQuality")) ||
                    !received.get(i).getTranslator_id().equals(expected.get(i).get("translator_id"))
            )
                return false;
        }
        return true;
    }

    @Test
    public void videosTest() {
        assertTrue(videosComparison());
    }

}
