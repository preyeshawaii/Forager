package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PremadeChallengeListActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    int[] ICONS = {R.drawable.icecream, R.drawable.group, R.drawable.dog};
    String[] CHALLENGES = {"Eat the Earthquake", "Loudly advertise See's Chocolate", "Walk someone's dog"};
    String[] LOCATIONS = {"Ghirardelli Square", "Chinatown", "Golden Gate Park"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premade_challenge_list);

        final ListView premadeChallengesList=(ListView)findViewById(R.id.premade_challenge_list);
        Button addToHuntButton = (Button)findViewById(R.id.add_to_hunt_button);

        final CustomAdapter customAdapter=new CustomAdapter();
        premadeChallengesList.setChoiceMode(premadeChallengesList.CHOICE_MODE_MULTIPLE);
        premadeChallengesList.setAdapter(customAdapter);

        addToHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO(@Jorge): Store the selected challenges in database for access elsewhere
                // Commented code shows how to store as strings, but we need to store as objects
//                String selected = "";
                int cntChoice = premadeChallengesList.getCount();

                SparseBooleanArray sparseBooleanArray = premadeChallengesList.getCheckedItemPositions();
                for(int i = 0; i < sparseBooleanArray.size(); i++){
                    System.out.println(sparseBooleanArray.valueAt(i));
                    if (sparseBooleanArray.valueAt(i)) {
                        System.out.println("Checking list while adding:" +
                                premadeChallengesList.getItemAtPosition(i).toString());

                    }
//                    if(sparseBooleanArray.get(i)) {
//                        selected += premadeChallengesList.getItemAtPosition(i);
//                        System.out.println("Checking list while adding:" + myList.getItemAtPosition(i).toString());
//                        SaveSelections();
//                    }

                }
                Intent intent = new Intent(PremadeChallengeListActivity.this, PremadeHuntsActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void SaveSelections() {
//        // save the selections in the shared preference in private mode for the user
//        SharedPreferences.Editor prefEditor = sharedpreferences.edit();
//        String savedItems = getSavedItems();
//        prefEditor.putString(MyPREFERENCES.toString(), savedItems);
//        prefEditor.commit();
//    }

    class CustomAdapter extends BaseAdapter{
        private CheckBox checkBox;

        @Override
        public int getCount() {
            return ICONS.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public Boolean isChecked(int i){
           return checkBox.isChecked();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.challenge_custom_view,null);
            // Populate list view
            ImageView imageView = (ImageView)view.findViewById(R.id.iconImageView);
            TextView challengeTextView = (TextView)view.findViewById(R.id.challengeTextView);
            TextView challengeLocationTextView = (TextView)view.findViewById(R.id.challengeLocationTextView);
            checkBox = findViewById(R.id.checkBox);

            imageView.setImageResource(ICONS[i]);
            challengeTextView.setText(CHALLENGES[i]);
            challengeLocationTextView.setText(LOCATIONS[i]);

            return view;
        }
    }
}
