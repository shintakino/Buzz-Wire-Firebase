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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Rankings extends AppCompatActivity {
    DatabaseReference finishTimeRef;
    DatabaseReference wireStatusRef;
    Button buttonBack;
    TextView txtplayerRank1;
    TextView txtplayerRank2;
    TextView txtplayerRank3;
    TextView txtplayerRank4;
    TextView txtplayerRank5;
    TextView txtplayerTime1;
    TextView txtplayerTime2;
    TextView txtplayerTime3;
    TextView txtplayerTime4;
    TextView txtplayerTime5;
    Button buttonReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        txtplayerRank1 = findViewById(R.id.txtplayerRank1);
        txtplayerRank2 = findViewById(R.id.txtplayerRank2);
        txtplayerRank3 = findViewById(R.id.txtplayerRank3);
        txtplayerRank4 = findViewById(R.id.txtplayerRank4);
        txtplayerRank5 = findViewById(R.id.txtplayerRank5);
        txtplayerTime1 = findViewById(R.id.txtplayerTime1);
        txtplayerTime2 = findViewById(R.id.txtplayerTime2);
        txtplayerTime3 = findViewById(R.id.txtplayerTime3);
        txtplayerTime4 = findViewById(R.id.txtplayerTime4);
        txtplayerTime5 = findViewById(R.id.txtplayerTime5);
        buttonBack = findViewById(R.id.buttonBack);
        buttonReset = findViewById(R.id.buttonReset);
        wireStatusRef = FirebaseDatabase.getInstance().getReference().child("wireStatus");
        finishTimeRef = FirebaseDatabase.getInstance().getReference().child("rankings").child("timeFinish");
        buttonBack.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(Rankings.this, MainActivity.class);
            startActivity(intent);
        });
        buttonReset.setOnClickListener(v -> {
            finishTimeRef.removeValue();
        });

        finishTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<Long, String> timePlayerMap = new TreeMap<>(Collections.reverseOrder());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long time = snapshot.child("time").getValue(Long.class);
                    String playerName = snapshot.child("playerName").getValue(String.class);

                    // Store time as the key to maintain sorting order
                    // Use TreeMap with reverse ordering to sort by keys (times) in reverse
                    timePlayerMap.put(time, playerName);
                }

                // Retrieve the sorted entries
                List<Map.Entry<Long, String>> sortedEntries = new ArrayList<>(timePlayerMap.entrySet());

                // Display the top players in TextViews if available
                int counter = 1;
                for (Map.Entry<Long, String> entry : sortedEntries) {
                    if (counter <= 5) {
                        String playerRankId = "txtplayerRank" + counter;
                        String playerTimeId = "txtplayerTime" + counter;

                        TextView playerRankTextView = findViewById(getResources().getIdentifier(playerRankId, "id", getPackageName()));
                        TextView playerTimeTextView = findViewById(getResources().getIdentifier(playerTimeId, "id", getPackageName()));

                        long topTime = entry.getKey();
                        String correspondingPlayer = entry.getValue();

                        // Update TextViews with top players' information
                        playerRankTextView.setText(correspondingPlayer);
                        playerTimeTextView.setText(String.valueOf(topTime));

                        counter++;
                    } else {
                        break;
                    }
                }

                // Fill remaining TextViews if fewer than 5 players
                while (counter <= 5) {
                    String playerRankId = "txtplayerRank" + counter;
                    String playerTimeId = "txtplayerTime" + counter;

                    TextView playerRankTextView = findViewById(getResources().getIdentifier(playerRankId, "id", getPackageName()));
                    TextView playerTimeTextView = findViewById(getResources().getIdentifier(playerTimeId, "id", getPackageName()));

                    playerRankTextView.setText("No data available");
                    playerTimeTextView.setText("");

                    counter++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Rankings.this, "Cant Connect to Database", Toast.LENGTH_SHORT).show();
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