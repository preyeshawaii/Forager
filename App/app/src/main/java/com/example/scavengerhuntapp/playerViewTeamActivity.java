package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class playerViewTeamActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private TextView teamNameTV;
    private ListView teamMemberListview;

    private String huntID;
    private String huntName;
    private String teamName;
    private String teamID;

    private Team team;
    private List<String> memberNames;
    private List<String> memberPhoneNumbers;
    private CustomAdapter customAdapter;

    private String TAG = "playerViewTeamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_view_own_team);

        db = FirebaseFirestore.getInstance();

        teamNameTV = findViewById(R.id.team_info_name);
        teamMemberListview = findViewById(R.id.team_member_list);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        memberNames = new ArrayList<>();
        memberPhoneNumbers = new ArrayList<>();

        customAdapter = new CustomAdapter();
    }

    @Override
    protected void onStart(){
        super.onStart();

        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);

        teamNameTV.setText(teamName);

        loadTeam();
        teamMemberListview.setAdapter(customAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

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
                        intent3.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });
    }

    public void clickedAddMember (View v){
        EditText nameInfo = findViewById(R.id.inputMemberNameText);
        EditText phoneNumberInfo = findViewById(R.id.inputPhoneNumberText);
        String name = nameInfo.getText().toString();
        String number = phoneNumberInfo.getText().toString();

        if(name.length() > 1 && number.length() > 1){ // both fields non empty
            nameInfo.getText().clear();
            phoneNumberInfo.getText().clear();
            customAdapter.notifyDataSetChanged();
            addMember(name, number);
        }

    }

    private void loadTeam(){
        memberNames.clear();
        memberPhoneNumbers.clear();

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        team = documentSnapshot.toObject(Team.class);
                        Map<String, String> members = team.getNamesAndPhoneNums();

                        for (Map.Entry<String,String> entry : members.entrySet()){
                            memberNames.add(entry.getKey());
                            memberPhoneNumbers.add(entry.getValue());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void addMember(final String name, final String number){
        team.addMember(name, number);
        memberNames.add(name);
        memberPhoneNumbers.add(number);
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).set(team)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Added " + name, Toast.LENGTH_SHORT).show();
                        teamMemberListview.setAdapter(customAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: Could not add " + name, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.toString());
                    }
                });
    }


    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return memberNames.size();
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

            teamMemberName.setText(memberNames.get(position));
            teamMemberPhoneNum.setText(memberPhoneNumbers.get(position));
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
