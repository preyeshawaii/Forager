package com.example.scavengerhuntapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class playerViewTeamActivity extends AppCompatActivity {

    private String teamName = "PURE GOATS";
    private ArrayList<String> teamMembers;
    private ArrayList<String> phoneNumbers;
    CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_view_own_team);

       teamMembers =  new ArrayList<String>(Arrays.asList("Jonathan", "Sarah", "Chloe", "Bonnie"));
       phoneNumbers =  new ArrayList<String>(Arrays.asList("(203) 550 - 5555", "(203) 550 - 5555", "(203) 550 - 5555","(203) 550 - 5555)"));

                // can set the team name here from the database
        TextView TeamName = findViewById(R.id.team_info_name);
        TeamName.setText(teamName);

        ListView teamMemberListview = findViewById(R.id.team_member_list);
        customAdapter = new CustomAdapter();
        teamMemberListview.setAdapter(customAdapter);
    }

    public void clickedAddMember (View v){
        EditText nameInfo = findViewById(R.id.inputMemberNameText);
        EditText phoneNumberInfo = findViewById(R.id.inputPhoneNumberText);
        String name = nameInfo.getText().toString();
        String number = phoneNumberInfo.getText().toString();

        if(name.length() > 1 && number.length() > 1){ // both fields non empty
            teamMembers.add(name);
            phoneNumbers.add(number);
            nameInfo.getText().clear();
            phoneNumberInfo.getText().clear();
            customAdapter.notifyDataSetChanged();
        }

    }


    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return teamMembers.size();
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
            TextView teamMemberName = convertView.findViewById(R.id.team_member_view_name);
            TextView teamMemberPhoneNum = convertView.findViewById(R.id.team_member_view_phone);

            teamMemberName.setText(teamMembers.get(position));
            teamMemberPhoneNum.setText(phoneNumbers.get(position));
            return convertView;
        }
    }








}