package ch.zli.peakality.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.zli.peakality.R;
import ch.zli.peakality.adapter.ScoreAdapter;
import ch.zli.peakality.database.AppDatabase;
import ch.zli.peakality.database.entity.Score;
import ch.zli.peakality.domain.bo.ScoreBO;
import ch.zli.peakality.model.ScoreModel;
import ch.zli.peakality.service.DatabaseService;
import ch.zli.peakality.service.ScoreCalculatorService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ScoreListActivity extends AppCompatActivity {

    private DatabaseService databaseService;
    private ScoreAdapter scoreAdapter;
    private ScoreCalculatorService scoreCalculator = new ScoreCalculatorService();
    private List<ScoreModel> scoreModels = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);

        ListView scoreList = findViewById(R.id.lvScores);
        databaseService = new DatabaseService(Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "peakalityDb").build());
        compositeDisposable.add(databaseService.getAllScoresFromDatabase()
                .subscribeOn(Schedulers.io())
                .subscribe(scores -> {
                    scoreModels = scores.stream()
                            .map(score -> ScoreModel.builder()
                                    .city(score.getCityName())
                                    .date(score.getDate())
                                    .score(scoreCalculator.calculateScore(mapToScoreBO(score)))
                                    .id(score.getScoreId())
                                    .build())
                            .collect(Collectors.toList());
                    scoreAdapter.addAll(scoreModels);
                }));
        scoreAdapter = new ScoreAdapter(scoreModels, this);
        scoreList.setAdapter(scoreAdapter);
    }

    private ScoreBO mapToScoreBO(Score score) {
        return ScoreBO.builder()
                .date(score.getDate())
                .airPressure(score.getAirPressure())
                .altitude(score.getAltitude())
                .latitude(score.getLatitude())
                .longitude(score.getLongitude())
                .temperature(score.getTemperature())
                .weather(score.getWeather())
                .weatherId(score.getWeatherId())
                .windSpeed(score.getWindSpeed())
                .build();
    }
}
