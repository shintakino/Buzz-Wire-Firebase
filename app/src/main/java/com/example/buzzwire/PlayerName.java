package com.example.buzzwire;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayerName extends AppCompatActivity {
    DatabaseReference wireStatusRef;
    EditText playerNameField;
    Button buttonEnter;
    DatabaseReference  playerDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_name);
        wireStatusRef = FirebaseDatabase.getInstance().getReference().child("wireStatus");

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(PlayerName.this, MainActivity.class);
            startActivity(intent);
        });
        playerNameField = findViewById(R.id.playerNameField);
        buttonEnter = findViewById(R.id.buttonEnter);

        playerDbRef = FirebaseDatabase.getInstance().getReference().child("Players");

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPlayerData();
                Intent intent = new Intent(PlayerName.this, preStartGame.class);
                startActivity(intent);
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
    private void insertPlayerData(){
        String playerName = playerNameField.getText().toString();
        Players players = new Players(playerName);

        playerDbRef.setValue(players);
        Toast.makeText(PlayerName.this, "Player Change", Toast.LENGTH_SHORT).show();

    }
}