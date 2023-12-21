package com.example.buzzwire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;

public class gameOnStart extends AppCompatActivity {
    DatabaseReference playerRef;
    DatabaseReference wireStatus;
    DatabaseReference playerFinishRef;
    DatabaseReference finishTimeRef;
    DatabaseReference countdownRef;
    Button buttonReset;
    Button buttonFinish;
    TextView setCountdown;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_on_start);
        setCountdown = findViewById(R.id.setCountdown);
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonReset = findViewById(R.id.buttonReset);
        countdownRef = FirebaseDatabase.getInstance().getReference().child("setCountdownInteger").child("timesetdatainteger");
        playerRef = FirebaseDatabase.getInstance().getReference().child("Players");
        finishTimeRef = FirebaseDatabase.getInstance().getReference().child("rankings").child("timeFinish");
        playerFinishRef = FirebaseDatabase.getInstance().getReference().child("rankings").child("playerName");
        wireStatus = FirebaseDatabase.getInstance().getReference().child("wireStatus");

        wireStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the value inside "wireStatus" is true
                Boolean isWireStatusTrue = dataSnapshot.getValue(Boolean.class);

                if (isWireStatusTrue != null && isWireStatusTrue) {
                    Intent intent = new Intent(gameOnStart.this, wireTouch.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(gameOnStart.this, "Cant Connect to Database", Toast.LENGTH_SHORT).show();
            }
        });

        countdownRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Integer countdownValue = snapshot.getValue(Integer.class);
                    if (countdownValue != null) {

                        long intTimeValue = countdownValue * 1000; // Convert seconds to milliseconds

                        timer = new CountDownTimer(intTimeValue, 1000) {
                            public void onTick(long millisUntilFinished) {
                                long minutes = millisUntilFinished / 60000;
                                long seconds = (millisUntilFinished % 60000) / 1000;
                                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                                setCountdown.setText(timeLeftFormatted);
                            }

                            public void onFinish() {
                                // Handle when the countdown finishes if needed
                                setCountdown.setText("00:00"); // Display 00:00 at the end
                            }
                        };

                        timer.start();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(gameOnStart.this, "Cant Connect to Database", Toast.LENGTH_SHORT).show();
            }
        });

        buttonFinish.setOnClickListener(v -> {

            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot nameSnapshot : dataSnapshot.getChildren()){
                            String playerData = nameSnapshot.getValue(String.class);
                            sendTimeRanking(playerData);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(gameOnStart.this, "Cant Connect to Database", Toast.LENGTH_SHORT).show();
                }
            });

        });

        buttonReset.setOnClickListener(v -> {
            Intent intent = new Intent(gameOnStart.this, preStartGame.class);
            startActivity(intent);
        });


    }
    private void sendTimeRanking(String playerNameStored) {
        if (timer != null) {
            timer.cancel(); // Pause the timer if it's running

            String currentTime = setCountdown.getText().toString();
            String[] timeComponents = currentTime.split(":");
            long pauseTimeSeconds = Long.parseLong(timeComponents[0]) * 60 + Long.parseLong(timeComponents[1]);
            String pauseTimeString = String.valueOf(pauseTimeSeconds);
            // Push the new values to the database under a unique key
            DatabaseReference newEntryRef = finishTimeRef.push();
            newEntryRef.child("time").setValue(pauseTimeSeconds);
            newEntryRef.child("playerName").setValue(playerNameStored);

            Intent intent = new Intent(gameOnStart.this, finishMaze.class);
            intent.putExtra("keyname", pauseTimeString);
            startActivity(intent);
        }
    }



}