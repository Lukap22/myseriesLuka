package nathan.luka.myseries.datacollector;

import nathan.luka.myseries.model.Episode;
import nathan.luka.myseries.model.Season;
import nathan.luka.myseries.model.Serie;
import nathan.luka.myseries.model.gjson.SeasonTheMovieDB;
import nathan.luka.myseries.model.gjson.SerieTheMovieDB;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class SerieMerger implements Callable<Serie> {
    private final SerieTheMovieDB serieTheMovieDB;
    private final List<SeasonTheMovieDB> seasonTheMovieDBList;
    private final Serie result;

    public SerieMerger(SerieTheMovieDB serieTheMovieDB, List<SeasonTheMovieDB> seasonTheMovieDBList) {
        this.serieTheMovieDB = serieTheMovieDB;
        this.seasonTheMovieDBList = seasonTheMovieDBList;
        this.result = new Serie();
        convertSerieTheMovieDBToSerie();
        mergeSeasonsWithSerie();
    }

    /**
     * This a nested class to preform asyn methodes
     */
    class MergeSeasonWithSerie implements Runnable {
        private final SeasonTheMovieDB seasonTheMovieDB;
        private final SerieTheMovieDB serieTheMovieDB;
        private final Serie result;

        public MergeSeasonWithSerie(SeasonTheMovieDB seasonTheMovieDB, SerieTheMovieDB serieTheMovieDB, Serie serie) {
            this.seasonTheMovieDB = seasonTheMovieDB;
            this.serieTheMovieDB = serieTheMovieDB;
            this.result = serie;
            run();
        }

        /**
         * Extracts seasons &  episodes from seasonTheMovieDBList and adds it to result.
         */
        public void run() {
            if (Objects.equals(serieTheMovieDB.getId(), seasonTheMovieDB.getEpisodes().get(0).getShowId())) {
                System.out.println("ID match found " + seasonTheMovieDB.getEpisodes().get(0).getShowId());
                //Season is of the same Serie.

                //converting
                Season season = convertSeasonTheMovieDBToSeason(this.seasonTheMovieDB);
                List<nathan.luka.myseries.model.gjson.Episode> episodes = seasonTheMovieDB.getEpisodes();
                for (nathan.luka.myseries.model.gjson.Episode episode : episodes) {
                    season.getEpisodes().add(new Episode(episode.getName(), episode.getSeasonNumber(), episode.getEpisodeNumber(), episode.getShowId(), episode.getAirDate()));
                }
                result.getSeasons().add(season);
            }
        }
    }


    /**
     * This method provides the nested MergeSeasonWithSerie.class with the needed params to add seasons to result, asyn to speed up the progress.
     */
    public void mergeSeasonsWithSerie() {
        ExecutorService executor = Executors.newFixedThreadPool(seasonTheMovieDBList.size());
        for (int i = 0; i < seasonTheMovieDBList.size(); i++) {
            executor.execute(new MergeSeasonWithSerie(seasonTheMovieDBList.get(i), serieTheMovieDB, result));
        }
    }

    /**
     * Extracts all data from serieTheMovieDB : SerieTheMovieDB to result : Serie
     */
    public void convertSerieTheMovieDBToSerie() {
        result.setThemoviedbSerieID(serieTheMovieDB.getId());
        result.setTitle(serieTheMovieDB.getName());
        result.setAmountOfEpisodes(serieTheMovieDB.getNumberOfEpisodes());
        result.setAmountOfSeasons(serieTheMovieDB.getNumberOfSeasons());
        result.setStatus(serieTheMovieDB.getStatus());
        result.setDescription(serieTheMovieDB.getOverview());
        result.setGenres(serieTheMovieDB.getGenres());
        //todo download image
        result.setImageurl("https://image.tmdb.org/t/p/original" + serieTheMovieDB.getBackdropPath());
        result.setGridImageURL("https://image.tmdb.org/t/p/w300_and_h450_bestv2" + serieTheMovieDB.getPosterPath());
    }

    /**
     * Extracts all data from SeasonTheMovieDB.class to Season
     *
     * @param seasonTheMovieDB imported class from themoviedb
     * @return Season
     */
    public Season convertSeasonTheMovieDBToSeason(SeasonTheMovieDB seasonTheMovieDB) {
        return new Season(seasonTheMovieDB.getName(), seasonTheMovieDB.getSeasonNumber(), result.getThemoviedbSerieID(), seasonTheMovieDB.getEpisodes().size());
    }

    @Override
    public Serie call() throws Exception {
        return result;
    }
}



