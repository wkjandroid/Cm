package com.example.administra.cm.po;

import android.app.Application;
import android.content.Context;

import com.example.administra.cm.po.User;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private Context context;
    private static User user;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);
    }

    public  static User getUser() {
        return user;
    }

    public  static void setUser(User l_user) {

        user=l_user;
    }
}
