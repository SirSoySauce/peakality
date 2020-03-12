package ch.zli.peakality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ch.zli.peakality.R;
import ch.zli.peakality.model.ScoreModel;

public class ScoreAdapter extends ArrayAdapter<ScoreModel>{

    private ArrayList<ScoreModel> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView dateView;
        TextView cityView;
        TextView scoreValueView;
    }

    public ScoreAdapter(ArrayList<ScoreModel> data, Context context) {
        super(context, R.layout.list_score, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ScoreModel score = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_score, parent, false);
            viewHolder.dateView = convertView.findViewById(R.id.tvScoreDate);
            viewHolder.cityView = convertView.findViewById(R.id.tvCity);
            viewHolder.scoreValueView = convertView.findViewById(R.id.tvScore);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.dateView.setText(score.getScore().getDate().toString());
        viewHolder.cityView.setText(score.getScore().getCityName());
        viewHolder.scoreValueView.setText(String.valueOf(score.getCalculatedScore()));
        // Return the completed view to render on screen
        return convertView;
    }
}
