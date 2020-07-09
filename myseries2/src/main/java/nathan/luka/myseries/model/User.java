package nathan.luka.myseries.model;

import nathan.luka.myseries.dataprovider.DataProvider;
import nathan.luka.myseries.model.usermetadata.UserSerieMetaData;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

public class User implements Callable<User> {
    public static int lastId = 1;

    private int id;
    private String email;


    private String password;

    private String userName;

    private Set<Serie> serie;

    private final List<UserSerieMetaData> userSerieMetaDataList;

    private Set<Review> reviews;

    private boolean loggedIn;


    public User(String password, String userName) {
        this.userSerieMetaDataList = new ArrayList<>();
        this.password = password;
        this.userName = userName;
        this.id = lastId;
        lastId++;


    }

    public void addSerie(Serie serie) {
        userSerieMetaDataList.add(new UserSerieMetaData(serie));
    }

    public void setEpisodeWatched(Integer themoviedbSerieID, Integer seasonNumber, Integer episodeNumber) {
        if (userSerieMetaDataList.size() != 0) {
            for (int i = 0; i < userSerieMetaDataList.size(); i++) {
                if (userSerieMetaDataList.get(i).getThemoviedbSerieID().equals(themoviedbSerieID)) {
                    //match
                    for (int j = 0; j < userSerieMetaDataList.get(i).getUserSeasonMetaDataList().size(); j++) {
                        if (Objects.equals(seasonNumber, userSerieMetaDataList.get(i).getUserSeasonMetaDataList().get(j).getSeasonNumber())) {
                            //season match
                            for (int k = 0; k < userSerieMetaDataList.get(i).getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().size(); k++) {
                                if (Objects.equals(episodeNumber, userSerieMetaDataList.get(i).getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().get(k).getEpisodeNumber())) {
                                    //Episode match
                                    if (userSerieMetaDataList.get(i).getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().get(k).isWatched()) {
                                        userSerieMetaDataList.get(i).getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().get(k).setWatched(false);
                                        break;
                                    } else {
                                        userSerieMetaDataList.get(i).getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().get(k).setWatched(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean getEpisodeWatched(Integer themoviedbSerieID, Integer seasonNumber, Integer episodeNumber) {
        //If the userSerieMetaDataList is empty add all series
        if (userSerieMetaDataList.isEmpty()) {
            List<Serie> serieList = DataProvider.getDataProvider().getSeries();
            for (int i = 0; i < serieList.size(); i++) {
                userSerieMetaDataList.add(new UserSerieMetaData(serieList.get(i)));
            }
            for (UserSerieMetaData userSerieMetaData : userSerieMetaDataList) {
                System.out.println(userSerieMetaData.getThemoviedbSerieID());
            }
        }
        for (UserSerieMetaData userSerieMetaData : userSerieMetaDataList) {
            if (userSerieMetaData.getThemoviedbSerieID().equals(themoviedbSerieID)) {
                //match
                for (int j = 0; j < userSerieMetaData.getUserSeasonMetaDataList().size(); j++) {
                    if (Objects.equals(seasonNumber, userSerieMetaData.getUserSeasonMetaDataList().get(j).getSeasonNumber())) {
                        //season match
                        for (int k = 0; k < userSerieMetaData.getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().size(); k++) {
                            if (Objects.equals(episodeNumber, userSerieMetaData.getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().get(k).getEpisodeNumber())) {
                                //Episode match
                                return userSerieMetaData.getUserSeasonMetaDataList().get(j).getUserEpisodeMetaDataList().get(k).isWatched();
                            }
                        }
                    }
                }
            }
        }
        //If the episode is not found in userSerieMetaDataList return false
        return false;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Serie> getSerie() {
        return serie;
    }

    public void addToSetSerie(String serieName, User user) {
        Serie serie = new Serie(serieName, user);
        this.serie.add(serie);
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public int getId() {
        return id;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getLastId() {
        return lastId;
    }

    public static void setLastId(int lastId) {
        User.lastId = lastId;
    }

    public void setSerie(Set<Serie> serie) {
        this.serie = serie;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        return userName + email;
    }

    @Override
    public User call() throws Exception {
        return this;
    }
}
