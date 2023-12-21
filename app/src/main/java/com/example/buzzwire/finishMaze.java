package com.example.buzzwire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class finishMaze extends AppCompatActivity {
    DatabaseReference wireStatusRef;

    DatabaseReference finishTimeRef;
    Button buttonResetGame;
    Button buttonSwitchPlayer;
    Button buttonRankings ;

    TextView numSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_maze);
        numSeconds = findViewById(R.id.numSeconds);
        buttonResetGame = findViewById(R.id.buttonResetGame);
        buttonSwitchPlayer = findViewById(R.id.buttonSwitchPlayer);
        buttonRankings = findViewById(R.id.buttonRankings2);
        finishTimeRef = FirebaseDatabase.getInstance().getReference().child("rankings").child("timeFinish");
        wireStatusRef = FirebaseDatabase.getInstance().getReference().child("wireStatus");

        String numSeconds1 = getIntent().getStringExtra("keyname");
        numSeconds.setText(numSeconds1);

        buttonResetGame.setOnClickListener(v -> {
            Intent intent = new Intent(finishMaze.this, preStartGame.class);
            startActivity(intent);
        });

        buttonSwitchPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(finishMaze.this, PlayerName.class);
            startActivity(intent);
        });
        buttonRankings.setOnClickListener(v -> {
            Intent intent = new Intent(finishMaze.this, Rankings.class);
            startActivity(intent);
        });

        // Play "victory.mp3" sound once
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.victory);
        mediaPlayer.start();

        // Release media player resources after playback completes
        mediaPlayer.setOnCompletionListener(mp -> {
            mediaPlayer.release();
        });

        wireStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean wireStatus = dataSnapshot.getValue(Boolean.class);

                    if (wireStatus) {
                        wireStatusRef.setValue(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors with the database operation
            }
        });
    }
}