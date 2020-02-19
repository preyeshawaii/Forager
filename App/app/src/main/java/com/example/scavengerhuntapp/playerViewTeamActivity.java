package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
        /*
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true); */

        /*

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_alerts:
                        Intent intent = new Intent(playerViewTeamActivity.this, AnnouncementsActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        startActivity(intent);
                        break;
                    case R.id.action_challenges:
                        Intent intent1 = new Intent(playerViewTeamActivity.this, PlayerHuntLandingActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent1.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent1.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        startActivity(intent1);
                        break;
                    case R.id.action_team:
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(playerViewTeamActivity.this, RankingsActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent3.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent3.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });
        */

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_hunts:
                Intent intent = new Intent(playerViewTeamActivity.this, PlayerLandingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(playerViewTeamActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }






}
