package io.github.teamseven.myvirtualplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URL;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{


    // Always remember to follow a naming scheme. The global variables should have a prefix, most commonly used one is m
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDataBase = firebaseDatabase.getReference(); //Firebase Reference
    private icon_Manager mIconManager; // Icon manager object, for adding glyphs
    private Toolbar mToolbar; // Toolbar object for the top toolbar
    private DrawerLayout mDrawerLayout; // Layout object for the navigation menu
    private NavigationView mNavigationView; // The navigation view
    private TextView notice; // The notice text view to show what important notifs we have
    private CircleButton mAddBtn; // Button to add new reminders//works as counter and flag for database todo : remove if not necessary
    private DatabaseReference mIndex_db = firebaseDatabase.getReference().child("mIndex");  //to update mIndex value
    private DatabaseReference sIndex_db = firebaseDatabase.getReference().child("sIndex"); //for subs
    private DatabaseReference sub1_db;
    private DatabaseReference sub2_db;
    private DatabaseReference sub3_db;
    private DatabaseReference sub4_db;
    private DatabaseReference sub5_db;
    private DatabaseReference sub6_db;
    private DatabaseReference sub7_db;
    private DatabaseReference sub8_db;
    private Menu menu;
    private String date=null,time=null,rem_text=null,notice_string=null; //date of rem, time of rem , notice in notice board
    private String user_token="";

    //Database always keeps mIndex value to know how many entires are there, which is used later by custom algortihms
    //mIndex is intialised at -1 when the user has no reminders. So when authorising for each user node maintain mIndex value

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
        * Start of "You shall log in to pass"
        */
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){

            user_token=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        /**
         * End of "You Shall log in to pass with user_token"
         */

        final String user_token_inner=user_token;
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_token_inner)){
                    //do nothing
                }else{
                    mDataBase.setValue(user_token_inner);
                    mDataBase=firebaseDatabase.getReference().child(user_token_inner);
                    mDataBase.setValue("mIndex");
                    mDataBase.push().setValue("sub_1");
                    sub1_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_1");
                    sub1_db.setValue("    ");
                    mDataBase.push().setValue("sub_2");
                    sub2_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_2");
                    sub2_db.setValue("    ");
                    mDataBase.push().setValue("sub_3");
                    sub3_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_3");
                    sub3_db.setValue("    ");
                    mDataBase.push().setValue("sub_4");
                    sub4_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_4");
                    sub4_db.setValue("    ");
                    mDataBase.push().setValue("sub_5");
                    sub5_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_5");
                    sub5_db.setValue("    ");
                    mDataBase.push().setValue("sub_6");
                    sub6_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_6");
                    sub6_db.setValue("    ");
                    mDataBase.push().setValue("sub_7");
                    sub7_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_7");
                    sub7_db.setValue("    ");
                    mDataBase.push().setValue("sub_8");
                    sub8_db=firebaseDatabase.getReference().child(user_token_inner).child("sub_8");
                    sub8_db.setValue("    ");
                    mIndex_db=firebaseDatabase.getReference().child(user_token_inner).child("mIndex");
                    mIndex_db.setValue("-1");
                    mDataBase.push().setValue("sIndex");
                    sIndex_db=firebaseDatabase.getReference().child(user_token_inner).child("sIndex");
                    sIndex_db.setValue("0");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
 
            }
        });

        mDataBase=firebaseDatabase.getReference().child(user_token_inner);
        mIndex_db=firebaseDatabase.getReference().child(user_token_inner).child("mIndex");
        
        //Toast.makeText(MainActivity.this,mIndex_db.toString(),Toast.LENGTH_LONG).show();

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

                            DatabaseReference yi = firebaseDatabase.getReference().child(user_token).child(Integer.toString(y));
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
                                        String yolo = "<b>"+"<font color=\"#ffffff\">"+yi_trim.substring(0, yi_trim.length() - 6)+"</font>"+ "</b>"
                                                + "<p>"+"<font color=\"#ffffff\">" +yi_text.substring(0, 10) +"</font>"+ "</p>";
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
                                            tv.setBackgroundColor(Color.parseColor("#FEB63F"));
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
////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        menu=mNavigationView.getMenu();
        final MenuItem sub1=menu.findItem(R.id.sub1);
        DatabaseReference sub_local;
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_1");
            sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String x=dataSnapshot.getValue().toString();
                        sub1.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        final MenuItem sub2=menu.findItem(R.id.sub2);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_2");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub2.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MenuItem sub3=menu.findItem(R.id.sub3);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_3");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub3.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MenuItem sub4=menu.findItem(R.id.sub4);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_4");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub4.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MenuItem sub5=menu.findItem(R.id.sub5);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_5");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub5.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MenuItem sub6=menu.findItem(R.id.sub6);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_6");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub6.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MenuItem sub7=menu.findItem(R.id.sub7);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_7");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub7.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final MenuItem sub8=menu.findItem(R.id.sub8);
        sub_local=firebaseDatabase.getReference().child(user_token).child("sub_8");
        sub_local.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    sub8.setTitle(Html.fromHtml(x.substring(0,x.length()-3)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////



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
        DatabaseReference notice_db=mIndex_db;
        final DatabaseReference notice_text=mDataBase;
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
                                        String s = ic + "<font color=##FD971F><b> Important Notice</b></font><p>"+ "<br/>"+notice_string + "</p>";
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
                                                        notice_string=x1.substring(11,x1.length()-6);
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

    /**
     * The updateUI method can be used to update the UI as it retrieves the name, email and profile pic
     * from firebase and gives us nice simple to use strings. There is no problems there. But there is some problem
     * with the bloody navigation_header.xml as the error message says the target cannot be null. Let's see if this
     * is something I can fix within the deadline or I will just change the navigation header altogether.
     * */

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
        mIndex_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String x=dataSnapshot.getValue().toString();
                    mDataBase.child("mIndex").setValue(Integer.toString(Integer.parseInt(x) + 1));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
     mIndex_db.addListenerForSingleValueEvent((new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String x = dataSnapshot.getValue().toString();
                if (!x.equals("-1")) {
                    int y = Integer.parseInt(x);
                    Log.d("MyTest","Y outside loop: "+Integer.toString(y));
                    while (y > -1) {
                        final int y_in = y + 1;
                        final int y_ex = y;
                        Log.d("MyTest","Y inside loop: "+Integer.toString(y_ex));
                        DatabaseReference yi = firebaseDatabase.getReference().child(user_token).child(Integer.toString(y));
                        yi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    String z = dataSnapshot.getValue().toString();
                                    String z_date = z.substring(0, 10);
                                    Log.d("MyTest", "Y inside inner: " + Integer.toString(y_ex));
                                    //call compare date method to return true if argument is more urgent else false
                                    if ((date_comp(d_in.substring(0, 10), z_date) || d_in.substring(0, 10).equals(z_date))) { //when d_in is earlier or equal
                                        if (d_in.substring(0, 10).equals(z_date)) {
                                            mDataBase.child(Integer.toString(y_in)).setValue(z);
                                            mDataBase.child(Integer.toString(y_ex)).setValue(d_in);
                                        } else {
                                            mDataBase.child(Integer.toString(y_in)).setValue(d_in);
                                            try{
                                                Thread.sleep(1500);
                                            }catch(Exception e){
                                            }
                                            System.exit(0);
                                        }

                                    } else {
                                        mDataBase.child(Integer.toString(y_in)).setValue(z);
                                        mDataBase.child(Integer.toString(y_ex)).setValue(d_in);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        try{
                           Thread.sleep(1500);
                        }catch(Exception e){
                        }
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
                notice_string = getString(R.string.doAss);
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
                startActivity(new Intent(MainActivity.this, profile.class));
                break;
            case R.id.dropDown_aboutUs:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                // TODO : Priya add your about us page here
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_sub:
<<<<<<< HEAD
                try {
                    startActivity(new Intent(this, listofsubs.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
=======
                ////////////////////////////////////////////////////////////////////////////////////////////////////

                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                final View lview = getLayoutInflater().inflate(R.layout.subject_input,null);
                mBuilder.setView(lview);
                AlertDialog rem_dialog=mBuilder.create();
                rem_dialog.show();
                final Button sub_add=(Button) lview.findViewById(R.id.sub_add);
                sub_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("MyTest5","eneters");
                        TextView sub_input_text=(TextView)lview.findViewById(R.id.sub_input_text);
                        final String x_sub=sub_input_text.getText().toString().trim();
                        Log.d("MyTest5",x_sub);
                        sIndex_db=firebaseDatabase.getReference().child(user_token).child("sIndex");
                        sIndex_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Log.d("MyTest5","enters");
                                    int x=Integer.parseInt(dataSnapshot.getValue().toString());
                                    int y=x+1;
                                    if(y<=8) {
                                        Log.d("MyTest5",Integer.toString(y));
                                        String sub = "sub" + "_" + Integer.toString(y);
                                        DatabaseReference sub_local = firebaseDatabase.getReference().child(user_token).child(sub);
                                        sub_local.setValue(x_sub+"0/0");
                                        sIndex_db.setValue(y);
                                    }else{
                                        Toast.makeText(MainActivity.this,"Limit Reached",Toast.LENGTH_LONG).show();
                                    }

                                }
                                Intent i5 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i5);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
>>>>>>> 83a06af767dd6bd2f729723f9fae386df8cd6473
                break;
            case R.id.exam:
                startActivity(new Intent(this, exams.class));
                break;
            case R.id.ass:
//                TODO : Logic for assignment lets remove this altogether because it seems like a lot more work
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub1:
                AlertDialog.Builder mBuilder_sub=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub.setView(lview_sub);
                final AlertDialog rem_dialog_sub=mBuilder_sub.create();
                rem_dialog_sub.show();
                final Button sub_assgn=(Button) lview_sub.findViewById(R.id.sub_assgn);
                final Button sub_test=(Button) lview_sub.findViewById(R.id.sub_test);
                final Button sub_others=(Button) lview_sub.findViewById(R.id.sub_others);
                sub_assgn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_1");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_1");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_1");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub2:
                AlertDialog.Builder mBuilder_sub2=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub2 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub2.setView(lview_sub2);
                final AlertDialog rem_dialog_sub2=mBuilder_sub2.create();
                rem_dialog_sub2.show();
                final Button sub_assgn2=(Button) lview_sub2.findViewById(R.id.sub_assgn);
                final Button sub_test2=(Button) lview_sub2.findViewById(R.id.sub_test);
                final Button sub_others2=(Button) lview_sub2.findViewById(R.id.sub_others);
                sub_assgn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub2.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_2");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub2.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_2");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub2.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_2");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub3:
                AlertDialog.Builder mBuilder_sub3=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub3 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub3.setView(lview_sub3);
                final AlertDialog rem_dialog_sub3=mBuilder_sub3.create();
                rem_dialog_sub3.show();
                final Button sub_assgn3=(Button) lview_sub3.findViewById(R.id.sub_assgn);
                final Button sub_test3=(Button) lview_sub3.findViewById(R.id.sub_test);
                final Button sub_others3=(Button) lview_sub3.findViewById(R.id.sub_others);
                sub_assgn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub3.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_3");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub3.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_3");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub3.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_3");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub4:
                AlertDialog.Builder mBuilder_sub4=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub4 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub4.setView(lview_sub4);
                final AlertDialog rem_dialog_sub4=mBuilder_sub4.create();
                rem_dialog_sub4.show();
                final Button sub_assgn4=(Button) lview_sub4.findViewById(R.id.sub_assgn);
                final Button sub_test4=(Button) lview_sub4.findViewById(R.id.sub_test);
                final Button sub_others4=(Button) lview_sub4.findViewById(R.id.sub_others);
                sub_assgn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub4.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_4");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub4.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_4");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub4.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_4");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub5:
                AlertDialog.Builder mBuilder_sub5=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub5 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub5.setView(lview_sub5);
                final AlertDialog rem_dialog_sub5=mBuilder_sub5.create();
                rem_dialog_sub5.show();
                final Button sub_assgn5=(Button) lview_sub5.findViewById(R.id.sub_assgn);
                final Button sub_test5=(Button) lview_sub5.findViewById(R.id.sub_test);
                final Button sub_others5=(Button) lview_sub5.findViewById(R.id.sub_others);
                sub_assgn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub5.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_5");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub5.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_5");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub5.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_5");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub6:
                AlertDialog.Builder mBuilder_sub6=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub6 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub6.setView(lview_sub6);
                final AlertDialog rem_dialog_sub6=mBuilder_sub6.create();
                rem_dialog_sub6.show();
                final Button sub_assgn6=(Button) lview_sub6.findViewById(R.id.sub_assgn);
                final Button sub_test6=(Button) lview_sub6.findViewById(R.id.sub_test);
                final Button sub_others6=(Button) lview_sub6.findViewById(R.id.sub_others);
                sub_assgn6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub6.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_6");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub6.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_6");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub6.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_6");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub7:
                AlertDialog.Builder mBuilder_sub7=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub7 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub7.setView(lview_sub7);
                final AlertDialog rem_dialog_sub7=mBuilder_sub7.create();
                rem_dialog_sub7.show();
                final Button sub_assgn7=(Button) lview_sub7.findViewById(R.id.sub_assgn);
                final Button sub_test7=(Button) lview_sub7.findViewById(R.id.sub_test);
                final Button sub_others7=(Button) lview_sub7.findViewById(R.id.sub_others);
                sub_assgn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub7.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_7");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub7.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_7");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub7.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_7");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.sub8:
                AlertDialog.Builder mBuilder_sub8=new AlertDialog.Builder(MainActivity.this);
                final View lview_sub8 = getLayoutInflater().inflate(R.layout.subject_dialog,null);
                mBuilder_sub8.setView(lview_sub8);
                final AlertDialog rem_dialog_sub8=mBuilder_sub8.create();
                rem_dialog_sub8.show();
                final Button sub_assgn8=(Button) lview_sub8.findViewById(R.id.sub_assgn);
                final Button sub_test8=(Button) lview_sub8.findViewById(R.id.sub_test);
                final Button sub_others8=(Button) lview_sub8.findViewById(R.id.sub_others);
                sub_assgn8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub8.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_8");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Assignment"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_test8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub8.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_8");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Test"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                sub_others8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rem_dialog_sub8.dismiss();
                        AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                        View lview = getLayoutInflater().inflate(R.layout.dialog_reminder,null);
                        final EditText lReminder=(EditText) lview.findViewById(R.id.textReminder);
                        sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_8");
                        sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String x=dataSnapshot.getValue().toString();
                                    x=x.substring(0,x.length()-3);
                                    lReminder.setText(Html.fromHtml(x+" : Stuff"));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
                break;
            case R.id.attendance:
                AlertDialog.Builder mBuilder_attd=new AlertDialog.Builder(MainActivity.this);
                View lview_attd = getLayoutInflater().inflate(R.layout.attendance,null);
                final TextView attd_tv1=(TextView) lview_attd.findViewById(R.id.attd_tv1);
                final TextView attd_tv2=(TextView) lview_attd.findViewById(R.id.attd_tv2);
                final TextView attd_tv3=(TextView) lview_attd.findViewById(R.id.attd_tv3);
                final TextView attd_tv4=(TextView) lview_attd.findViewById(R.id.attd_tv4);
                final TextView attd_tv5=(TextView) lview_attd.findViewById(R.id.attd_tv5);
                final TextView attd_tv6=(TextView) lview_attd.findViewById(R.id.attd_tv6);
                final TextView attd_tv7=(TextView) lview_attd.findViewById(R.id.attd_tv7);
                final TextView attd_tv8=(TextView) lview_attd.findViewById(R.id.attd_tv8);
                final Button attd_submit=(Button) lview_attd.findViewById(R.id.attd_submit);
                final TextView attd_date=(TextView) lview_attd.findViewById(R.id.attd_date);
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                attd_date.setText(currentDateTimeString);
                mBuilder_attd.setView(lview_attd);
                AlertDialog rem_dialog_attd=mBuilder_attd.create();
                rem_dialog_attd.show();
                sub1_db=firebaseDatabase.getReference().child(user_token).child("sub_1");
                sub1_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv1.setText(Html.fromHtml(x));
                                attd_tv1.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv1.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub1_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv1.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv8.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv1.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv1.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub1_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv1.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv1.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                sub2_db=firebaseDatabase.getReference().child(user_token).child("sub_2");
                sub2_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv2.setText(Html.fromHtml(x));
                                attd_tv2.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv2.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub2_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv2.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv2.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv2.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv2.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub2_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv2.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv2.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                sub3_db=firebaseDatabase.getReference().child(user_token).child("sub_3");
                sub3_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv3.setText(Html.fromHtml(x));
                                attd_tv3.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv3.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub3_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv3.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv3.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv3.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv1.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub3_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv3.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv3.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                sub4_db=firebaseDatabase.getReference().child(user_token).child("sub_4");
                sub4_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv4.setText(Html.fromHtml(x));
                                attd_tv4.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv4.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub4_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv4.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv4.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv4.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv4.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub4_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv4.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv4.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                sub5_db=firebaseDatabase.getReference().child(user_token).child("sub_5");
                sub5_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv5.setText(Html.fromHtml(x));
                                attd_tv5.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv5.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub5_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv5.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv5.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv5.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv5.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub5_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv5.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv5.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                sub6_db=firebaseDatabase.getReference().child(user_token).child("sub_6");
                sub6_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv6.setText(Html.fromHtml(x));
                                attd_tv6.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv6.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub6_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv6.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv6.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv6.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv6.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub6_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv6.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv6.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                sub7_db=firebaseDatabase.getReference().child(user_token).child("sub_7");
                sub7_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv7.setText(Html.fromHtml(x));
                                attd_tv7.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv7.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub7_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv7.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv7.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                    }
                                });
                                attd_tv7.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv7.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub7_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv7.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv7.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent)+"%");
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                sub8_db=firebaseDatabase.getReference().child(user_token).child("sub_8");
                sub8_db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String x=dataSnapshot.getValue().toString();
                            final String y=x;
                            Log.d("MyTest6",x);
                            x="<b>"+x.substring(0,x.length()-3)+" "+x.substring(x.length()-3,x.length())+"</b>";

                            if(!x.equals("    ")){
                                attd_tv8.setText(Html.fromHtml(x));
                                attd_tv8.setBackgroundColor(Color.parseColor("#50000000"));
                                attd_tv8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        attd_tv8.setBackgroundColor(Color.parseColor("#63ff6c"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        num=Integer.toString(Integer.parseInt(num)+1);
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub8_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv8.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv8.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent+"%"));

                                    }
                                });
                                attd_tv8.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        attd_tv8.setBackgroundColor(Color.parseColor("#f71976"));
                                        Pattern p=Pattern.compile("[0-9]");
                                        Pattern r=Pattern.compile("/");
                                        Matcher m=p.matcher(y);
                                        Matcher n=r.matcher(y);
                                        int index_string_num=0;
                                        while(m.find()){
                                            index_string_num=m.start();
                                            break;
                                        }
                                        int index_bar=0;
                                        while(n.find()){
                                            index_bar=n.start();
                                        }
                                        String num=(y.substring(index_string_num,index_bar));
                                        String dnum=(y.substring(index_bar+1,y.length()));
                                        Log.d("MyTest7",(num));
                                        Log.d("MyTest7",(dnum));
                                        dnum=Integer.toString(Integer.parseInt(dnum)+1);
                                        sub8_db.setValue(y.substring(0,y.length()-3)+num+"/"+dnum);
                                        attd_tv8.setTextColor(Color.BLACK);
                                        float percent=Float.parseFloat(num)/Float.parseFloat(dnum)*100;
                                        attd_tv8.setText(y.substring(0,y.length()-3)+" "+num+"/"+dnum+"    "+String.format("%.2f",percent+"%"));
                                        return true;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                attd_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i5 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i5);

                    }
                });
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

}

