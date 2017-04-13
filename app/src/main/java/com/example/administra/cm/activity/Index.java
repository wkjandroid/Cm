package com.example.administra.cm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.fragment.MyFragmentManager;
import com.example.administra.cm.MyIdicator;
import com.example.administra.cm.R;
import com.example.administra.cm.po.User;
import com.example.administra.cm.util.ActivityCollector;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Index extends AppCompatActivity {
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
    public static class Myhandler extends Handler {
        private WeakReference<Index> weakReference;

        public Myhandler(Index activity) {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.activityList.add(this);
        FragmentPagerAdapter adapter=new MyFragmentManager(getSupportFragmentManager(),content);
        pager=(ViewPager)findViewById(R.id.pager);
        indicator=(MyIdicator) findViewById(R.id.indicator);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        /*NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        RelativeLayout headerLayout=(RelativeLayout) navView.inflateHeaderView(R.layout.nav_header);
        T_information=(TextView)headerLayout.findViewById(R.id.information);
        String information=null;
        myApplication=((MyApplication)getApplication()==null)?null:(MyApplication)getApplication();
        User user=myApplication.getUser();
        if(user!=null){
        int intent=user.getIntention();


            information=user.getUsername();
            T_information.setText("ID:"+information);
        }else{
            T_information.setText("未登录333");
        }

        initView();
        initData();
        addListener();
       *//* viewPager.setCurrentItem(images.length*100);*//*
        *//*initRadioButton(images.length);*//*
        startSwitch();



            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nav_friends);


        }
        navView.setCheckedItem(R.id.login);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.login:
                        Intent intent1 = new Intent(Index.this, LoginActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.join:
                        Intent intent2 = new Intent(Index.this, RegisterActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.own:
                        *//*myApplication=((MyApplication)getApplication()==null)?null:(MyApplication)getApplication();
                        User user=myApplication.getUser();
                        if(user!=null){*//*
                        Intent intent3=new Intent(Index.this,Ownspace.class);
                        startActivity(intent3);*//*}else{
                            Toast.makeText(Index.this,"未登录，请登录", Toast.LENGTH_SHORT).show();
                            Intent intent4=new Intent(Index.this,LoginActivity.class);
                            startActivity(intent4);
                        }*//*
                        break;
                    default:

                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });
*/

    }

    private void startSwitch() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isContinue){
                    myhandler.sendEmptyMessage(1);
                }
            }
        },3000,3500);
    }

    /*private void initRadioButton(int length) {
        for(int i=0;i<length;i++){
            ImageView imageview=new ImageView(this);
            imageview.setImageResource(R.drawable.feature_point_cur);
            imageview.setPadding(0,0,20,0);
            group.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            group.getChildAt(0).setEnabled(false);
        }
    }*/

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

    /*private void setCurrentDot(int i) {
        if(group.getChildAt(i)!=null){
            group.getChildAt(i).setEnabled(false);
        }
        if(group.getChildAt(preIndex)!=null){
            group.getChildAt(preIndex).setEnabled(true);
            preIndex=i;
        }
    }*/
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
            ImageView imageview=new ImageView(Index.this);
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
    protected void onDestroy() {
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
        myhandler=new Myhandler(this);
    }

    private void initView() {
        viewPager=(ViewPager)findViewById(R.id.viewpager);

    }





    @Override
    public void onBackPressed() {

        if (ActivityCollector.activityList.size()==1){
            AlertDialog alertDialog=new AlertDialog.Builder(this).setMessage("将会退出程序！！！")
                    .setCancelable(false).setTitle("提示")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            alertDialog.show();
        }
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case android.R.id.home:
               mDrawerLayout.openDrawer(GravityCompat.START);
               break;

       }
        return true;
    }*/
}