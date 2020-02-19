package com.example.scavengerhuntapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        memberNames = new ArrayList<>();
        memberPhoneNumbers = new ArrayList<>();

        customAdapter = new CustomAdapter();
    }

    @Override
    protected void onStart(){
        super.onStart();
        teamNameTV.setText(teamName);

        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);

        loadTeam();
        teamMemberListview.setAdapter(customAdapter);
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
}
