package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PlayerLandingActivity extends AppCompatActivity {

    private Button signOutButton;
    private Button huntCodeButton;

    private TextView noHuntsView;
    private ListView huntsListView;
    private EditText huntCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_landing);

        signOutButton = findViewById(R.id.sign_out);
        huntCodeButton = findViewById(R.id.hunt_code_button);
        huntsListView = findViewById(R.id.player_hunts_listView);
        noHuntsView = findViewById(R.id.no_hunts_view);
        huntCodeEditText = findViewById(R.id.hunt_code_et);


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PlayerLandingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        huntCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String huntCodeResult = huntCodeEditText.getText().toString();
                // TODO (@Jorge): use hunt code to determine which hunt you need to load
                Intent intent = new Intent(PlayerLandingActivity.this, PlayerHuntLandingActivity.class);
                startActivity(intent);
            }
        });
    }

    // TODO (@Jorge): load info about current hunts
}
