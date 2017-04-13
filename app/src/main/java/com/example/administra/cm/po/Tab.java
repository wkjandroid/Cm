package com.example.administra.cm.po;

/**
 * Created by Administra on 2017/4/10.
 */


public class Tab {
    private String title;
    private int img;
    private Class fragment;

    public Tab() {
    }

    public Tab(Class fragment, String title, int img) {
        this.fragment = fragment;
        this.title = title;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
