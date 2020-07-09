package nathan.luka.myseries.model.usermetadata;

import nathan.luka.myseries.model.Season;
import nathan.luka.myseries.model.Serie;

import java.util.ArrayList;
import java.util.List;

public class UserSeasonMetaData {
    private Integer seasonNumber;
    private Integer themoviedbSerieID;
    private final List<UserEpisodeMetaData> userEpisodeMetaDataList;
    private Serie serie;
    private Season season;
    private boolean watched;

    public UserSeasonMetaData(Integer seasonNumber, Integer themoviedbSerieID, List<UserEpisodeMetaData> userEpisodeMetaDataList) {
        this.seasonNumber = seasonNumber;
        this.themoviedbSerieID = themoviedbSerieID;
        this.userEpisodeMetaDataList = userEpisodeMetaDataList;
    }

    public UserSeasonMetaData(Serie serie, Season season) {
        this.season = season;
        this.serie = serie;
        this.seasonNumber = season.getSeasonNumber();
        this.themoviedbSerieID = serie.getThemoviedbSerieID();
        this.userEpisodeMetaDataList = new ArrayList<>();
        for (int i = 0; i < season.getEpisodes().size(); i++) {
            userEpisodeMetaDataList.add(0, new UserEpisodeMetaData(season.getEpisodes().get(i)));
        }
    }


    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getThemoviedbSerieID() {
        return themoviedbSerieID;
    }

    public void setThemoviedbSerieID(Integer themoviedbSerieID) {
        this.themoviedbSerieID = themoviedbSerieID;
    }

    public List<UserEpisodeMetaData> getUserEpisodeMetaDataList() {
        return userEpisodeMetaDataList;
    }


}
