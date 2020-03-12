package ch.zli.peakality.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ch.zli.peakality.R;
import ch.zli.peakality.database.entity.Score;
import ch.zli.peakality.service.ScoreCalculator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScoreActivity extends Activity {

    private ScoreCalculator scoreCalculator = new ScoreCalculator();

    private Score score;

    private int calculatedScore = 0;

    public static final String SCORE_EXTRA_NAME = "score_extra";

    private static DecimalFormat decimalFormat = new DecimalFormat("#.####");

    FloatingActionButton shareScoreButton;

    TextView latitudeView;

    TextView longitudeView;

    TextView temperatureView;

    TextView pressureView;

    TextView altitudeView;

    TextView dateView;

    TextView weatherView;

    TextView scoreView;

    TextView windSpeedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = (Score) getIntent().getSerializableExtra(SCORE_EXTRA_NAME);

        // Get elements from resource.
        shareScoreButton = findViewById(R.id.fabShareScore);
        latitudeView = findViewById(R.id.tvLatitudeValue);
        longitudeView = findViewById(R.id.tvLongitudeValue);
        temperatureView = findViewById(R.id.tvTemperatureValue);
        pressureView = findViewById(R.id.tvPressureValue);
        altitudeView = findViewById(R.id.tvAltitudeValue);
        dateView = findViewById(R.id.tvDateValue);
        weatherView = findViewById(R.id.tvWeatherValue);
        scoreView = findViewById(R.id.tvScoreValue);
        windSpeedView = findViewById(R.id.tvWindSpeedValue);

        shareScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                // @TODO: Set actual score.
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My score is: " + calculatedScore);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        updateMeasuredValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMeasuredValues();
    }

    private void recalculateScore() {
        calculatedScore = scoreCalculator.calculateScore(score);
        scoreView.setText(String.valueOf(calculatedScore));
    }

    private void updateMeasuredValues() {
        String currentDate = SimpleDateFormat.getDateInstance().format(new Date());

        pressureView.setText(getString(R.string.pressure_value, score.getAirPressure()));
        longitudeView.setText(getString(R.string.longitude_value, decimalFormat.format(score.getLongitude())));
        latitudeView.setText(getString(R.string.latitude_value, decimalFormat.format(score.getLatitude())));
        altitudeView.setText(getString(R.string.altitude_value, decimalFormat.format(score.getAltitude())));
        temperatureView.setText(getString(R.string.temperature_value, decimalFormat.format(score.getTemperature())));
        weatherView.setText(score.getWeather());
        windSpeedView.setText(getString(R.string.wind_speed_value, decimalFormat.format(score.getWindSpeed())));
        dateView.setText(currentDate);
        recalculateScore();
    }
}
