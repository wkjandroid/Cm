package com.example.administra.cm.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administra.cm.MyIdicator;
import com.example.administra.cm.R;

import com.example.administra.cm.activity.LoginActivity;
import com.example.administra.cm.activity.Ownspace;
import com.example.administra.cm.activity.RegisterActivity;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.po.User;
import com.example.administra.cm.util.ActivityCollector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administra on 2017/4/10.
 */

public class IndexFragment extends Fragment {
    private View mview;
    private MyApplication myApplication;
    private DrawerLayout mDrawerLayout;

    private TextView T_information;
    private MyIdicator indicator;
    private ViewPager pager;
    String content[]={"摇滚","民谣","伤感","友情","爱情","亲情","bigboom"};

    private  static ViewPager viewPager;
    private RadioGroup group;
    private  int[] images={R.drawable.first,R.drawable.secondimage,R.drawable.thirdimage,R.drawable.fourimage};
    private List<ImageView> mList;
    private static int index=0,preIndex=0;
    private boolean isContinue=true;
    private Timer timer=new Timer();
    private Myhandler myhandler;
    private AppCompatActivity activity;




    public static class Myhandler extends Handler {
        private WeakReference<Activity> weakReference;

        public Myhandler(Activity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                index++;
                viewPager.setCurrentItem(index);
            }
            super.handleMessage(msg);
        }
    }
  private int i=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mview==null){
        mview=inflater.inflate(R.layout.activity_main,container,false);
        }
        i=1;
        activity= (AppCompatActivity) getActivity();

        ActivityCollector.activityList.add(activity);


        FragmentPagerAdapter adapter=new MyFragmentManager(activity.getSupportFragmentManager(),content);
        pager=(ViewPager)mview.findViewById(R.id.pager);
        indicator=(MyIdicator) mview.findViewById(R.id.indicator);
        if(pager.getAdapter()==null) {
            pager.setAdapter(adapter);
        }
        indicator.setViewPager(pager);


        initView();
        initData();
        addListener();

        startSwitch();




        return mview;
    }





    private void initView() {
        viewPager=(ViewPager)mview.findViewById(R.id.viewpager);

    }
    private void startSwitch() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isContinue){
                    myhandler.sendEmptyMessage(1);
                }
            }
        },10000,15000);
    }
    View.OnTouchListener ontouchListener=new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue=false;
                    break;
                default:isContinue=true;
            }
            return false;
        }
    };
    ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            index=position;
            /*setCurrentDot(index % images.length);*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((ViewGroup)mview.getParent()).removeView(mview);
    }

PagerAdapter pagerAdapter=new PagerAdapter() {
    @Override
    public int getCount() {
        return  Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position=position %images.length;
        ImageView imageview=new ImageView(activity);
        imageview.setImageResource(images[position]);
        imageview.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageview);
        mList.add(imageview);
        return imageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
};

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            preIndex=0;
            timer.cancel();
        }
    }

    private void addListener() {
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setOnTouchListener(ontouchListener);
    }

    private void initData() {
        mList=new ArrayList<>();
        viewPager.setAdapter(pagerAdapter);
        myhandler=new IndexFragment.Myhandler(activity);
    }








   /* public void acticity.onBackPressed() {

        if (ActivityCollector.activityList.size()==1){
            AlertDialog alertDialog=new AlertDialog.Builder(activity).setMessage("将会退出程序！！！")
                    .setCancelable(false).setTitle("提示")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    }).create();
            alertDialog.show();
        }
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return true;
    }*/
}
