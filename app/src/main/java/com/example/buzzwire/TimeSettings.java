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


public class TimeSettings extends AppCompatActivity {
    DatabaseReference wireStatusRef;
    Button buttonSetTime;
    EditText setCountdown;
    DatabaseReference  setTimeDbRef;
    DatabaseReference setTimeDbRefInteger;
    DatabaseReference finishTimeRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_settings);
        Button buttonBack = findViewById(R.id.buttonBack);
        finishTimeRef = FirebaseDatabase.getInstance().getReference().child("rankings").child("timeFinish");
        buttonBack.setOnClickListener(v -> {
            // Start PlayerName activity
            Intent intent = new Intent(TimeSettings.this, MainActivity.class);
            startActivity(intent);
        });

        setCountdown = findViewById(R.id.setCountdown);
        buttonSetTime = findViewById(R.id.buttonSetTime);


        setTimeDbRefInteger = FirebaseDatabase.getInstance().getReference().child("setCountdownInteger");
        setTimeDbRef = FirebaseDatabase.getInstance().getReference().child("setCountdown");
        wireStatusRef = FirebaseDatabase.getInstance().getReference().child("wireStatus");
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTimeRef.removeValue();
                setTimeData();
                // Start PlayerName activity
                Intent intent = new Intent(TimeSettings.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
    private int convertTimeToInteger(String time) {
        String[] timeParts = time.split(":");
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);

        return (hours * 60) + minutes;
    }
    private void setTimeData(){
        String time = setCountdown.getText().toString();
        int timeInt = convertTimeToInteger(time);
        timeSetDataInteger timesetdatainteger = new timeSetDataInteger(timeInt);
        timeSetData timesetdata = new timeSetData(time);
        setTimeDbRefInteger.setValue(timesetdatainteger);
        setTimeDbRef.setValue(timesetdata);
        Toast.makeText(TimeSettings.this, "Time Inserted", Toast.LENGTH_SHORT).show();
    }


}