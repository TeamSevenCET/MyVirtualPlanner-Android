package io.github.teamseven.myvirtualplanner;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        simulateDayNight(/* night */ 1);

        Element btn = new Element();
        btn.setTitle("OK");
        btn.setIconDrawable(R.drawable.ic_ok);
        btn.setGravity(Gravity.CENTER);
        btn.setIconNightTint(android.R.color.white);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(AboutUs.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.icn_img)
                .setDescription("We are students of CET, BBSR. This app is our project. The main idea of this project is it's a TODO app specifically tailored for students. Try it out and let us know, what you feel and how we can improve this further.")
                .addGroup("Connect with us")
                .addEmail("elmehdi.sakout@gmail.com")
                .addGitHub("TeamSevenCET")
                .addItem(getCopyRightsElement())
                .addItem(btn)
                .create();

        setContentView(aboutPage);
    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format("Copyright", Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_copyright);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUs.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }



    void simulateDayNight(int currentSetting) {

        final int DAY = 0;

        final int NIGHT = 1;

        final int FOLLOW_SYSTEM = 3;



        int currentNightMode = getResources().getConfiguration().uiMode

                & Configuration.UI_MODE_NIGHT_MASK;

        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {

            AppCompatDelegate.setDefaultNightMode(

                    AppCompatDelegate.MODE_NIGHT_NO);

        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {

            AppCompatDelegate.setDefaultNightMode(

                    AppCompatDelegate.MODE_NIGHT_YES);

        } else if (currentSetting == FOLLOW_SYSTEM) {

            AppCompatDelegate.setDefaultNightMode(

                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        }

    }
}
