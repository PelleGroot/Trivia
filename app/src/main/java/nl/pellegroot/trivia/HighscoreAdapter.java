package nl.pellegroot.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class HighscoreAdapter extends ArrayAdapter {
    public ArrayList<User> userlist;

    public HighscoreAdapter(@NonNull Context context, int resource,@NonNull ArrayList objects) {
        super(context, resource, objects);
        userlist = objects;

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

        User user = (User) userlist.get(position);

        nameView.setText(user.getNickname());
        scoreView.setText(String.format(Locale.getDefault(),"%d",user.getHighscore()));

        return convertView;
    }
}
