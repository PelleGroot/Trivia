package nl.pellegroot.trivia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {
    public Context context;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TriviaHelper triviaHelper = new TriviaHelper(this);
        triviaHelper.getQuestion(this);

        // test firebase DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setLogLevel(Logger.Level.DEBUG);

        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("On Data Change", "Value is: " + value);
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
        TextView question_field = (TextView) findViewById(R.id.question_id);
        TextView difficulty_field= (TextView) findViewById(R.id.dif_id);
        TextView category_field = (TextView) findViewById(R.id.cat_id);

        question_field.setText(question.getQuestion());
        difficulty_field.setText(String.format(Locale.getDefault(),"%d", question.getDifficulty()));
        category_field.setText(String.format(Locale.getDefault(),"%d", question.getCategory_id()));
    }

    @Override
    public void gotError(String error) {
        // create a toast message from the error returned
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, error, duration);
        toast.show();
    }
}
