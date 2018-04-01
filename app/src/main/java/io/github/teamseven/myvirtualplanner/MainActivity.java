package io.github.teamseven.myvirtualplanner;

/**
 * Created by teamseven
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
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

import java.util.Calendar;

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
    private int mIndex=-1; //works as counter and flag for database
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
                String x=dataSnapshot.getValue().toString();
                if(!x.equals("-1")){
                    int y=Integer.parseInt(x);
                    int z=10;
                    while(y!=-1&&z!=0){
                        final int zen=z;

                        DatabaseReference yi=firebaseDatabase.getReference().child(Integer.toString(y));
                        yi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) { //easy to understand logic , DO NOT meddle if you dont understand
                                TextView tv=new TextView(MainActivity.this);
                                switch(zen){
                                    case 10 : tv=(TextView)findViewById(R.id.mainscreen_tv10);
                                        break;
                                    case 9  : tv=(TextView)findViewById(R.id.mainscreen_tv9);
                                        break;
                                    case 8  : tv=(TextView)findViewById(R.id.mainscreen_tv8);
                                        break;
                                    case 7  : tv=(TextView)findViewById(R.id.mainscreen_tv7);
                                        break;
                                    case 6  : tv=(TextView)findViewById(R.id.mainscreen_tv6);
                                        break;
                                    case 5  : tv=(TextView)findViewById(R.id.mainscreen_tv5);
                                        break;
                                    case 4  : tv=(TextView)findViewById(R.id.mainscreen_tv4);
                                        break;
                                    case 3  : tv=(TextView)findViewById(R.id.mainscreen_tv3);
                                        break;
                                    case 2  : tv=(TextView)findViewById(R.id.mainscreen_tv2);
                                        break;
                                    case 1  : tv=(TextView)findViewById(R.id.mainscreen_tv1);
                                        break;
                                }
                                String yi_text=dataSnapshot.getValue().toString();
                                String yi_trim=yi_text.substring(11,yi_text.length());
                                String yolo="<p>"+yi_trim+"</p>"+"<p>"+yi_text.substring(0,10)+"</p>";
                                tv.setText(Html.fromHtml(yolo));
                                tv.setBackgroundColor(Color.rgb(125,224,175));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        y--;
                        z--;
                    }
                }
            }
            //end of handling list of reminders
            // TODO: check current date, remove the data with key = mIndex( bottom - most urgent)


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


        // Compatibility mode
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
                String in_value=dataSnapshot.getValue().toString();
                if(!in_value.equals("-1")){
                    notice_text.child(in_value).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String x=dataSnapshot.getValue().toString();
                            notice_string=x.substring(11,x.length());
                            char icon=notice.getText().charAt(0);
                            String ic=Character.toString(icon);
                            String s=ic+"<font color=##FD971F><b> Important Notice</b></font><p>"+notice_string+"</p>";
                            notice.setText(Html.fromHtml(s));
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
        //end of notice board


        //Add reminder in main screen
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("circle", "onClick: Add btn clicked");
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                String text_rem=lReminder.getText().toString();
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
                        prioritise(date+"_"+rem_text);
                        Intent i5=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i5);
                    }

                });


            }
        });


    }


    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        Toast.makeText(MainActivity.this,date,Toast.LENGTH_SHORT).show();

    }
    public String theDate(int i){ //for formatting of date
        String i_s=Integer.toString(i);
        if(i_s.length()==1){
            i_s="0"+i_s;
        }
        return i_s;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) { //not needed for project now,may become important later
        Calendar calendar=Calendar.getInstance();
        i=timePicker.getCurrentHour();
        i1=timePicker.getCurrentMinute();
        time=Integer.toString(i)+":"+Integer.toString(i1);
        Toast.makeText(MainActivity.this,time,Toast.LENGTH_LONG).show();

    }
    //prioritising algorithm , DO NOT meddle if you dont understand
    public void prioritise(String d){
        final String d_in=d;
        mIndex_db.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String x=dataSnapshot.getValue().toString();
                if(!x.equals("-1")){
                    int y=Integer.parseInt(x);
                    while(y!=-1) {
                        final int y_in=y+1;
                        final int y_ex=y;
                        DatabaseReference yi = firebaseDatabase.getReference().child(Integer.toString(y));
                        yi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String z=dataSnapshot.getValue().toString();
                                String z_date=z.substring(0,10);
                                //call compare date method to return true if argument is more urgent else false
                                if(date_comp(d_in.substring(0,10),z_date)){ //when d_in is earlier
                                    mDataBase.child(Integer.toString(y_in)).setValue(d_in);

                                }else{
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

                }else{
                    mDataBase.child("0").setValue(date+"_"+rem_text);
                    finish();
                }
                mDataBase.child("mIndex").setValue(Integer.parseInt(x)+1);
                finish();
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
            case R.id.dropDown_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dropDown_login:
                Toast.makeText(this, "Log in / Sign up", Toast.LENGTH_SHORT).show();
                break;
            case R.id.dropDown_aboutUs:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;

//                TODO : A few more items need to be added. There will be no nav bar in the bottom and all of them will be included here.
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
        boolean check=true;
        switch (id) {
            case R.id.add_sub:
//                Work : Subject Addition Done!
                this.openDialog();
                break;
            case R.id.exam:
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, exams.class));
                break;
            case R.id.ass:
//                TODO : Logic for assignment
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personal:
//                TODO : Open a new activity containing all the things related to personal. *hint* Intent
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.misc:
//                TODO : New activity for all things misc using Intent
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.sub_1:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_2:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_3:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_4:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.sub_5:
////                TODO : open subject page
//                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
//                break;


//                Todo : Add all the other things that are needed in the navigation bar and then implement them here

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
