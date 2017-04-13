package com.example.administra.cm.po;

import android.graphics.Bitmap;

/**
 * Created by Administra on 2017/3/28.
 */

public class ShowOwn {
    private int TouID;
    private String name;
    private Bitmap ImageID;
    private int LayoutID;
    public ShowOwn(int TouID,String name,Bitmap ImageID,int LayoutID){
        this.LayoutID=LayoutID;
       this.TouID=TouID;
       this.name=name;
       this.ImageID=ImageID;
   }

    public int getLayoutID() {
        return LayoutID;
    }

    public int getTouID() {
        return TouID;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImageID() {
        return ImageID;
    }
}
