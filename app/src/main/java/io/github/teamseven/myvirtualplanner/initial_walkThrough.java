package io.github.teamseven.myvirtualplanner;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class initial_walkThrough extends AppCompatActivity {

    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private SliderAdapter mSliderAdapter;
    private TextView [] mDots;
    private Button mNextBtn;
    private Button mPrevBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_initial_walk_through);
        mViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mLinearLayout = (LinearLayout) findViewById(R.id.dotLayout);
        mSliderAdapter = new SliderAdapter(this);
        mViewPager.setAdapter(mSliderAdapter);

        mNextBtn = (Button) findViewById(R.id.nextBtn);
        mPrevBtn = (Button) findViewById(R.id.prevBtn);

        addDotIndicator(0);

        mViewPager.addOnPageChangeListener(viewListener);
    }

    public void addDotIndicator(int position) {
        mDots = new TextView[3];
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.grey));

            mLinearLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
            mCurrentPage = position;
            if (mCurrentPage == 0) {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
            } else if (position == mDots.length - 1) {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mNextBtn.setText("Finish");
                mPrevBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
            } else {
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
