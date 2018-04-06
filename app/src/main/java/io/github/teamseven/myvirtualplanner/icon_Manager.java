package io.github.teamseven.myvirtualplanner;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;


public class icon_Manager {
    private static Hashtable<String,Typeface> cached_icons = new Hashtable<>();
    public static Typeface get_icons(String path,Context context){
        Typeface icons=cached_icons.get(path);
        if(icons==null){
            icons=Typeface.createFromAsset(context.getAssets(),path);
        }
        return icons;
    }

}
