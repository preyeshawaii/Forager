/* used for seeing the list of submissions that need to be */

package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SubmissionsActivity extends AppCompatActivity {

    /**private String [] submissions_teamNames = {"StanfordStuds", "NightOwls", "VeraBlue"};
    private String [] submissions_challengeNames = {"Golden Gate Bridge", "Fountain", "Take photos with officer"};
    private int [] submission_points = {10,20,30}; **/

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView title;
    private ListView submissionList;
    private CustomAdapter customAdapter;

    private List<String> teamNames;
    private List<String> descriptions;
    private List<Integer> points;
    private List<Submission> subs;

    private String TAG = "SubmissionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.submissionText);
        submissionList = findViewById(R.id.submission_list);

        teamNames = new ArrayList<>();
        descriptions = new ArrayList<>();
        points = new ArrayList<>();
        subs = new ArrayList<>();
        customAdapter = new CustomAdapter();
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadSubmissions();
    }

    private void loadSubmissions(){
        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        final String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        title.setText("Submissions for " + huntName);

        subs.clear();
        teamNames.clear();
        descriptions.clear();
        points.clear();

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Submission.KEY_SUBMISSIONS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Submission sub = documentSnapshot.toObject(Submission.class);
                            teamNames.add(sub.getTeamName());
                            descriptions.add(sub.getDescription());
                            points.add(sub.getPoints());

                            subs.add(sub);
                        }

                        setAdapter(huntID, huntName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public void setAdapter(final String huntID, final String huntName){
        submissionList.setAdapter(customAdapter);

        submissionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String teamName = teamNames.get(position);
                String subID = subs.get(position).getSubmissionID();
                String description = descriptions.get(position);
                String teamComments = subs.get(position).getTeamComments();
                String imageUri = subs.get(position).getmImageUrl();

                Toast.makeText(getApplicationContext(), teamName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SubmissionsActivity.this, ProcessSubmissionActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                intent.putExtra(Submission.KEY_SUBMISSION_ID, subID);
                intent.putExtra(Submission.KEY_DESCRIPTION, description);
                intent.putExtra(Submission.KEY_TEAM_COMMENTS, teamComments);
                intent.putExtra(Submission.KEY_POINTS, Integer.toString(points.get(position)));
                intent.putExtra(Submission.KEY_TEAM_NAME, teamNames.get(position));
                intent.putExtra(Submission.KEY_IMAGE_URI, imageUri);
                Log.w(TAG, subID + ": " + teamName + ", " + description);
                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter{

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.submission_list_custom_view, null);
            TextView teamName = convertView.findViewById(R.id.submission_list_team_name);
            teamName.setText(teamNames.get(position));

            TextView challengeName = convertView.findViewById(R.id.submission_list_challenge);
            challengeName.setText(descriptions.get(position));

            TextView pointsAwarded = convertView.findViewById(R.id.submission_list_points);
            pointsAwarded.setText(Integer.toString(points.get(position)));
            return convertView;
        }
    }
}
