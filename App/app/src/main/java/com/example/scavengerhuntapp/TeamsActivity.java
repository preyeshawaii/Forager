package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    //private String [] teamNames = {"Rebels", "Chickens", "Donut Lovers", "TryAndCry", "StanfordStuds", "NoMoreTears", "FoshoFros","Rebels1", "Chickens1", "Donut Lovers1", "TryAndCry1", "StanfordStuds1", "NoMoreTears1", "FoshoFros1"};
    //private int [] points = {0,0,0,10,20,30,40,0,0,0,10,20,30,40};

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ListView teamsListView;
    private TextView title;

    private List<String> teamIDs;
    private List<String> teamNames;
    private List<Integer> points;
    private CustomAdapter customAdapter;
    private  String TAG = "TeamsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        teamsListView = findViewById(R.id.teamsListView);
        title = findViewById(R.id.team_hunt_title);

        teamIDs = new ArrayList<>();
        teamNames = new ArrayList<>();
        points = new ArrayList<>();
        customAdapter = new CustomAdapter();
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadTeams();
    }

    private void loadTeams(){
        teamNames.clear();
        points.clear();
        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);

        title.setText("Teams for " + huntName);

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                        Log.w(TAG, Integer.toString(queryDocumentSnapshots.size()));

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Team team = documentSnapshot.toObject(Team.class);
                            teamIDs.add(team.getTeamID());
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
                String teamID = teamIDs.get(position);

                Toast.makeText(getApplicationContext(), teamName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TeamsActivity.this, TeamInfoActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                intent.putExtra(Team.KEY_TEAM_ID, teamID);
                Log.w(TAG, teamID + ": " + teamName);
                startActivity(intent);
            }
        });
    }


    // used to populate custom list view
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

            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here

            teamName.setText(teamNames.get(position));
            teamPoints.setText(points.get(position) + " pts");
            return convertView;
        }
    }
}
