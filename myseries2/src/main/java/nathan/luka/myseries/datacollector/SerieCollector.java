package nathan.luka.myseries.datacollector;

import com.google.gson.Gson;
import nathan.luka.myseries.model.Serie;
import nathan.luka.myseries.model.gjson.SeasonTheMovieDB;
import nathan.luka.myseries.model.gjson.SerieTheMovieDB;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.*;

public class SerieCollector implements Callable<Serie> {
    private static ArrayList<SeasonTheMovieDB> seasonsList;

    static SerieTheMovieDB serieTheMovieDB;
    boolean completed;
    public SerieCollector(int serieID, int amountOfSeasons, int option) {
        completed = false;
        seasonsList = new ArrayList<>();
        getSerieWithSeason(serieID, amountOfSeasons, option);
    }

    /**
     *     This method creates the proper URL link to send URL to the getResponseBody for the amount of times needed.
     *
     *     option 1: Serie only
     *     option 2: Season only
     *     option 3: both
     * @param serieID
     * @param amountOfSeasons
     * @param option
     * @return
     */
    public SerieTheMovieDB getSerieWithSeason(int serieID, int amountOfSeasons, int option) {
        String website = "https://api.themoviedb.org/3/tv/";
        String season = "/season/";
        String var = "?";
        String api_key = "api_key=8acc85d41e6a21f84c2651c11e9453c7";
        String language = "&language=en-US";
        String result;

        switch (option) {
            case 1:
                result = website + serieID + var + api_key + language;
                getResponseBody(result);
                break;
            case 2:
                for (int i = 0; i < amountOfSeasons; i++) {
                    result = website + serieID + season + i + var + api_key + language;
                    getResponseBody(result);
                }
                break;
            case 3:
                getSerieWithSeason(serieID, amountOfSeasons, 1);
                getSerieWithSeason(serieID, amountOfSeasons, 2);
                break;
        }
        return null;
    }


    /**
     * This method preforms a request to the api from themoviedb, to get the serie/seasons which then sends the string to the parse method.
      * @param urls
     */
    public void getResponseBody(String urls) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urls))
                    //cancel if timeout == 2min
                .timeout(Duration.ofMinutes(2))
                    .build();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    //get the body
                    .thenApply(HttpResponse::body)
                    //print body
                    .thenApply(SerieCollector::parse)
                    .join();
    }

    /**
     * Determines if responseBody is a season object
     * @param responseBody jsonString of serie or season
     * @return
     */
    public static boolean determineIfJSONStrIsSeason(String responseBody) {
        if (responseBody.contains("season_number")) {
            return true;
        }
        return false;
    }

    /**
     * Determines if responseBody is a serie object
     * @param responseBody jsonString of serie or season
     * @return
     */
    public static boolean determineIfJSONStrIsSerie(String responseBody) {
        if (responseBody.contains("number_of_seasons")) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param responseBody jsonString of serie or season
     * @return nothing
     */
    public static org.json.JSONObject parse(String responseBody) {
        Gson gson = new Gson();
        BufferedReader bufferedReader = null;
        try {
            //create a temp file
            File temp = File.createTempFile("tempfile", ".tmp");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));
            bufferedWriter.write(responseBody);
            bufferedWriter.close();
            bufferedReader = new BufferedReader(new FileReader(temp));
            if (determineIfJSONStrIsSerie(responseBody)) {
                serieTheMovieDB = gson.fromJson(bufferedReader, SerieTheMovieDB.class);
                System.out.println("Serie: " + serieTheMovieDB.getName());

            } else if (determineIfJSONStrIsSeason(responseBody)) {
                SeasonTheMovieDB seasonTheMovieDB = gson.fromJson(bufferedReader, SeasonTheMovieDB.class);
                System.out.println("Season " + seasonTheMovieDB.getName());
                seasonsList.add(seasonTheMovieDB);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Serie call() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Serie> future =  executor.submit(new SerieMerger(serieTheMovieDB, seasonsList));
        return future.get();
    }
}
