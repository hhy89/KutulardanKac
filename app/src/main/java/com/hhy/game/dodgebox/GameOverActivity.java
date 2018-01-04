package com.hhy.game.dodgebox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_gameover);

        // get score and record value
        int score = getIntent().getIntExtra("score", 0);
        int record = getIntent().getIntExtra("record", 0);

        // gameover or highscore image
        ImageView gameOver = findViewById(R.id.gameOver);
        if (score > record) gameOver.setImageResource(R.drawable.highscore);
        else gameOver.setImageResource(R.drawable.gameover);

        // score
        TextView recordText = findViewById(R.id.recordText);
        recordText.setText(String.valueOf(score));

        // play game button
        ImageButton playGame = findViewById(R.id.playGame);
        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // for keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    // on back button pressed
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
