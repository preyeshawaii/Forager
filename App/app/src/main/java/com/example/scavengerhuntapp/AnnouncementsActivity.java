package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class AnnouncementsActivity extends AppCompatActivity {

    private Button backButton;
    private ListView announcementsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        backButton = findViewById(R.id.back_btn);
        announcementsListView = findViewById(R.id.announcements_list);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnnouncementsActivity.this, PlayerHuntLandingActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));

                startActivity(intent);
            }
        });


    }
}
