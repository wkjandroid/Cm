package com.example.administra.cm.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wkj on 2017/3/25.
 */

public class ActivityCollector {
    public static List<Activity> activityList=new ArrayList<>();
    public static void addActivity(Activity activity){
        activityList.add(activity);
    }
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }
    public static void removeAllActivity(){
        activityList.clear();
    }
    
}
