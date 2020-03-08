package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamInfoActivity<teamMembers> extends AppCompatActivity {


    // copied same data is a simple version of what the database could be
    //private String [] teamNames = {"Rebels", "Chickens", "Donut Lovers", "TryAndCry", "StanfordStuds", "NoMoreTears", "FoshoFros","Rebels1", "Chickens1", "Donut Lovers1", "TryAndCry1", "StanfordStuds1", "NoMoreTears1", "FoshoFros1"};
    //private ArrayList<ArrayList<String>> teamMembers;
    //private int [] points = {0,0,0,10,20,30,40,0,0,0,10,20,30,40};
    //private ArrayList<String> currTeamMembers;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView teamNameView;
    private ListView teamMemberListview;

    private Team team;

    private  String TAG = "TeamInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**teamMembers = new ArrayList<>();
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
        currTeamMembers = correspondingMembers;  **/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        teamNameView = findViewById(R.id.team_info_name);
        teamMemberListview = findViewById(R.id.team_member_list);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadTeamInfo();
    }

    private void loadTeamInfo(){
        String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        String teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);
        String teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);


        teamNameView.setText(teamName);

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        team = documentSnapshot.toObject(Team.class);

                        CustomAdapter  customAdapter = new CustomAdapter();
                        teamMemberListview.setAdapter(customAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    class CustomAdapter extends BaseAdapter {
        private Map<String, String> currTeamMembers = team.getNamesAndPhoneNums();

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
            List<String> teamNames = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            for (Map.Entry<String, String> entry : currTeamMembers.entrySet()){
                teamNames.add(entry.getKey());
                phoneNumbers.add(entry.getValue());
            }

            convertView = getLayoutInflater().inflate(R.layout.team_members_list_custom_view, null);
            // initialize all of the different types of views
            TextView teamMemberName = convertView.findViewById(R.id.team_member_view_name);
            TextView teamMemberPhoneNum = convertView.findViewById(R.id.team_member_view_phone);
            convertView.findViewById(R.id.teamMemberDelete).setVisibility(View.GONE); // remove x option from organizer since it's not their team or duty 


            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here
            teamMemberName.setText(teamNames.get(position));
            teamMemberPhoneNum.setText(phoneNumbers.get(position));
            // can set the phone number here
            return convertView;
        }
    }



}
