package com.example.scavengerhuntapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class EditChallengeDialog extends AppCompatDialogFragment {
    private EditText challengeEditText;
    private EditText challengeLocationEditText;
    private EditText pointsEditText;
    private EditChallengeDialog.CustomEditChallengeListener listener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_challenge_view, null);
        builder.setView(view)
                .setTitle("Edit Challenge")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String checkPoints = pointsEditText.getText().toString();
                        if (checkPoints.matches("")){
                            checkPoints = "0";
                        }

                        String challenge = challengeEditText.getText().toString();
                        String location = challengeLocationEditText.getText().toString();
                        Integer pointsInt = Integer.parseInt(checkPoints);
                        listener.getTexts(challenge, location, pointsInt);
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
            listener = (EditChallengeDialog.CustomEditChallengeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement CustomChallengeListener");
        }

    }

    public interface CustomEditChallengeListener{
        void getTexts(String challenge, String location, Integer points);
    }
}
