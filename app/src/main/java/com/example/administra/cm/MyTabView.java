package com.example.administra.cm;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by pljay on 2017/3/16.
 */

public class MyTabView extends TextView {
    public int index;
    public MyTabView(Context context){
        super(context);
    }
    public MyTabView(Context context,int index){
        super(context);
        this.index=index;
    }
}
