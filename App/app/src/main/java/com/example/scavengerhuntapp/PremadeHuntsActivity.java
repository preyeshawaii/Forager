package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PremadeHuntsActivity extends AppCompatActivity {

    String[] locationsArray = {"San Francisco"};
    ListView locationsListView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premade_hunts);

        adapter = new ArrayAdapter<>(this,
                R.layout.single_list_item, R.id.premade_hunt_location, locationsArray);

        locationsListView = (ListView)findViewById(R.id.locations_list);
        locationsListView.setAdapter(adapter);
        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PremadeHuntsActivity.this, PremadeChallengeListActivity.class);
                // Need to put the location as an extra so we can use that to get the premade
                // challenges that correspond to that location from the database
//                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
//                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
//                Log.w(TAG, huntID + ": " + huntName);
                startActivity(intent);            }
        });

    }


}
