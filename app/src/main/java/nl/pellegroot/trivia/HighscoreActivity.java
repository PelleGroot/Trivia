package nl.pellegroot.trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HighscoreActivity extends AppCompatActivity {

    public ArrayList<User> userlist = new ArrayList<>();
    public HighscoreAdapter highscoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // initiate and set up the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Read from the database
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // loop through the snapshot
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    // get the userinfo and set them in a User class
                    User userInfo = new User();
                    userInfo.setNickname(ds.getValue(User.class).getNickname());
                    userInfo.setHighscore(ds.getValue(User.class).getHighscore());
                    userlist.add(userInfo);
                }

                // Set the highscore adapter here so it does not load before the data is retrieved
                highscoreAdapter = new HighscoreAdapter(HighscoreActivity.this, R.layout.highscore_item, userlist);
                ListView scoreboard = (ListView) findViewById(R.id.HS_LV);
                scoreboard.setAdapter(highscoreAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("On cancelled", "Failed to read value.", error.toException());
                //TODO: Let the user know something went wrong
            }
        });
    }
}
