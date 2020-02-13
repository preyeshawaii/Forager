package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;
import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

    private List<String> teamNames;
    private List<Integer> points;
    private CustomAdapter customAdapter;
    private String TAG = "RankingsActivity";

    Boolean isPlayer;
    Boolean viewPointSwitchState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String player_organizer = getIntent().getExtras().getString("playerType");
        isPlayer = player_organizer == "player"? true: false;
        backBtn = findViewById(R.id.rankings_go_back);
        teamsListView = findViewById(R.id.team_list);
        viewPointSwitch = findViewById(R.id.show_points_switch);
        viewPointSwitchState = viewPointSwitch.isChecked();
        if (isPlayer == true)   {
            viewPointSwitch.setVisibility(View.GONE);
        }

        teamNames = new ArrayList<>();
        points = new ArrayList<>();
        customAdapter = new CustomAdapter();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = isPlayer? new Intent(RankingsActivity.this, PlayerHuntLandingActivity.class) : new Intent(RankingsActivity.this, HuntLandingActivity.class);

                intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        loadRankings();
    }

    private void loadRankings(){
        teamNames.clear();
        points.clear();
        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS)
                .orderBy(Team.KEY_POINTS, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Team team = documentSnapshot.toObject(Team.class);

                            teamNames.add(team.getTeamName());
                            points.add(team.getPoints());
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
                String teamName = teamNames.get(position);


                Toast.makeText(getApplicationContext(), teamName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RankingsActivity.this, TeamInfoActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);

                startActivity(intent);
            }
        });
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return teamNames.size();
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
            if (viewPointSwitchState == true)   {
                teamPoints.setVisibility(View.GONE);
            }
            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here

            teamName.setText(teamNames.get(position));
            teamPoints.setText(points.get(position) + " pts");
            return convertView;
        }
    }
}
