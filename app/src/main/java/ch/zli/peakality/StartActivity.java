package ch.zli.peakality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.zli.peakality.database.entity.Score;

import static ch.zli.peakality.ScoreActivity.SCORE_EXTRA_NAME;

public class StartActivity extends Activity {

    Button generateScoreButton;
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private SensorEventListener sensorEventListener;
    private float pressure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        generateScoreButton = findViewById(R.id.bGenerateScore);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        // TODO: Handle case if mobile has no sensor
        sensorEventListener = pressureSensorEventListener();

        generateScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Score score = buildScore();
                Intent intent = new Intent(StartActivity.this, ScoreActivity.class);
                intent.putExtra(SCORE_EXTRA_NAME, score);
                startActivity(intent);
            }
        });
    }

    private Score buildScore() {
        // TODO: Fully build Score object
        return Score.builder()
                .airPressure(pressure)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }


    private SensorEventListener pressureSensorEventListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                pressure = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };
    }
}
