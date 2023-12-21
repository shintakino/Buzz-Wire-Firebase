package com.example.buzzwire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

public class wireTouch extends AppCompatActivity {
    Button buttonSwitchPlayer;
    Button buttonResetGame;
    Button buttonRankings;
    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wire_touch);
        buttonSwitchPlayer = findViewById(R.id.buttonSwitchPlayer1);
        buttonResetGame = findViewById(R.id.buttonResetGame1);
        buttonRankings = findViewById(R.id.buttonRankings3);

        buttonResetGame.setOnClickListener(v -> {
            Intent intent = new Intent(wireTouch.this, preStartGame.class);
            startActivity(intent);
        });

        buttonSwitchPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(wireTouch.this, PlayerName.class);
            startActivity(intent);
        });
        buttonRankings.setOnClickListener(v -> {
            Intent intent = new Intent(wireTouch.this, Rankings.class);
            startActivity(intent);
        });


        // Play "buzz.mp3" once
        mediaPlayer1 = MediaPlayer.create(this, R.raw.buzz);
        mediaPlayer1.setOnCompletionListener(mp -> {
            mediaPlayer1.release();
            // After "buzz.mp3" finishes, play "disappoint.mp3" once
            mediaPlayer2 = MediaPlayer.create(wireTouch.this, R.raw.disappoint);
            mediaPlayer2.start();
            mediaPlayer2.setOnCompletionListener(mp2 -> {
                mediaPlayer2.release();
            });
        });
        mediaPlayer1.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (mediaPlayer1 != null) {
            mediaPlayer1.release();
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
        }

    }
}