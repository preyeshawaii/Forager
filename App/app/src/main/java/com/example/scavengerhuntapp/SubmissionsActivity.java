package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubmissionsActivity extends AppCompatActivity {

    private String [] submissions_teamNames = {"StanfordStuds", "NightOwls", "VeraBlue"};
    private String [] submissions_challengeNames = {"Golden Gate Bridge", "Fountain", "Take photos with officer"};
    private int [] submission_points = {10,20,30};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);

        ListView submissionList = findViewById(R.id.submission_list);
        CustomAdapter customAdapter = new CustomAdapter();
        submissionList.setAdapter(customAdapter);

    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return submissions_teamNames.length;
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
            teamName.setText(submissions_teamNames[position]);

            TextView challengeName = convertView.findViewById(R.id.submission_list_challenge);
            challengeName.setText(submissions_challengeNames[position]);

            TextView pointsAwarded = convertView.findViewById(R.id.submission_list_points);
            pointsAwarded.setText(String.valueOf(submission_points[position]));
            return convertView;
        }
    }
}
