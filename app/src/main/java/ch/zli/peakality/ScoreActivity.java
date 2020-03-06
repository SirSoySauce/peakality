package ch.zli.peakality;

import android.app.Activity;
import android.os.Bundle;

import ch.zli.peakality.database.entity.Score;

public class ScoreActivity extends Activity {

    private Score score;

    public static final String SCORE_EXTRA_NAME = "score_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = (Score) getIntent().getSerializableExtra(SCORE_EXTRA_NAME);
    }
}
