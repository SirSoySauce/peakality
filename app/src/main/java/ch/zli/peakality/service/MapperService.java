package ch.zli.peakality.service;

import net.aksingh.owmjapis.model.CurrentWeather;

import ch.zli.peakality.database.entity.Score;
import ch.zli.peakality.domain.bo.ScoreBO;

public class MapperService {

    public static ScoreBO mapScoreToScoreBO(Score score) {
       return ScoreBO.builder()
                .airPressure(score.getAirPressure())
                .latitude(score.getLatitude())
                .longitude(score.getLongitude())
                .altitude(score.getAltitude())
                .date(score.getDate())
                .windSpeed(score.getWindSpeed())
                .temperature(score.getTemperature())
                .weather(score.getWeather())
                .weatherId(score.getWeatherId())
                .build();
    }

    public static Score mapScoreBOtoScore(ScoreBO scoreBO, CurrentWeather currentWeather) {
        return Score.builder()
                .airPressure(scoreBO.getAirPressure())
                .altitude(scoreBO.getAltitude())
                .cityName(currentWeather.getCityName())
                .date(scoreBO.getDate())
                .latitude(scoreBO.getLatitude())
                .longitude(scoreBO.getLongitude())
                .temperature(scoreBO.getTemperature())
                .weather(scoreBO.getWeather())
                .weatherId(scoreBO.getWeatherId())
                .windSpeed(scoreBO.getWindSpeed())
                .build();
    }
}
