package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TeamInfoActivity<teamMembers> extends AppCompatActivity {


    // copied same data is a simple version of what the database could be
    private String [] teamNames = {"Rebels", "Chickens", "Donut Lovers", "TryAndCry", "StanfordStuds", "NoMoreTears", "FoshoFros","Rebels1", "Chickens1", "Donut Lovers1", "TryAndCry1", "StanfordStuds1", "NoMoreTears1", "FoshoFros1"};
    private ArrayList<ArrayList<String>> teamMembers;
    private int [] points = {0,0,0,10,20,30,40,0,0,0,10,20,30,40};
    private ArrayList<String> currTeamMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        teamMembers = new ArrayList<>();
        for(int i = 0; i < teamNames.length; i++){

            ArrayList<String> currMembers = new ArrayList<>();
            String iVal = String.valueOf(i);
            currMembers.add("Samantha" + iVal);
            currMembers.add("Jonathan" + iVal);
            currMembers.add("Noah" + iVal);
            currMembers.add("Samanthaaa" + iVal);
            currMembers.add("Jonathanaaa" + iVal);
            currMembers.add("Noahaaaa" + iVal);
            teamMembers.add(currMembers);
        }

        // this is where we import the proper index of the to get the right list of names.
        Intent currIntent = getIntent();
        String teamName = currIntent.getStringExtra("teamName");
        int index = 0;
        for(int i = 0; i < teamNames.length; i++){
            if(teamName.equals(teamNames[i])){
                index = i;
                break;
            }
        }

        ArrayList<String> correspondingMembers = teamMembers.get(index); // this is what we will print
        currTeamMembers = correspondingMembers;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        ListView teamMemberListview= findViewById(R.id.team_member_list);
        CustomAdapter  customAdapter = new CustomAdapter();
        teamMemberListview.setAdapter(customAdapter);
        // change the name of the team name
        TextView teamNameView = findViewById(R.id.team_info_name);
        teamNameView.setText(teamName);


    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currTeamMembers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.team_members_list_custom_view, null);
            // initialize all of the different types of views
            TextView teamMemberName = (TextView)convertView.findViewById(R.id.team_member_view_name);
            //TextView teamMemberPhone = (TextView)convertView.findViewById(R.id.team_member_view_phone);

            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here
            teamMemberName.setText(currTeamMembers.get(position));
            // can set the phone number here
            return convertView;
        }
    }



}
