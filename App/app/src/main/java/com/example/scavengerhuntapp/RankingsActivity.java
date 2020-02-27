package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private FirebaseFirestore db;

    private ListView teamsListView;
    private SwipeRefreshLayout swipeContainer;

    private List<Team> teams;
    private CustomAdapter customAdapter;
    private Boolean canViewPoints;

    private String TAG = "RankingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        db = FirebaseFirestore.getInstance();

        teamsListView = findViewById(R.id.team_list);
        swipeContainer = findViewById(R.id.swipe_container_rankings);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        teams = new ArrayList<>();
        customAdapter = new CustomAdapter();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRankings();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark);

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
                        intent.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        startActivity(intent);
                        break;
                    case R.id.action_challenges:
                        Intent intent2 = new Intent(RankingsActivity.this, PlayerHuntLandingActivity.class);
                        intent2.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent2.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent2.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent2.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        startActivity(intent2);
                        break;
                    case R.id.action_team:
                        Intent intent1 = new Intent (RankingsActivity.this, playerViewTeamActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent1.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent1.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        intent1.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        startActivity(intent1);
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

    private void loadRankings(){
        teams.clear();

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hunt hunt = documentSnapshot.toObject(Hunt.class);
                        canViewPoints  = hunt.getViewPoints();
                    }
                });

        rankTeams(huntID);
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

        /*teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String teamName = teams.get(position).getTeamName();


                Toast.makeText(getApplicationContext(), teamName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RankingsActivity.this, TeamInfoActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                intent.putExtra(Team.KEY_TEAM_ID, teams.get(position).getTeamID());

                startActivity(intent);
            }
        });*/
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
                Intent intent = new Intent(RankingsActivity.this, PlayerLandingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(RankingsActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
