package com.example.administra.cm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administra.cm.Adapter.ShowAdapter;
import com.example.administra.cm.R;
import com.example.administra.cm.UploadsActivity;
import com.example.administra.cm.activity.Ownspace;
import com.example.administra.cm.po.CmFile;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.po.ShowOwn;
import com.example.administra.cm.po.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administra on 2017/4/11.
 */

public class OwnFragment extends Fragment {
    private View mview;
   private Activity activity;
    private MyApplication myApplication;
    private byte[] file1=null;
    private int [] backg={R.drawable.audio,R.drawable.vedio,R.drawable.word,R.drawable.picture};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mview==null){
            mview=inflater.inflate(R.layout.ownplacelayout,container,false);
        }
        activity=getActivity();

        CircleImageView iv=(CircleImageView)mview.findViewById(R.id.own_image);

        iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                activity.finish();
                Intent intent=new Intent(activity,UploadsActivity.class);
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
                RecyclerView recyclerView=(RecyclerView)mview.findViewById(R.id.recyclerview);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(linearLayoutManager);
                ShowAdapter showAdapter=new ShowAdapter(Slist);
                recyclerView.setAdapter(showAdapter);
            }


        }

        return mview;
    }
}
