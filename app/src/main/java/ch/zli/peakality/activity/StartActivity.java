package ch.zli.peakality.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.model.CurrentWeather;

import ch.zli.peakality.R;
import ch.zli.peakality.database.entity.Score;
import ch.zli.peakality.domain.bo.ScoreBO;
import ch.zli.peakality.service.OpenWeatherMapService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static ch.zli.peakality.activity.ScoreActivity.SCORE_EXTRA_NAME;

public class StartActivity extends Activity {

    // Button used to share the score.
    Button generateScoreButton;
    private FusedLocationProviderClient fusedLocationClient;
    private OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService();
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private SensorEventListener sensorEventListener;
    private float pressure;
    private Location location;
    private CurrentWeather currentWeather;
    // Set a code to identify the app.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 9556165;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        generateScoreButton = findViewById(R.id.bGenerateScore);

        // Request the location permission if they aren't already set.
        if (!checkPermissions()) {
            requestPermissions();
        }

        if (checkPermissions()) {
            generateScoreButton.setOnClickListener(v -> {
                ScoreBO score = buildScore();
                Intent intent = new Intent(StartActivity.this, ScoreActivity.class);
                intent.putExtra(SCORE_EXTRA_NAME, score);
                startActivity(intent);
            });
        } else {
            generateScoreButton.setEnabled(false);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        // TODO: Handle case if mobile has no sensor
        sensorEventListener = pressureSensorEventListener();

        // Check if the app is allowed to access the location.
        if (checkPermissions()) {
            // Get the location client.
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    /**
     * Builds a score object.
     *
     * @return
     *  Score object.
     */
    private ScoreBO buildScore() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(locationOnSuccessListener());
        // TODO: Fully build Score object
        return ScoreBO.builder()
                .airPressure(pressure)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .altitude(location.getAltitude())
                .windSpeed(currentWeather.getWindData().getSpeed())
                .temperature(currentWeather.getMainData().getTemp())
                .weather(currentWeather.getWeatherList().get(0).getMainInfo())
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
        // Unregister the sensor event listener while the app is in the background in order
        // to save battery.
        sensorManager.unregisterListener(sensorEventListener);
    }

    /**
     * Sensor event listener.
     *
     * @return
     *  Event listener to handle events.
     */
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

    /**
     * On success listener.
     *
     * @return
     *  The on success listener to set device location.
     */
    private OnSuccessListener<Location> locationOnSuccessListener() {
        return deviceLocation -> {
            location = deviceLocation;
            if (location != null && currentWeather == null) {
                try {
                    compositeDisposable.add(openWeatherMapService.getCurrentWeather(location.getLatitude(), location.getLongitude())
                            .subscribeOn(Schedulers.io())
                            .subscribe(weatherData -> currentWeather = weatherData, error -> showSnackbar(R.string.error_weather_api)));
                } catch (APIException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                    showSnackbar(R.string.error_weather_api);
                }
            }
        };
    }

    /**
     * Checks if the application has the necessary permissions.
     *
     * @return
     *  Indicates if the app has the necessary permissions.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Shows a snack bar to notify the user that the application is missing the location permission.
     *
     * @param mainTextStringId
     *  The id for the string resource for the Snackbar text.
     * @param listener
     *  The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, View.OnClickListener listener) {
        // Create a snack bar.
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(android.R.string.ok), listener).show();
    }

    private void showSnackbar(final int mainTextStringId) {
        showSnackbar(mainTextStringId, null);
    }

    // Request the permission to access the location.
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
