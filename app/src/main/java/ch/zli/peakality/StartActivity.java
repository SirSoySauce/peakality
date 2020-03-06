package ch.zli.peakality;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

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
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 9556165;
    private static final String TAG = StartActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        generateScoreButton = findViewById(R.id.bGenerateScore);

        if (!checkPermissions()) {
            requestPermissions();
        }

        if (checkPermissions()) {
            generateScoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Score score = buildScore();
                    Intent intent = new Intent(StartActivity.this, ScoreActivity.class);
                    intent.putExtra(SCORE_EXTRA_NAME, score);
                    startActivity(intent);
                }
            });
        } else {
            generateScoreButton.setEnabled(false);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        // TODO: Handle case if mobile has no sensor
        sensorEventListener = pressureSensorEventListener();

        if (checkPermissions()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    private Score buildScore() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(locationOnSuccessListener());
        // TODO: Fully build Score object
        return Score.builder()
                .airPressure(pressure)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .altitude(location.getAltitude())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(locationOnSuccessListener());
        }
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

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Shows a {@link Snackbar}.
     *  @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(android.R.string.ok), listener).show();
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showSnackbar(R.string.permission_rationale,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(StartActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
