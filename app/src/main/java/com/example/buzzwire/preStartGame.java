package com.example.buzzwire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class preStartGame extends AppCompatActivity {
    DatabaseReference wireStatusRef;
    Button buttonGameStart;
    TextView setCountdown;
    DatabaseReference countdownRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_start_game);
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(preStartGame.this, MainActivity.class);
            startActivity(intent);
        });
        buttonGameStart = findViewById(R.id.buttonGameStart);

        buttonGameStart.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(preStartGame.this, gameOnStart.class);
            startActivity(intent);
        });

        setCountdown = findViewById(R.id.setCountdown);

        countdownRef = FirebaseDatabase.getInstance().getReference().child("setCountdown").child("timesetdata");
        wireStatusRef = FirebaseDatabase.getInstance().getReference().child("wireStatus");
        countdownRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get the value from the dataSnapshot and set it to the TextView
                    String countdownValue = snapshot.getValue(String.class);
                    setCountdown.setText(countdownValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(preStartGame.this, "Cant Connect to Database", Toast.LENGTH_SHORT).show();
            }
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