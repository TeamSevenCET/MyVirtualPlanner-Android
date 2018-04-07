package io.github.teamseven.myvirtualplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import java.util.regex.*;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.util.Date;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Subjects.SubjectsListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{


    // Always remember to follow a naming scheme. The global variables should have a prefix, most commonly used one is m
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDataBase = firebaseDatabase.getReference(); //Firebase Reference
    private icon_Manager mIconManager; // Icon manager object, for adding glyphs
    private Toolbar mToolbar; // Toolbar object for the top toolbar
    private DrawerLayout mDrawerLayout; // Layout object for the navigation menu
    private NavigationView mNavigationView; // The navigation view
    private TextView notice; // The notice text view to show what important notifs we have
    private CircleButton mAddBtn; // Button to add new reminders
    private int mIndex=-1; //works as counter and flag for database todo : remove if not necessary
    private DatabaseReference mIndex_db = firebaseDatabase.getReference().child("mIndex");  //to update mIndex value
    private String date=null,time=null,rem_text=null,notice_string=null;  //date of rem, time of rem , notice in notice board
    //Database always keeps mIndex value to know how many entires are there, which is used later by custom algortihms
    //mIndex is intialised at -1 when the user has no reminders. So when authorising for each user node maintain mIndex value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //list of reminders //only shows top 10 rems and if less are there, replaced by quotes
        mIndex_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String x = dataSnapshot.getValue().toString();
                    if (!x.equals("-1")) {
                        int y = Integer.parseInt(x);
                        int z = 10;
                        while (y != -1 && z != 0) {
                            final int zen = z;

                            DatabaseReference yi = firebaseDatabase.getReference().child(Integer.toString(y));
                            final DatabaseReference yi_removal = yi;
                            yi.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) { //easy to understand logic , DO NOT meddle if you dont understand
                                    if (dataSnapshot.exists()) {
                                        TextView tv = new TextView(MainActivity.this);
                                        switch (zen) {
                                            case 10:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv10);
                                                break;
                                            case 9:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv9);
                                                break;
                                            case 8:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv8);
                                                break;
                                            case 7:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv7);
                                                break;
                                            case 6:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv6);
                                                break;
                                            case 5:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv5);
                                                break;
                                            case 4:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv4);
                                                break;
                                            case 3:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv3);
                                                break;
                                            case 2:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv2);
                                                break;
                                            case 1:
                                                tv = (TextView) findViewById(R.id.mainscreen_tv1);
                                                break;
                                        }
                                        String yi_text = dataSnapshot.getValue().toString();
                                        String yi_trim = yi_text.substring(11, yi_text.length());
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
                                        String strDate = mdformat.format(calendar.getTime());
                                        String yolo = "<p>" +"<b>"+ yi_trim.substring(0, yi_trim.length() - 6)+"</b" + "</p>" + "<p>" +"<b>"+ yi_text.substring(0, 10) +"</b>"+ "</p>";
                                        if (yi_text.substring(0, 10).equals(strDate) && !time_comp(yi_trim.substring(yi_trim.length() - 5, yi_trim.length()))) {
                                            final int yi_remove_ind = Integer.parseInt(yi_removal.getKey());
                                            mIndex_db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        mIndex_db.setValue(Integer.toString(yi_remove_ind - 1));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            //Toast.makeText(MainActivity.this,yi_remove_ind,Toast.LENGTH_LONG).show();
                                            yi_removal.removeValue();
                                            Intent i5 = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(i5);

                                        } else {
                                            tv.setText(Html.fromHtml(yolo));
                                            tv.setBackgroundColor(Color.rgb(125, 224, 175));
                                        }
                                    }
                                    }


                                    @Override
                                    public void onCancelled (DatabaseError databaseError){

                                    }

                                });
                            y--;
                            z--;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // TODO: check current date, remove the data with key = mIndex( bottom - most urgent)


        setContentView(R.layout.navigation_drawer);
        mToolbar = (Toolbar) findViewById(R.id.topToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mAddBtn = (CircleButton) findViewById(R.id.addBtn);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mDrawerLayout.removeDrawerListener(toggle);


        // Compatibility mode and toolbar-actionbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(10.f);
        }



        //notice_board
        mIconManager=new icon_Manager();
        notice=((TextView) findViewById(R.id.notice));
        notice.setTypeface(mIconManager.get_icons(
                "fonts/ionicons.ttf",this
        ));
        DatabaseReference notice_db=firebaseDatabase.getReference().child("mIndex");
        final DatabaseReference notice_text=firebaseDatabase.getReference();
        notice_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // logic for noticeboard to display most urgent from database
                if (dataSnapshot.exists()) {
                    String in_value = dataSnapshot.getValue().toString();
                    final String for_date = Integer.toString(Integer.parseInt(in_value)-1);
                    if (!in_value.equals("-1")) {
                        if (in_value.equals("0")) {
                            notice_text.child(in_value).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String x = dataSnapshot.getValue().toString();
                                        notice_string = x.substring(11, x.length() - 6);
                                        char icon = notice.getText().charAt(0);
                                        String ic = Character.toString(icon);
                                        String s = ic + "<font color=##FD971F><b> Important Notice</b></font><p>" + notice_string + "</p>";
                                        notice.setText(Html.fromHtml(s));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else{
                            //pass recent two urgent strings to proximity
                            notice_text.child(in_value).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        final String x1 = dataSnapshot.getValue().toString();
                                        final String datex1 = x1.substring(0,10);
                                        notice_text.child(for_date).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    final String x2 = dataSnapshot.getValue().toString();
                                                    final String datex2 = x2.substring(0,10);
                                                    if(!proximity(x2,x1)){
                                                        proximity(x2,x1);
                                                        char icon = notice.getText().charAt(0);
                                                        String ic = Character.toString(icon);
                                                        String s = ic + "<font color=##FD971F><b> Important Notice</b></font><p>" + notice_string + "</p>";
                                                        notice.setText(Html.fromHtml(s));

                                                    }else{
                                                        notice_string=x1.substring(11,x1.length()-5);
                                                        char icon = notice.getText().charAt(0);
                                                        String ic = Character.toString(icon);
                                                        String s = ic + "<font color=##FD971F><b> Important Notice</b></font><p>" + notice_string + "</p>";
                                                        notice.setText(Html.fromHtml(s));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //if it returns true no need to do anything
                            //else assign a new date and stuff
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //end of notice board


        //Add reminder in main screen
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                final DatePicker rem_date=(DatePicker) lview.findViewById(R.id.datePicker4);
                final TimePicker rem_time=(TimePicker)lview.findViewById(R.id.timePicker);
                mBuilder.setView(lview);
                AlertDialog rem_dialog=mBuilder.create();
                rem_dialog.show();
                Button submit_date=(Button) lview.findViewById(R.id.submit_date);
                submit_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int day=0;
                        int month=0;
                        int year=0;
                        int hour=0;
                        int min=0;
                        onDateSet(rem_date,day,month,year);
                        onTimeSet(rem_time,hour,min);
                        rem_text=lReminder.getText().toString().trim();
                        //algorithm for priority
                        prioritise(date+"_"+rem_text+"_"+time);
                        Intent i5=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i5);
                    }

                });


            }
        });


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
    //prioritising algorithm , DO NOT meddle if you dont understand
    public void prioritise(String d){
        final String d_in=d;
        mIndex_db.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String x = dataSnapshot.getValue().toString();
                    mDataBase.child("mIndex").setValue(Integer.parseInt(x) + 1);
                    if (!x.equals("-1")) {
                        int y = Integer.parseInt(x);
                        while (y != -1) {
                            final int y_in = y + 1;
                            final int y_ex = y;
                            DatabaseReference yi = firebaseDatabase.getReference().child(Integer.toString(y));
                            yi.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String z = dataSnapshot.getValue().toString();
                                    String z_date = z.substring(0, 10);
                                    //call compare date method to return true if argument is more urgent else false
                                    if ((date_comp(d_in.substring(0, 10), z_date) || d_in.substring(0, 10).equals(z_date))) { //when d_in is earlier or equal
                                        if(d_in.substring(0,10).equals(z_date)){
                                            mDataBase.child(Integer.toString(y_in)).setValue(z);
                                            mDataBase.child(Integer.toString(y_ex)).setValue(d_in);
                                        }else {
                                            mDataBase.child(Integer.toString(y_in)).setValue(d_in);
                                            try
                                            {
                                                Thread.sleep(1000);
                                            }
                                            catch (InterruptedException e)
                                            {
                                                e.printStackTrace();
                                            }

                                            System.exit(0);
                                        }

                                    } else {
                                        mDataBase.child(Integer.toString(y_in)).setValue(z);
                                        mDataBase.child(Integer.toString(y_ex)).setValue(d_in);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            y--;
                        }

                    } else {
                        mDataBase.child("0").setValue(date + "_" + rem_text + "_" + time);
                    }


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
        finish();
    }
    //end of prioritising algorithm
    //end of dealing with reminder


    //method to compare dates
    public boolean date_comp(String date1,String date2){ //false if date1 is later
        int year1=Integer.parseInt(date1.substring(6,10));
        int year2=Integer.parseInt(date2.substring(6,10));
        if(year1>year2){
            return false;
        }else if(year1<year2){
            return true;
        }else{
            int month1=Integer.parseInt(date1.substring(3,5));
            int month2=Integer.parseInt(date2.substring(3,5));
            if(month1>month2){
                return false;
            }else if(month1<month2){
                return true;
            }else{
                int day1=Integer.parseInt(date1.substring(0,2));
                int day2=Integer.parseInt(date2.substring(0,2));
                if(day1>day2){
                    return false;
                }else
                    return true;
            }
        }
    }
    //method to compare with current time
    public boolean time_comp(String t_i){
        Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime=cal.getTime();
        DateFormat d_t=new SimpleDateFormat("HH:mm");
        d_t.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime=d_t.format(currentLocalTime);
        Toast.makeText(MainActivity.this,localTime,Toast.LENGTH_LONG).show();
        if(Integer.parseInt(t_i.substring(0,2))>Integer.parseInt(localTime.substring(0,2))){
            return true;
        }else if(Integer.parseInt(t_i.substring(0,2))==Integer.parseInt(localTime.substring(0,2))){
            if(Integer.parseInt(t_i.substring(3,5))>=Integer.parseInt(localTime.substring(3,5))){
                return true;
            }
        }
        return false;

    }
    //proximity algorithm
    public boolean proximity(String s1,String s2){ //first param: lower urgent date , second param: urgent date //false if conflict
        if(date_proximity(s1.substring(0,10),s2.substring(0,10))) {
            Pattern p = Pattern.compile("[Ee][Xx][Aa][Mm]");
            Matcher m = p.matcher(s1);
            int check_exam_s1 = 0;
            while (m.find()){
                    check_exam_s1 = 1;
                break;
            }
            p = Pattern.compile("[Aa][Ss][Ss][Ii][Gg][Nn][Mm][Ee][Nn][Tt]");
            m = p.matcher(s2);
            int check_assgn_s2 = 0;
            while (m.find()) {
                    check_assgn_s2 = 1;
                break;
            }
            if (check_exam_s1 == 1 && check_assgn_s2 == 1) {
                notice_string = getString(R.string.clashAssExm);
                return false;
            }
            p = Pattern.compile("[Cc][Oo][Nn][Tt][Ee][Ss][Tt]");
            m = p.matcher(s2);
            int check_contest_s2 = 0;
            while (m.find()) {
                    check_contest_s2 = 1;
                break;
            }
            if (check_exam_s1 == 1 && check_contest_s2 == 1) {
                notice_string = getString(R.string.clashEventExm);
                return false;
            }
            p = Pattern.compile("[Aa][Ss][Ss][Ii][Gg][Nn][Mm][Ee][Nn][Tt]");
            m = p.matcher(s1);
            int check_assgn_s1 = 0;
            while (m.find()) {
                    check_assgn_s1 = 1;
                break;
            }
            if (check_assgn_s1 == 1 && check_contest_s2 == 1) {
                notice_string = getString(R.string.clashEventAss);
                return false;
            }
            if(check_exam_s1==1){
                notice_string = getString(R.string.clashExmEverything);
                return false;
            }
            if(check_assgn_s1==1){
                notice_string = "Assignments needs to be done now, stuff to do";
                return false;
            }
            //TODO: Add more conditions to check conflicts if any //recommended
        }
        return true;
    }
    public boolean date_proximity(String date1,String date2){ //returns true if there is a conflict in 40hrs
        DateFormat formatter;
        Date date_1=new Date();
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date_1 = formatter.parse(date1);
            SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = mdformat.format(date_1);
        }catch(Exception e){}
        Date date_2=new Date();
        try{
            date_2 = formatter.parse(date2);
            SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = mdformat.format(date_2);
        }catch(Exception e){}
        long duration=date_1.getTime()-date_2.getTime();
        long diffInHours=Math.abs(TimeUnit.MILLISECONDS.toHours(duration));
        if(diffInHours<=48){
            return true;
        }
        return false;
    }


    //adds subjects dynamically to drawer
    public void addSub(String str) {
        if(str.equals("") || str.equals(" "))
        {
            Toast.makeText(this, "Please Add Subject!", Toast.LENGTH_SHORT).show();

        }
        else{
            mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
            mNavigationView.setNavigationItemSelectedListener(this);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            Menu menu = mNavigationView.getMenu();
            menu.add(str);
            Toast.makeText(this, "Subject Added!", Toast.LENGTH_SHORT).show();
        }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Todo : Add some required functionality here
        int id = item.getItemId();
        switch (id) {
            case R.id.dropDown_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.dropDown_aboutUs:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
// Dialog box for subject addition to drawer
    public void openDialog()
    {
        Subjects ob=new Subjects();
        ob.show(getSupportFragmentManager(), "example dialog");
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_sub:
                this.openDialog();
                break;
            case R.id.exam:
                startActivity(new Intent(this, exams.class));
                break;
            case R.id.ass:
//                TODO : Logic for assignment
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
        }
            mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
