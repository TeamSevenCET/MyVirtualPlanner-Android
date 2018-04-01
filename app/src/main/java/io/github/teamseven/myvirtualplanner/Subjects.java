package io.github.teamseven.myvirtualplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class Subjects extends AppCompatDialogFragment {
    private EditText subName;
    private SubjectsListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.content_subjects, null);
        builder.setView(view)
                .setTitle("Enter Subject Name: ")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add Subject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subjectname= subName.getText().toString(); // fetches value entered

                        listener.addSub(subjectname);
                    }
                });
        subName= view.findViewById(R.id.editText);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (SubjectsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement SubjectsListener");
        }
    }

   // Adds a new subject to the menu
   public interface SubjectsListener
    {
        void addSub(String subjectname);
    }
}
