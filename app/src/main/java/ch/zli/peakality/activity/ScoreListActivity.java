package ch.zli.peakality.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ch.zli.peakality.R;
import ch.zli.peakality.adapter.ScoreAdapter;
import ch.zli.peakality.model.ScoreModel;

public class ScoreListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);

        ListView scoreList = findViewById(R.id.lvScores);
        ArrayList<ScoreModel> listItems = new ArrayList<>();
        final ScoreAdapter adapter = new ScoreAdapter(listItems, this);
        scoreList.setAdapter(adapter);
    }
}
