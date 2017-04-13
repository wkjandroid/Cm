package com.example.administra.cm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administra.cm.Adapter.ShowAdapter;
import com.example.administra.cm.UploadsActivity;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.R;
import com.example.administra.cm.po.CmFile;
import com.example.administra.cm.po.ShowOwn;
import com.example.administra.cm.po.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administra on 2017/3/7.
 */

public class Ownspace extends AppCompatActivity {
  private MyApplication myApplication;
    private byte[] file1=null;
   private int [] backg={R.drawable.audio,R.drawable.vedio,R.drawable.word,R.drawable.picture};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ownplacelayout);
        CircleImageView iv=(CircleImageView)findViewById(R.id.own_image);

        iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                   Intent intent=new Intent(Ownspace.this,UploadsActivity.class);
                startActivity(intent);
            }
        });
        Bitmap bm;
        String username;
        ShowOwn showOwn=null;
        List<ShowOwn> Slist=new ArrayList<>();
        Random ne=new Random();
        User user=MyApplication.getUser();
        if(user!=null) {
            Set<CmFile> cmFiles = user.getCmFiles();
            if (cmFiles != null) {
                Iterator<CmFile> iterator = cmFiles.iterator();
                while (iterator.hasNext()) {
                    CmFile file = iterator.next();
                    file1 = file.getFile();
                    if (file1!=null) {
                        bm = BitmapFactory.decodeByteArray(file1, 0, file1.length);
                        showOwn=new ShowOwn(R.drawable.commeter,user.getUsername(),bm,backg[ne.nextInt(3)]);
                        Slist.add(showOwn);
                    }
                }
                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                ShowAdapter showAdapter=new ShowAdapter(Slist);
                recyclerView.setAdapter(showAdapter);
            }


        }


    }
}

