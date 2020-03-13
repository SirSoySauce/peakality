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

public class ScoreAdapter extends ArrayAdapter<ScoreModel>{

    private List<ScoreModel> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView dateView;
        TextView cityView;
        TextView scoreValueView;
    }

    public ScoreAdapter(List<ScoreModel> data, Context context) {
        super(context, R.layout.list_score, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ScoreModel score = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_score, parent, false);
            viewHolder.dateView = convertView.findViewById(R.id.tvScoreDate);
            viewHolder.cityView = convertView.findViewById(R.id.tvCity);
            viewHolder.scoreValueView = convertView.findViewById(R.id.tvScore);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        lastPosition = position;

        viewHolder.dateView.setText(score.getDate());
        viewHolder.cityView.setText(score.getCity());
        viewHolder.scoreValueView.setText(score.getScore());
        viewHolder.
        // Return the completed view to render on screen
        return convertView;
    }
}
