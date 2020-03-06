package ch.zli.peakality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import ch.zli.peakality.database.entity.Score;

import static ch.zli.peakality.ScoreActivity.SCORE_EXTRA_NAME;

public class StartActivity extends Activity {

    Button generateScoreButton;
    private FusedLocationProviderClient fusedLocationClient;
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private SensorEventListener sensorEventListener;
    private float pressure;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        generateScoreButton = findViewById(R.id.bGenerateScore);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        // TODO: Handle case if mobile has no sensor
        sensorEventListener = pressureSensorEventListener();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


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
        fusedLocationClient.getLastLocation().addOnSuccessListener(locationOnSuccessListener());
        // TODO: Fully build Score object
        return Score.builder()
                .airPressure(pressure)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
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
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    private OnSuccessListener<Location> locationOnSuccessListener() {
        return new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location deviceLocation) {
                location = deviceLocation;
                if (location != null) {
                    // TODO: Handle null location
                }
            }
        };
    }

}
