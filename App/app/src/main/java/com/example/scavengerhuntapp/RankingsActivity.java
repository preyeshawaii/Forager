package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.Arrays;


public class RankingsActivity extends AppCompatActivity {

    Button button;
    ListView mListView;
    private ArrayAdapter aAdapter;
    String[] teamArray = {"Team1","Team2","Team3","Team4",
            "Team5"};
    int[] teamScores = {4,3,40,20,1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        addListenerOnButton();

        mListView = (ListView) findViewById(R.id.team_list);
        aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sort_elements(teamArray,teamScores));
        mListView.setAdapter(aAdapter);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_rankings, R.id.team_list, teamArray);
        //simpleList.setAdapter(arrayAdapter);

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

    private String[] sort_elements(String[] teamArray,int[] teamScores)   {
        String[] names = new String[teamArray.length];
        int[] nums = new int[teamArray.length];
        for (int k = 0; k < nums.length; k ++)  {
            nums[k] = teamScores[k];
        }
        Arrays.sort(nums);
       // int[] nums = {1,3,4,20,40};
        for (int i = 0; i < nums.length; i++)   {
            int value = nums[i];
            for (int j = 0; j < nums.length; j++)   {
                if (teamScores[j] == value) {
                    names[i]= teamArray[j];
                }
            }
        }

        return names;

    }




}
