package nathan.luka.myseries.model.gjson;

import nathan.luka.myseries.model.Serie;

import java.util.List;

public class BackupSeries {
    List<Serie> backSeries;

    public BackupSeries(List<Serie> backSeries) {
        this.backSeries = backSeries;
    }

    public List<Serie> getBackSeries() {
        return backSeries;
    }

    public void setBackSeries(List<Serie> backSeries) {
        this.backSeries = backSeries;
    }
}
