package ch.zli.peakality.model;

import java.text.SimpleDateFormat;

import ch.zli.peakality.database.entity.Score;
import ch.zli.peakality.service.ScoreCalculator;

public class ScoreModel {
    private int id;
    private String date;
    private String city;
    private String score;
    private ScoreCalculator scoreCalculator = new ScoreCalculator();

    public ScoreModel(Score score) {
        this.score = "42";
        this.city = score.getCityName();
        this.date = SimpleDateFormat.getDateInstance().format(score.getDate());
        this.id = score.getScoreId();
    }

    public String getDate() {
        return this.date;
    }

    public String getCity() {
        return this.city;
    }

    public int getId() {
        return this.id;
    }

    public String getScore() {
        return this.score;
    }
}
