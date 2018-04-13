package nl.pellegroot.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HighscoreAdapter extends ArrayAdapter {

    public HighscoreAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.highscore_item, parent, false);
        }
        // code here
        TextView nameView = (TextView) convertView.findViewById(R.id.HI_name);
        TextView scoreView = (TextView) convertView.findViewById(R.id.HI_score);



        return convertView;
    }
}
