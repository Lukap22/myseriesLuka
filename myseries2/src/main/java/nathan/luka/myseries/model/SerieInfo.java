package nathan.luka.myseries.model;

public class SerieInfo {
    int id;
    int amountOfSeasons;
    int option;

    public SerieInfo(int id, int amountOfSeasons, int option) {
        this.id = id;
        this.amountOfSeasons = amountOfSeasons;
        this.option = option;
    }

    public int getId() {
        return id;
    }

    public int getAmountOfSeasons() {
        return amountOfSeasons;
    }

    public int getOption() {
        return option;
    }
}
