package com.example.administra.cm.util;

import com.google.gson.Gson;

public class GsonUtil {
    private static GsonUtil instance=null;
    private Gson gson;
    private GsonUtil(){
       gson=new Gson();
    }
    public static GsonUtil getInstance(){
        return (instance==null)?new GsonUtil():instance;
    }
    public Gson getGson(){
        return gson;
    }
}
