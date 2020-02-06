package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class RankingsActivity extends AppCompatActivity {

    Button button;
    ListView simpleList;
    String[] teamArray = {"Team1","Team2","Team3","Team4",
            "Team5"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        addListenerOnButton();

        simpleList = (ListView)findViewById(R.id.team_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_rankings, R.id.team_list, teamArray);
        simpleList.setAdapter(arrayAdapter);

    }

    public void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }

        });

    }


}
