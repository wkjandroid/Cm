package com.example.administra.cm.util;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FileDownloadAndUpload {
    public FileDownloadAndUpload(){}
    private OkHttpClient client;
    public  void uploadFile(String t, String uploadContent, String uploadUrl,okhttp3.Callback callback){

                client=new OkHttpClient();
                RequestBody body=new FormBody.Builder()
                        .add(t,uploadContent)
                        .build();
                Request request=new Request.Builder().url(uploadUrl)
                        .post(body).build();
                client.newCall(request).enqueue(callback);

    }
    public void downloadFile(final String downloadUrl,okhttp3.Callback callback){

              client =new OkHttpClient();
              Request request=new Request.Builder().
                      url(downloadUrl).build();
                 client.newCall(request).enqueue(callback);
    }
}
