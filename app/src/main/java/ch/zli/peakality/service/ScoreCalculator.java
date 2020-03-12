package ch.zli.peakality.service;

import ch.zli.peakality.database.entity.Score;

public class ScoreCalculator {
    // Arrays containing the max, the norm and the min value, in that order.
    private static final int[] TEMPERATURE_VALUES = {40, 18, -20};
    private static final int[] ALTITUDE_VALES = {3000, 400, 0};
    private static final int[] AIR_PRESSURE = {1100, 900, 700};
    private static final int[] WIND_SPEED= {120, 5, 0};

    public int calculateScore(Score score) {
        int scoreValue = 0;
        scoreValue += calculateSingleScoreValue(score.getAltitude(), ALTITUDE_VALES);
        scoreValue += calculateSingleScoreValue(score.getTemperature(), TEMPERATURE_VALUES);
        scoreValue += calculateSingleScoreValue(score.getAirPressure(), AIR_PRESSURE);
        return scoreValue;
    }

    private int calculateSingleScoreValue(double actualValue, int[] referenceValues) {
        double score;
        double maxValue = referenceValues[0];
        double normValue = referenceValues[1];
        double minValue = referenceValues[2];
        if (actualValue >= referenceValues[1]) {
            score = (actualValue - normValue) / (maxValue - normValue);
        } else {
            score = (normValue - actualValue) / (normValue - minValue);
        }
        return (int)(score * 1000);
    }
}
