package nathan.luka.myseries.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private static int lastId;
    private int rating;
    private String comment;
    private LocalDate dateAdded;
    private User user;
    private  int id;
    private String date;

    //https://stackoverflow.com/questions/57752600/how-to-convert-time-format-to-todays-time-in-date-format-in-java
    public Review(String comment, User user) {
        this.id = lastId;
        lastId++;
        this.comment = comment;
        this.user = user;
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh.mm");
        String newDateTimeFormatter = ldt.format(dtf);
        this.date = newDateTimeFormatter;
    }


    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
