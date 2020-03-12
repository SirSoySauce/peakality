package ch.zli.peakality.service;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class OpenWeatherMapService {

    private static final String API_KEY = "a6a2b91107c97e9756707648de1c54c2";
    private final OWM owm;

    public OpenWeatherMapService() {
        OWM owm = new OWM(API_KEY);
        owm.setUnit(OWM.Unit.METRIC);
        this.owm = owm;
    }

    public Observable<CurrentWeather> getCurrentWeather(double latitude, double longitude) throws APIException {
        Callable<CurrentWeather> callable = () -> owm.currentWeatherByCoords(latitude, longitude);
        return Observable.fromCallable(callable);
    }
}
