package com.example.scavengerhuntapp.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.scavengerhuntapp.R;

public class PremadeHuntsActivity extends AppCompatActivity {

    String[] locationsArray = {"San Francisco"};

    private ListView locationsListView;
    private Button backButton;
    private ArrayAdapter<String> adapter;

    private String TAG = "PremadeHuntsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premade_hunts);

        locationsListView = findViewById(R.id.locations_list);
        backButton = findViewById(R.id.premade_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PremadeHuntsActivity.this, CreateHuntActivity.class);
                startActivity(intent);
            }
        });

        adapter = new ArrayAdapter<>(this, R.layout.single_list_item, R.id.premade_hunt_location, locationsArray);

        locationsListView.setAdapter(adapter);

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PremadeHuntsActivity.this, PremadeChallengeListActivity.class);
                startActivity(intent);
            }
        });

    }


}
