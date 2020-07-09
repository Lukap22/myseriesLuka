package nathan.luka.myseries.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@JsonPropertyOrder(alphabetic = true)
@JsonRootName(value = "Season")
public class Season {


    private Integer episodeCount;
    private List<Episode> episodes;

    private String name;
    private Integer seasonNumber;
    private Integer themoviedbSerieID;


    public Season(String name, Integer seasonNumber, Integer themoviedbSerieID, Integer episodeCount) {
        this.episodes = new ArrayList<>();
        this.name = name;
        this.seasonNumber = seasonNumber;
        this.episodeCount = episodeCount;
        this.themoviedbSerieID = themoviedbSerieID;
    }


    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static class SortbySeasonNumber implements Comparator<Season> {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Season a, Season b) {
            return a.seasonNumber - b.seasonNumber;
        }
    }

}
