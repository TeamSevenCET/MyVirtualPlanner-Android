package io.github.teamseven.myvirtualplanner;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    icon_Manager icon_manager;
    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    TextView notice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        mToolbar = (Toolbar) findViewById(R.id.topToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

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
        icon_manager=new icon_Manager();
        notice=((TextView) findViewById(R.id.notice));
        notice.setTypeface(icon_manager.get_icons(
                "fonts/ionicons.ttf",this
        ));
        char icon=notice.getText().charAt(0);
        String ic=Character.toString(icon);
        String s=ic+"<font color=#f94d18><b> Important Notice</b></font>";
        notice.setText(Html.fromHtml(s));
        // Todo : Add functionality to display required string in notice box

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_sub:
//                TODO : Add a subject
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exam:
//                TODO : Add logic for exam
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
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
            case R.id.sub_1:
//                TODO : open subject page
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub_2:
//                TODO : open subject page
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub_3:
//                TODO : open subject page
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub_4:
//                TODO : open subject page
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sub_5:
//                TODO : open subject page
                Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
                break;


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
