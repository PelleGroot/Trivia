package nl.pellegroot.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        triviaHelper.getQuestion(callback);

        // current user is set to the testuser because of the missing login part
        curUser = "PelGro";

        // set up database
        database = FirebaseDatabase.getInstance();
        database.setLogLevel(Logger.Level.DEBUG);
        usersRef = database.getReference("users");

        // Read from the database
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // loop through the snapshot
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    // get the userinfo and set them in a User class
                    userInfo = new User();
                    userInfo.setNickname(ds.getValue(User.class).getNickname());
                    userInfo.setHighscore(ds.getValue(User.class).getHighscore());
                    score = userInfo.getHighscore();

                    // set highscore field here, so it gets the latest highscore from the DB
                    TextView score_field = (TextView) findViewById(R.id.AG_score);
                    score_field.setText(String.format(Locale.getDefault(), "%d", score));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("On cancelled", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void gotQuestion(Question question) {
        gameQuestion = question;

        // set the fields
        TextView question_field = (TextView) findViewById(R.id.question_id);
        EditText answerfield = (EditText) findViewById(R.id.answer_field);

        // set the text in the fields
        question_field.setText(gameQuestion.getQuestion());
        answerfield.setText("");
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

        // check if the answer is correct
        if (answer.equals(gameQuestion.getCorrectAnswer().toLowerCase())) {

                // if answer is correct, update the score in the db
                score+=1;
                usersRef.child(curUser).child("highscore").setValue(score);

            // and create a new question
            triviaHelper.getQuestion(callback);

        } else {

            // if answer is not correct, create toast message
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Wrong answer, try again!", duration);
            toast.show();
        }
    }
    public void onHighscoreClick (View view){

        // start the highscore view
        Intent intent = new Intent(GameActivity.this, HighscoreActivity.class);
        startActivity(intent);
    }
    public void onSkipClick (View view){
        Button button = (Button) findViewById(R.id.next_submit);

        // create a new question
        triviaHelper.getQuestion(callback);
    }

}
