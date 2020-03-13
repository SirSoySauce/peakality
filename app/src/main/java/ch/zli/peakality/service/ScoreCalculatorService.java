package ch.zli.peakality.service;

import java.util.HashMap;
import java.util.Map;

import ch.zli.peakality.domain.bo.ScoreBO;

public class ScoreCalculatorService {
    // Arrays containing the max, the norm and the min value, in that order.
    private static final int[] TEMPERATURE_VALUES = {40, 18, -20};
    private static final int[] ALTITUDE_VALES = {3000, 400, 0};
    private static final int[] AIR_PRESSURE = {1100, 900, 700};

    private final HashMap<String, Integer> weatherValueMap;

    public ScoreCalculatorService() {
        weatherValueMap = initializeWeatherMap();
    }

    private HashMap<String, Integer> initializeWeatherMap() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("^2\\d\\d$", 800); // Thunderstorm
        hashMap.put("^3\\d\\d$", 400); // Drizzle
        hashMap.put("^5\\d\\d$", 500); // Rain
        hashMap.put("^6\\d\\d$", 600); // Snow
        hashMap.put("^800$", 200); // Clear Sky
        hashMap.put("^80[1234]$", 300); // Cloudy Sky
        hashMap.put("^7\\d\\d$", 650); // Atmosphere
        return hashMap;
    }


    /**
     * Calculates the score.
     *
     * @param score
     *  The score object to use for the calculation.
     * @return
     *  The calculated score.
     */
    public int calculateScore(ScoreBO score) {
        int scoreValue = 0;
        // Add together all the single scores.
        scoreValue += calculateSingleScoreValue(score.getAltitude(), ALTITUDE_VALES);
        scoreValue += calculateSingleScoreValue(score.getTemperature(), TEMPERATURE_VALUES);
        scoreValue += calculateSingleScoreValue(score.getAirPressure(), AIR_PRESSURE);
        scoreValue += calculateWeatherScoreValue(Integer.toString(score.getWeatherId()));
        return scoreValue;
    }

    /**
     * Generate the score points of a single value.
     *
     * @param actualValue
     *  The actual value of the user.
     * @param referenceValues
     *  An array of 3 values which defines the max, norm and min values to calculate the score.
     * @return
     *  The calculated score.
     */
    private int calculateSingleScoreValue(double actualValue, int[] referenceValues) {
        double score;
        double maxValue = referenceValues[0];
        double normValue = referenceValues[1];
        double minValue = referenceValues[2];
        // Check if the min or max value has to be used fot the calculation.
        if (actualValue >= referenceValues[1]) {
            score = (actualValue - normValue) / (maxValue - normValue);
        } else {
            score = (normValue - actualValue) / (normValue - minValue);
        }
        return (int)(score * 1000);
    }

    private int calculateWeatherScoreValue(String weatherId) {
        for (Map.Entry<String, Integer> entry : weatherValueMap.entrySet()) {
            if(weatherId.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 0;
    }
}
