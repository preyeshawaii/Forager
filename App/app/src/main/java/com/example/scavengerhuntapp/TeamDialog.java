package com.example.scavengerhuntapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TeamDialog extends AppCompatDialogFragment {
    private EditText teamName;
    private TeamDialogListener listener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.team_dialog, null);

        teamName = view.findViewById(R.id.team_name);

        builder.setView(view)
                .setTitle("Select Team Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(teamName.getText().toString().matches("")){
                            Toast.makeText(getActivity(), "Missing team name, could not save.", Toast.LENGTH_SHORT).show();
                        } else{
                            listener.getTeamName(teamName.getText().toString());
                        }
                    }
                });

        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TeamDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement TeamDialogListener");
        }

    }

    public interface TeamDialogListener{
        void getTeamName(String teamName);
    }
}
