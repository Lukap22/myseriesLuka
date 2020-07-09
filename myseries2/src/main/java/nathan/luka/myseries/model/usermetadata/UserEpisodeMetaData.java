package nathan.luka.myseries.model.usermetadata;

import nathan.luka.myseries.model.Episode;

public class UserEpisodeMetaData {
    private Integer seasonNumber;
    private Integer episodeNumber;
    private Integer tmdbID;
    private Boolean watched;
    private Episode episode;

    public UserEpisodeMetaData(Integer seasonNumber, Integer episodeNumber, Integer tmdbID, Boolean watched) {
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.tmdbID = tmdbID;
        this.watched = watched;
    }
    public UserEpisodeMetaData(Episode episode) {
        this.episode = episode;
        this.seasonNumber = episode.getSeasonNumber();
        this.episodeNumber = episode.getEpisodeNumber();
        this.tmdbID = episode.getTmdbID();
        this.watched = false;
    }
    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Integer getTmdbID() {
        return tmdbID;
    }

    public void setTmdbID(Integer tmdbID) {
        this.tmdbID = tmdbID;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }



}
