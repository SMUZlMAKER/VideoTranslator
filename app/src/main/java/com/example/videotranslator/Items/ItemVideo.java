package com.example.videotranslator.Items;

public class ItemVideo {
    private int logo;
    private String translationSeasonEpisodeQuality;
    private String episode="0";
    private String season="0";
    private String m3u8;
    private String id;
    private String translator_id;

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTranslationSeasonEpisodeQuality() {
        return translationSeasonEpisodeQuality;
    }

    public void setTranslationSeasonEpisodeQuality(String translationSeasonEpisodeQuality) {
        this.translationSeasonEpisodeQuality = translationSeasonEpisodeQuality;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String qualityEpisode) {
        this.episode = qualityEpisode;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranslator_id() {
        return translator_id;
    }

    public void setTranslator_id(String translator_id) {
        this.translator_id = translator_id;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }
}
