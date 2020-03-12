package com.example.scavengerhuntapp.organizer;

import android.app.Activity;
import android.content.Intent;
import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.widget.ImageView;



import com.google.firebase.auth.FirebaseAuth;

import static android.R.layout.simple_spinner_item;

import com.example.scavengerhuntapp.R;

public class CustomChallengeDialog extends AppCompatDialogFragment {
    private EditText challengeEditText;
    private EditText challengeLocationEditText;
    private EditText pointsEditText;
    private CustomChallengeListener listener;
    private Spinner spinner;
    public int icon;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_challenge_view, null);

        final CreatingHuntSingleton creatingHuntSingleton = CreatingHuntSingleton.init();
        List<String> list1 = creatingHuntSingleton.getIconNameList();

        final ImageView image = view.findViewById(R.id.iconImageView);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (view.getContext(), android.R.layout.simple_spinner_item, list1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                icon = creatingHuntSingleton.getSpinnerIcon(position);
                image.setImageResource(icon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setView(view)
                .setTitle("Add custom challenge")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Add to hunt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String checkPoints = pointsEditText.getText().toString();
                        if (checkPoints.matches("")){
                            checkPoints = "10";
                        }


                        String challenge = challengeEditText.getText().toString();
                        String location = challengeLocationEditText.getText().toString();
                        Integer pointsInt = Integer.parseInt(checkPoints);
                        listener.getTexts(challenge, location, pointsInt, icon);
                    }
                });
        challengeEditText = view.findViewById(R.id.challengeEditTextView);
        challengeLocationEditText = view.findViewById(R.id.challengeLocationEditTextView);
        pointsEditText = view.findViewById(R.id.pointsEditText);

        return builder.create();
    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CustomChallengeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement CustomChallengeListener");
        }

    }

    public interface CustomChallengeListener{
        void getTexts(String challenge, String location, Integer points, int icon);
    }


}
