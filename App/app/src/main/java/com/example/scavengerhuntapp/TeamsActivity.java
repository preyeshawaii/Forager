package com.example.scavengerhuntapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TeamsActivity extends AppCompatActivity {

    private String [] teamNames = {"Rebels", "Chickens", "Donut Lovers", "TryAndCry", "StanfordStuds", "NoMoreTears", "FoshoFros","Rebels1", "Chickens1", "Donut Lovers1", "TryAndCry1", "StanfordStuds1", "NoMoreTears1", "FoshoFros1"};
    private int [] points = {0,0,0,10,20,30,40,0,0,0,10,20,30,40};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        ListView teamListView = findViewById(R.id.teamsListView);
        CustomAdapter customAdapter = new CustomAdapter();
        teamListView.setAdapter(customAdapter);
    }

    public void clickedTeam(View v){
        Intent intent = new Intent(this, TeamInfoActivity.class);
        TextView teamName = v.findViewById(R.id.teamview_name);
        intent.putExtra("teamName", teamName.getText()); // this is where we are passing the identifier for the team
        startActivity(intent);
    }


    // used to populate custom list view
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return teamNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.team_list_custom_view, null);
            // initialize all of the different types of views
            TextView teamName = (TextView)convertView.findViewById(R.id.teamview_name);
            TextView teamPoints = (TextView)convertView.findViewById(R.id.teamview_points);

            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here
            teamName.setText(teamNames[position]);
            teamPoints.setText(String.valueOf(points[position]) + " pts");
            return convertView;
        }
    }
}
