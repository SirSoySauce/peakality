package ch.zli.peakality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.zli.peakality.R;
import ch.zli.peakality.model.ScoreModel;
import lombok.Setter;

public class ScoreAdapter extends ArrayAdapter<ScoreModel>{

    public ScoreAdapter(List<ScoreModel> data, Context context) {
        super(context, R.layout.list_score, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScoreModel scoreModel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_score, parent, false);
        }

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
