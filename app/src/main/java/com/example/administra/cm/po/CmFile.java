package com.example.administra.cm.po;

import android.provider.MediaStore;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CmFile extends DataSupport {
    private int id;
    private String account;
    private Date time;
    private byte[] file;
    private int sign;	//1:词  2:曲
    private int type;
    private int intention;		//意图	：1上传	2下载	3修改	4删除
    public CmFile(){}
    private Set<FileImages> fileImages=new HashSet<>();
    public Set<FileImages> getFileImages() {
        return fileImages;
    }
    public void setFileImages(Set<FileImages> fileImages) {
        this.fileImages = fileImages;
    }

    public CmFile(int id, String account, Date time, byte[] file, int sign, int type, int intention) {
        this.id = id;
        this.account=account;
        this.time=time;
        this.file=file;
        this.sign=sign;
        this.type=type;
        this.intention=intention;
    }

    public int getIntention() {
        return intention;
    }

    public CmFile setIntention(int intention) {
        this.intention = intention;
        return this;
    }

    public int getType() {

        return type;
    }

    public CmFile setType(int type) {
        this.type = type;
        return this;
    }

    public int getSign() {

        return sign;
    }

    public CmFile setSign(int sign) {
        this.sign = sign;
        return this;
    }

    public byte[] getFile() {

        return file;
    }

    public CmFile setFile(byte[] file) {
        this.file = file;
        return this;
    }

    public Date getTime() {

        return time;
    }

    public CmFile setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getAccount() {

        return account;
    }

    public CmFile setAccount(String account) {
        this.account = account;
        return this;
    }

    public int getId() {

        return id;
    }

    public CmFile setId(int id) {
        this.id = id;
        return this;
    }
}
