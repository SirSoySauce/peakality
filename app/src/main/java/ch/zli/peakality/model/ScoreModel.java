package ch.zli.peakality.model;

import java.util.Date;

import ch.zli.peakality.database.entity.Score;
import ch.zli.peakality.domain.bo.ScoreBO;
import ch.zli.peakality.service.ScoreCalculator;

public class ScoreModel {
    private Score score;
    private ScoreBO scoreBo;
    private int calculatedScore;
    private ScoreCalculator scoreCalculator = new ScoreCalculator();

    public ScoreModel(ScoreBO scoreBO) {
        this.scoreBo = scoreBO;
        this.score = Score.builder()
                .airPressure(scoreBO.getAirPressure())
                .altitude(scoreBO.getAltitude())
                .cityName("Hallo G")
                .date(new Date())
                .latitude(scoreBO.getLatitude())
                .longitude(scoreBO.getLongitude())
                .temperature(scoreBO.getTemperature())
                .weather(scoreBO.getWeather())
                .windSpeed(scoreBO.getWindSpeed())
                .build();;
    }

    public int getCalculatedScore() {
        return scoreCalculator.calculateScore(this.scoreBo);
    }

    public Score getScore() {
        return this.score;
    }
}
