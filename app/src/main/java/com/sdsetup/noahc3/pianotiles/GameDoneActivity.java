package com.sdsetup.noahc3.pianotiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GameDoneActivity extends AppCompatActivity {

    //stuff run when the activity is generated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_done);

        //Grab the score and display it.
        ((TextView)findViewById(R.id.txtScoreFinal)).setText("Final Score: " + getIntent().getIntExtra("SCORE", 0));
    }

    //When restart is pressed, start the main activity again and exit this one.
    public void btnRestart_Clicked(View v) {
        startActivity(new Intent(GameDoneActivity.this, MainActivity.class));
        finish();
    }


}
