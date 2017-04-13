package com.example.administra.cm.fragment;



import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pljay on 2017/3/16.
 */

public class MyFragment extends Fragment {
    private static final String KEY_CONTENT="TestFragment:Content";
    private String mcontent="fragmentpage";
    public static MyFragment newInstance(String content){
        MyFragment fragment=new MyFragment();
        fragment.mcontent=content;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if ((savedInstanceState!=null)&&savedInstanceState.containsKey(KEY_CONTENT)){
            mcontent=savedInstanceState.getString(KEY_CONTENT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        TextView textView=new TextView(getActivity());
        textView.setText(mcontent);
        LinearLayout layout=new LinearLayout(getActivity());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(textView);
        return layout;
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT,mcontent);
    }
}
