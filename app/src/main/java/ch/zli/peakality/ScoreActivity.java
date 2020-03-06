package ch.zli.peakality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScoreActivity extends Activity {

    FloatingActionButton shareScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        shareScoreButton = findViewById(R.id.fabShareScore);

        shareScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                // @TODO: Set actual score.
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My score is: PLACE_HOLDER_SCORE");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
    }
}
