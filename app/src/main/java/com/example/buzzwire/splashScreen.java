package com.example.buzzwire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        VideoView videoView = findViewById(R.id.splashVideo);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bacgroundsplash; // Replace with your video filename
        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.setOnCompletionListener(mp -> {
            // When video finishes playing, start the main activity
            Intent intent = new Intent(splashScreen.this, MainActivity.class); // Replace MainActivity with your target activity
            startActivity(intent);
            finish(); // Close the splash activity to prevent returning to it when pressing back
        });

        videoView.start();
    }
}