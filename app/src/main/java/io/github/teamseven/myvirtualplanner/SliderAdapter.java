package io.github.teamseven.myvirtualplanner;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter (Context context) {
        this.context = context;
    }

    public int[] slide_images = {
        R.drawable.group_twelve,
        R.drawable.group_eleven,
        R.drawable.group_ten
    };

    public String[] headings = {
      "Study",
      "Intelligent",
      "Enjoy"
    };

    public String[] descriptions = {
      "Using this app you can concentrate more on studies. With an emphasis on students our app is built on the principle of time is money.",
            "Our app uses an intelligent algorithm to determine what is important at the moment and suggests you on what to do next. With this feature you will always be on top of whatever this college life throws at you.",
            "With this app you can not only give more time to your studies but you can actually enjoy your college life. A moment that is saved from studying can now be used to enjoy, go out with friends, or play video games because you lack any. Choice is yours."
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.topIcon);
        TextView title = (TextView) view.findViewById(R.id.heading);
        TextView desc = (TextView) view.findViewById(R.id.description);

        imageView.setImageResource(slide_images[position]);
        title.setText(headings[position]);
        desc.setText(descriptions[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);

    }
}
