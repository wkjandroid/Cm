package com.example.administra.cm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.administra.cm.MyIdicator;
import com.example.administra.cm.R;
import com.example.administra.cm.fragment.IndexFragment;
import com.example.administra.cm.fragment.OwnFragment;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.po.Tab;
import com.example.administra.cm.po.User;
import com.example.administra.cm.util.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administra on 2017/4/11.
 */

public class FragmentActivity extends AppCompatActivity {
    private FragmentTabHost tabhost;
    private LayoutInflater mInflater;
    private List<Tab> list;
    private DrawerLayout mDrawerLayout;
    private TextView T_information;
    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allayout);

        initTab();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        RelativeLayout headerLayout=(RelativeLayout) navView.inflateHeaderView(R.layout.nav_header);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                        Intent intent1 = new Intent(FragmentActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.join:
                        Intent intent2 = new Intent(FragmentActivity.this, RegisterActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.own:
                        /*myApplication=((MyApplication)getApplication()==null)?null:(MyApplication)getApplication();
                        User user=myApplication.getUser();
                        if(user!=null){*/
                        Intent intent3=new Intent(FragmentActivity.this,Ownspace.class);
                        startActivity(intent3);/*}else{
                            Toast.makeText(Index.this,"未登录，请登录", Toast.LENGTH_SHORT).show();
                            Intent intent4=new Intent(Index.this,LoginActivity.class);
                            startActivity(intent4);
                        }*/
                        break;
                    default:

                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });



    }


    private void initTab() {
        initTabData();
        mInflater = LayoutInflater.from(this);
        tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(this,getSupportFragmentManager(),R.id.contentLayout);
        tabhost.getTabWidget().setDividerDrawable(null); //去掉按钮之间的分割线
        for (int i = 0; i < list.size(); i++) {
            TabHost.TabSpec tabSpecOne = tabhost.newTabSpec(list.get(i).getTitle());
            View view = mInflater.inflate(R.layout.tabi,null);
            ImageView img = (ImageView) view.findViewById(R.id.tab_img);
            TextView textView = (TextView) view.findViewById(R.id.tab_text);
            img.setImageResource(list.get(i).getImg());
            textView.setText(list.get(i).getTitle());
            tabSpecOne.setIndicator(view);
            tabhost.addTab(tabSpecOne, list.get(i).getFragment(),null);
        }
        tabhost.setCurrentTab(2);
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabI) {
                Log.e("app",tabI);
            }
        });
    }

    private void initTabData(){
        list = new ArrayList<>();
        Tab tabOne = new Tab(IndexFragment.class,"首页",R.mipmap.ic_launcher);
        Tab tabTwo = new Tab(OwnFragment.class,"动态",R.mipmap.ic_launcher);


        list.add(tabOne);
        list.add(tabTwo);


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
 @Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
        case android.R.id.home:
            mDrawerLayout.openDrawer(GravityCompat.START);
            break;

    }
    return true;
}
}