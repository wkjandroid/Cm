package com.example.administra.cm.fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administra.cm.fragment.MyFragment;

/**
 * Created by pljay on 2017/3/16.
 */

public class  MyFragmentManager extends FragmentPagerAdapter{
    String content[]={};
    public MyFragmentManager(FragmentManager fragmentManager,String content[]){
        super(fragmentManager);
        this.content=content;
    }



    @Override
    public Fragment getItem(int position){
        return MyFragment.newInstance("fragment");
    }
    @Override
    public CharSequence getPageTitle(int position){
        return content[position % content.length];

    }
    @Override
    public  int getCount(){
        return content.length;
    }

}
