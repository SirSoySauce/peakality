package ch.zli.peakality.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;
import java.util.stream.Collectors;

import ch.zli.peakality.R;
import ch.zli.peakality.adapter.ScoreAdapter;
import ch.zli.peakality.database.AppDatabase;
import ch.zli.peakality.model.ScoreModel;
import ch.zli.peakality.service.DatabaseService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ScoreListActivity extends AppCompatActivity {

    private DatabaseService databaseService;
    private List<ScoreModel> scoreModels;

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
                            .map(ScoreModel::new)
                            .collect(Collectors.toList());
                    ScoreAdapter adapter = new ScoreAdapter(scoreModels, this);
                    scoreList.setAdapter(adapter);
                }));
    }
}
