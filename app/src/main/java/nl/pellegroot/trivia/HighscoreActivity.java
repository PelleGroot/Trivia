package nl.pellegroot.trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        HighscoreAdapter highscoreAdapter = new HighscoreAdapter(this, R.layout.highscore_item);

    }
}
