package com.example.administra.cm.po;

import com.example.administra.cm.po.CmFile;

import org.litepal.crud.DataSupport;

import java.util.Set;

public class User extends DataSupport{
    private int uid;
    private String account;
    private  String username;
    private String password;
    private boolean gender;
    private String qw;
    private int sign;   //1代表作词 2代表作曲
    private String token;
    private byte[] image;
    private int intention;  //1 登录  2注册             0掉线
    private Set<CmFile> cmFiles;

    public Set<CmFile> getCmFiles() {
        return cmFiles;
    }

    public void setCmFiles(Set<CmFile> cmFiles) {
        this.cmFiles = cmFiles;
    }

    public User (){}
    public User(int uid, String account, String username, String password, boolean gender, String qw, int sign,
                String token, byte[] image,int intention ) {
        this.uid = uid;
        this.account = account;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.qw = qw;
        this.sign = sign;
        this.token = token;
        this.image = image;
        this.intention=intention;

    }
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getQw() {
        return qw;
    }

    public void setQw(String qw) {
        this.qw = qw;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getIntention() {
        return intention;
    }

    public void setIntention(int intention) {
        this.intention = intention;
    }



}
