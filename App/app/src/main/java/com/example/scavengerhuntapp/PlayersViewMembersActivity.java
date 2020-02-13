package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayersViewMembersActivity extends AppCompatActivity {

    private String[] teamNames = {"Rebels", "Chickens", "Donut Lovers", "TryAndCry", "StanfordStuds", "NoMoreTears", "FoshoFros", "Rebels1", "Chickens1", "Donut Lovers1", "TryAndCry1", "StanfordStuds1", "NoMoreTears1", "FoshoFros1"};
    private String [] phoneNumbers = {"(111) 555-7878"};
    private ArrayList<ArrayList<String>> teamMembers;
    private int[] points = {0, 0, 0, 10, 20, 30, 40, 0, 0, 0, 10, 20, 30, 40};
    private ArrayList<String> currTeamMembers;
    private TextView teamName;
    private ListView teamMembersList;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        // creating dummy data
        teamMembers = new ArrayList<>();
        for (int i = 0; i < teamNames.length; i++) {

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
        customAdapter = new CustomAdapter();


        // here is where we can identify which team is which 
        loadTeamInfo(1);

    }

    // takes in an index (or we can switch to the key of a team to retrieve their values
    private void loadTeamInfo(int index){
        currTeamMembers = teamMembers.get(index);
        teamName = findViewById(R.id.team_info_name);
        teamMembersList = findViewById(R.id.team_member_list);
        teamName.setText(teamNames[index]);

        CustomAdapter  customAdapter = new CustomAdapter();
        teamMembersList.setAdapter(customAdapter);

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
            TextView teamMemberPhoneNum = convertView.findViewById(R.id.team_member_view_phone);

            teamMemberName.setText(currTeamMembers.get(position));
            teamMemberPhoneNum.setText(phoneNumbers[0]);
            return convertView;

        }
    }

}
