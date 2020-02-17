package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;
import java.util.ArrayList;
import java.util.List;


public class RankingsActivity extends AppCompatActivity {
    /**String[] teamArray = {"Team1","Team2","Team3","Team4",
     "Team5"};
     int[] teamScores = {4,3,40,20,1}; **/

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button backBtn;
    private ListView teamsListView;
    private Switch viewPointSwitch;

    private List<Team> teams;
    private CustomAdapter customAdapter;
    private Boolean isPlayer;
    private Boolean canViewPoints;

    private String TAG = "RankingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.rankings_go_back);
        teamsListView = findViewById(R.id.team_list);
        viewPointSwitch = findViewById(R.id.show_points_switch);

        String userType = getIntent().getExtras().getString(User.KEY_PLAYER_TYPE);
        isPlayer = userType.equals(User.KEY_PLAYER)? true : false;
        BottomNavigationView bottomNavigationView = isPlayer ?
                (BottomNavigationView)findViewById(R.id.bottom_navigation) :
                (BottomNavigationView)findViewById(R.id.organizer_bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);


        teams = new ArrayList<>();
        customAdapter = new CustomAdapter();

        viewPointSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDB(isChecked);
            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = isPlayer ? new Intent(RankingsActivity.this, PlayerHuntLandingActivity.class) : new Intent(RankingsActivity.this, HuntLandingActivity.class);

                intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                startActivity(intent);
            }
        });

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_alerts:
                        Intent intent = new Intent(RankingsActivity.this, AnnouncementsActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        startActivity(intent);
                        break;
                    case R.id.action_challenges:
                        Intent intent2 = new Intent(RankingsActivity.this, PlayerHuntLandingActivity.class);
                        intent2.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent2.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent2.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        startActivity(intent2);
                        break;
                    case R.id.action_team:
                        break;
                    case R.id.action_rankings:
                        break;
                }

                return false;
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        loadRankings();
    }

    private void updateDB(final Boolean isChecked){
        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hunt hunt = documentSnapshot.toObject(Hunt.class);
                        hunt.setViewPoints(isChecked);

                        db.collection(Hunt.KEY_HUNTS).document(huntID).set(hunt)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        canViewPoints  = isChecked;

                                        if (isPlayer == true) {
                                            viewPointSwitch.setVisibility(View.GONE);
                                        }

                                        teamsListView.setAdapter(customAdapter);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.toString();
                                    }
                                });
                    }
                });
    }

    private void loadRankings(){
        teams.clear();

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hunt hunt = documentSnapshot.toObject(Hunt.class);
                        viewPointSwitch.setChecked(hunt.getViewPoints());
                        canViewPoints  = hunt.getViewPoints();

                        if (isPlayer == true) {
                            viewPointSwitch.setVisibility(View.GONE);
                        }

                        rankTeams(huntID);
                    }
                });
    }

    private void rankTeams(final String huntID){
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS)
                .orderBy(Team.KEY_POINTS, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Team team = documentSnapshot.toObject(Team.class);
                            teams.add(team);
                        }

                        setAdapter(huntID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }


    public void setAdapter(final String huntID){
        teamsListView.setAdapter(customAdapter);

        teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String teamName = teams.get(position).getTeamName();


                Toast.makeText(getApplicationContext(), teamName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RankingsActivity.this, TeamInfoActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                intent.putExtra(Team.KEY_TEAM_ID, teams.get(position).getTeamID());

                String playerType = isPlayer ? User.KEY_PLAYER : User.KEY_ORGANIZER;
                intent.putExtra(User.KEY_PLAYER_TYPE, playerType);

                startActivity(intent);
            }
        });
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return teams.size();
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
            TextView teamName = convertView.findViewById(R.id.teamview_name);
            TextView teamPoints = convertView.findViewById(R.id.teamview_points);
            if (canViewPoints == false)   {
                teamPoints.setVisibility(View.GONE);
            }
            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here

            teamName.setText(teams.get(position).getTeamName());
            teamPoints.setText(teams.get(position).getPoints() + " pts");
            return convertView;
        }
    }
}
