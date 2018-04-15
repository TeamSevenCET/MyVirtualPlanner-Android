package io.github.teamseven.myvirtualplanner;

import android.support.v7.app.AppCompatDialogFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.Context;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import  java.util.Calendar;
public class Subjects_lol extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView subName;
    private TextView subName1;
    private TextView subName2;
    private TextView subName3;
    private Subjects_lolListener listener;
    private String date=null,time=null,rem_text=null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_subjects_lol, null);
        builder.setView(view)
                .setTitle("Enter Dates/Deadlines: ")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subjectname = subName.getText().toString(); // fetches value entered
                        listener.addSub(subjectname, subName1.getText().toString(), subName2.getText().toString(), subName3.getText().toString());

                    }
                });
        subName = view.findViewById(R.id.textView8);
        subName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder, null);
                final EditText lReminder = (EditText) lview.findViewById(R.id.textReminder);
                lReminder.setText("Assignment:");
                final DatePicker rem_date = (DatePicker) lview.findViewById(R.id.datePicker4);
                final TimePicker rem_time = (TimePicker) lview.findViewById(R.id.timePicker);
                mBuilder.setView(lview);
                AlertDialog rem_dialog = mBuilder.create();
                rem_dialog.show();
                Button submit_date = (Button) lview.findViewById(R.id.submit_date);
                submit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day = 0;
                        int month = 0;
                        int year = 0;
                        int hour = 0;
                        int min = 0;
                        onDateSet(rem_date, day, month, year);
                        onTimeSet(rem_time, hour, min);
                        rem_text = lReminder.getText().toString().trim()+" ON  "+date+" AT "+time;
                        subName.setText(rem_text);
                        //algorithm for priority
                    }
                });

            }
        });
        subName1 = view.findViewById(R.id.textView9);
        subName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder, null);
                final EditText lReminder = (EditText) lview.findViewById(R.id.textReminder);
                lReminder.setText("Internal:");
                final DatePicker rem_date = (DatePicker) lview.findViewById(R.id.datePicker4);
                final TimePicker rem_time = (TimePicker) lview.findViewById(R.id.timePicker);
                mBuilder.setView(lview);
                AlertDialog rem_dialog = mBuilder.create();
                rem_dialog.show();
                Button submit_date = (Button) lview.findViewById(R.id.submit_date);
                submit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day = 0;
                        int month = 0;
                        int year = 0;
                        int hour = 0;
                        int min = 0;
                        onDateSet(rem_date, day, month, year);
                        onTimeSet(rem_time, hour, min);
                        rem_text = lReminder.getText().toString().trim()+" ON  "+date+" AT "+time;
                        subName1.setText(rem_text);
                        //algorithm for priority
                    }
                });

            }
        });
        subName2 = view.findViewById(R.id.textView10);
        subName2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder, null);
                final EditText lReminder = (EditText) lview.findViewById(R.id.textReminder);
                lReminder.setText("Quiz Test:");
                final DatePicker rem_date = (DatePicker) lview.findViewById(R.id.datePicker4);
                final TimePicker rem_time = (TimePicker) lview.findViewById(R.id.timePicker);
                mBuilder.setView(lview);
                AlertDialog rem_dialog = mBuilder.create();
                rem_dialog.show();
                Button submit_date = (Button) lview.findViewById(R.id.submit_date);
                submit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day = 0;
                        int month = 0;
                        int year = 0;
                        int hour = 0;
                        int min = 0;
                        onDateSet(rem_date, day, month, year);
                        onTimeSet(rem_time, hour, min);
                        rem_text = lReminder.getText().toString().trim()+" ON  "+date+" AT "+time;
                        subName2.setText(rem_text);
                        //algorithm for priority
                    }
                });

            }
        });
        subName3 = view.findViewById(R.id.textView11);
        subName3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder, null);
                final EditText lReminder = (EditText) lview.findViewById(R.id.textReminder);
                lReminder.setText("Semester:");
                final DatePicker rem_date = (DatePicker) lview.findViewById(R.id.datePicker4);
                final TimePicker rem_time = (TimePicker) lview.findViewById(R.id.timePicker);
                mBuilder.setView(lview);
                AlertDialog rem_dialog = mBuilder.create();
                rem_dialog.show();
                Button submit_date = (Button) lview.findViewById(R.id.submit_date);
                submit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day = 0;
                        int month = 0;
                        int year = 0;
                        int hour = 0;
                        int min = 0;
                        onDateSet(rem_date, day, month, year);
                        onTimeSet(rem_time, hour, min);
                        rem_text = lReminder.getText().toString().trim()+" ON  "+date+" AT "+time;
                        subName3.setText(rem_text);
                        //algorithm for priority
                    }
                });

            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (Subjects_lolListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement SubjectsListener");
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        i=datePicker.getDayOfMonth();
        String i_s1=theDate(i);
        i1=datePicker.getMonth()+1;
        String i_s2=theDate(i1);
        i2=datePicker.getYear();
        String i_s3=theDate(i2);
        date=i_s1+"-"+i_s2+"-"+i_s3;


    }
    public String theDate(int i){ //for formatting of date
        String i_s=Integer.toString(i);
        if(i_s.length()==1){
            i_s="0"+i_s;
        }
        return i_s;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Calendar calendar=Calendar.getInstance();
        i=timePicker.getCurrentHour();
        i1=timePicker.getCurrentMinute();
        if(Integer.toString(i).length()==1){
            time="0"+Integer.toString(i);
        }else{
            time=Integer.toString(i);
        }
        if(Integer.toString(i1).length()==1){
            time+=":0"+Integer.toString(i1);
        }else{
            time+=":"+Integer.toString(i1);
        }


    }
    // Adds a new subject to the menu
    public interface Subjects_lolListener {
        void addSub(String subjectname, String s1,String s2,String s3);
    }


}