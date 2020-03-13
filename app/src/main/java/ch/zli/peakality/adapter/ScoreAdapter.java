package ch.zli.peakality.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.zli.peakality.R;
import ch.zli.peakality.activity.ScoreActivity;
import ch.zli.peakality.activity.StartActivity;
import ch.zli.peakality.database.AppDatabase;
import ch.zli.peakality.domain.bo.ScoreBO;
import ch.zli.peakality.model.ScoreModel;
import ch.zli.peakality.service.DatabaseService;
import ch.zli.peakality.service.MapperService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Setter;

import static ch.zli.peakality.activity.ScoreActivity.SCORE_EXTRA_NAME;

public class ScoreAdapter extends ArrayAdapter<ScoreModel> {

    @Setter
    private List<ScoreModel> dataSet;
    private Context mContext;
    private DatabaseService databaseService;

    public ScoreAdapter(List<ScoreModel> data, Context context) {
        super(context, R.layout.list_score, data);
        this.dataSet = data;
        this.mContext = context;

        this.databaseService = new DatabaseService(Room.databaseBuilder(mContext.getApplicationContext(),
                AppDatabase.class, "peakalityDb").build());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScoreModel scoreModel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_score, parent, false);
        }

        convertView.setOnClickListener(view -> {
            Disposable disposable = databaseService.getScoreFromDatabase(scoreModel.getId())
                    .map(MapperService::mapScoreToScoreBO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(score -> {
                        Intent intent = new Intent(mContext, ScoreActivity.class);
                        intent.putExtra(SCORE_EXTRA_NAME, score);
                        mContext.startActivity(intent);
                    });
        });

        TextView dateView = convertView.findViewById(R.id.tvScoreDate);
        TextView cityView = convertView.findViewById(R.id.tvCity);
        TextView scoreView = convertView.findViewById(R.id.tvScore);

        dateView.setText(SimpleDateFormat.getDateInstance().format(scoreModel.getDate()));
        cityView.setText(scoreModel.getCity());
        scoreView.setText(scoreModel.getScore() + "");

        return convertView;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
