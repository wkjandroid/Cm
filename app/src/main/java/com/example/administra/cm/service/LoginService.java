package com.example.administra.cm.service;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.example.administra.cm.util.FileDownloadAndUpload;
import com.example.administra.cm.util.GsonUtil;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.R;
import com.example.administra.cm.activity.LoginActivity;
import com.example.administra.cm.po.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginService extends Service {
    private Gson gson= GsonUtil.getInstance().getGson();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    break;
                case 2:
                    MyApplication.setUser(null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginService.this)
                            .setMessage("用户已掉线")
                            .setTitle("提醒")
                            .setCancelable(true)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(LoginService.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.create().show();
                   // NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                   /* Notification notification= new NotificationCompat.Builder(LoginService.this)
                            .setContentTitle("该用户在别处登录，请修改密码。")
                            .setWhen(System.currentTimeMillis())
                            .setContentText("该用户在别处登录，请修改密码。")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true).build();
                    manager.notify(1,notification);*/
                    break;
                case 3:
                    NotificationManager nmanager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    Notification nnotification= new NotificationCompat.Builder(LoginService.this)
                            .setContentTitle("系统繁忙")
                            .setWhen(System.currentTimeMillis())
                            .setContentText("系统繁忙")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true).build();
                    nmanager.notify(1,nnotification);
                    break;
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        User user = MyApplication.getUser();
        if (user!=null) {
            user.setIntention(3);
            new FileDownloadAndUpload().uploadFile("user", gson.toJson(user)
                    , LoginActivity.loginUrl, new okhttp3.Callback() {

                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = 3;
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Gson gson=new Gson();
                            //System.out.println(responseData+"good");
                            User respUser = gson.fromJson(responseData,User.class);
                            ////1 代表登录		2下线	0异常
                            if (respUser.getIntention() == 0) {
                                Message message = new Message();
                                message.what = 3;
                                handler.sendMessage(message);
                            } else if (respUser.getIntention() == 2) {
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            }
                        }
                    });
        }
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        long triggerAtMill= SystemClock.elapsedRealtime()+1000;
        Intent intent1=new Intent(LoginService.this,LoginService.class);
        PendingIntent pi=PendingIntent.getService(getApplicationContext(),0,intent1,0);
        if (Build.VERSION.SDK_INT>=23){
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtMill,pi);
        }else {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMill, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
