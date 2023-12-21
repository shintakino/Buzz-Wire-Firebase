package com.example.buzzwire;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Button;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference wireStatusRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wireStatusRef = FirebaseDatabase.getInstance().getReference().child("wireStatus");

        // Play "victory.mp3" sound once
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.start();

        // Release media player resources after playback completes
        mediaPlayer.setOnCompletionListener(mp -> {
            mediaPlayer.release();

        });
        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(MainActivity.this, PlayerName.class);
            startActivity(intent);
        });

        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(MainActivity.this, TimeSettings.class);
            startActivity(intent);
        });

        Button buttonRankings = findViewById(R.id.buttonRankings);
        buttonRankings.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(MainActivity.this, Rankings.class);
            startActivity(intent);
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