package nl.pellegroot.trivia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {
    public Context context;
    public TriviaHelper triviaHelper = new TriviaHelper(this);
    public TriviaHelper.Callback callback = this;
    public long score;
    public Question gameQuestion;
    public String curUser;
    public User userInfo;

    // initiate database
    public FirebaseDatabase database;
    public DatabaseReference usersRef;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        curUser = "PelGro";

        // create test user
//        User PelGro = new User();
//        PelGro.setNickname("pellegroot");

        // set up database
        database = FirebaseDatabase.getInstance();
        database.setLogLevel(Logger.Level.DEBUG);
        usersRef = database.getReference("users");

//        final Map<String, User> users = new HashMap<>();
//        users.put(curUser, PelGro);
//        usersRef.setValue(users);

        // Read from the database
        usersRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    userInfo = new User();
                    userInfo.setNickname(ds.child(curUser).getValue(User.class).getNickname());
                    userInfo.setHighscore(ds.child(curUser).getValue(User.class).getHighscore());
                }

//                if(dataSnapshot.exists()) {
//                    score = dataSnapshot.child(curUser).getValue(long.class);
//                    Log.d("OnCreate", "onDataChange: " + dataSnapshot.getValue());
//                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("On cancelled", "Failed to read value.", error.toException());
            }
        });

        usersRef.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                score = dataSnapshot.child("highscore").getValue(long.class);
                Log.d("pp", "onChildChanged: " + dataSnapshot.child("highscore").getValue(long.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        triviaHelper.getQuestion(callback);
    }

    @Override
    public void gotQuestion(Question question) {
        gameQuestion = question;

        TextView question_field = (TextView) findViewById(R.id.question_id);
        TextView score_field = (TextView) findViewById(R.id.AG_score);
        EditText answerfield = (EditText) findViewById(R.id.answer_field);

        question_field.setText(gameQuestion.getQuestion());
        answerfield.setText("");
        score_field.setText(String.format(Locale.getDefault(), "%d", score));
    }

    @Override
    public void gotError(String error) {
        // create a toast message from the error returned
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, error, duration);
        toast.show();
    }

    public void onSubmitClick(View view) {
        EditText answerField = (EditText) findViewById(R.id.answer_field);
        String answer = answerField.getText().toString().toLowerCase();

        if (answer.equals(gameQuestion.getCorrectAnswer().toLowerCase())) {
                score+=1;
                usersRef.child(curUser).child("highscore").setValue(score);
            Log.d("score", "onClick: " + score);
            triviaHelper.getQuestion(callback);
        } else {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Wrong answer, try again!", duration);
            toast.show();
        }
    }
}
