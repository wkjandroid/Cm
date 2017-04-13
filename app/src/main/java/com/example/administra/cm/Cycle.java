package com.example.administra.cm;

import android.media.AudioDeviceInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administra on 2017/3/4.
 */

public class Cycle extends AppCompatActivity {
    private List<ImageView> views=new ArrayList<ImageView>();
    private List<AudioDeviceInfo> infos=new ArrayList<AudioDeviceInfo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
