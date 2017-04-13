package com.example.administra.cm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administra.cm.util.FileDownloadAndUpload;
import com.example.administra.cm.util.GsonUtil;
import com.example.administra.cm.service.LoginService;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.R;
import com.example.administra.cm.po.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends Activity {
    public static final String loginUrl="http://119.29.154.229/CommentPrj/LoginServlet";
    private EditText account;
    private EditText pwd;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private User user;
    private Gson gson;
    private CheckBox repass;
    private Message message;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(LoginActivity.this,"系统繁忙，请稍等...",Toast.LENGTH_SHORT).show();
                    break;
                case 1:             //false
                    pwd .setError("密码错误");
                    break;
                case 2:
                    account.setError("账户不存在");
                    break;
                case 4:
                    Toast.makeText(LoginActivity.this,"没联网！",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    if(repass.isChecked()){
                        pref=getSharedPreferences("login_info", Context.MODE_PRIVATE);
                        editor=pref.edit();
                        editor.putString("account",account.getText().toString());
                        editor.putString("password",pwd.getText().toString());
                        editor.putBoolean("remember",true);
                        editor.apply();
                        editor.commit();
                    }else{
                        pref=getSharedPreferences("login_info", Context.MODE_PRIVATE);
                        editor=pref.edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();
                    }
                    Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent=new Intent(LoginActivity.this,Index.class);
                    startActivity(intent);


            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView loginToRegister=(TextView)findViewById(R.id.join);
        loginToRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        final Button startBtn=(Button)findViewById(R.id.start_login);
        account=(EditText)findViewById(R.id.account);
        pwd=(EditText)findViewById(R.id.pwd);
        repass=(CheckBox)findViewById(R.id.cb);
        SharedPreferences prefs=getSharedPreferences("login_info", Context.MODE_PRIVATE);

        prefs.getBoolean("remember",false);//如果没得到就得到他的默认值是false
        if(prefs.getBoolean("remember",false)){
            account.setText(prefs.getString("account",""));
            pwd.setText(prefs.getString("password",""));
            repass.setChecked(true);
        }

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final  ProgressDialog loginDialog = new ProgressDialog(LoginActivity.this);
                loginDialog.setMessage("登陆中...");
                loginDialog.show();
                pwd = (EditText)findViewById(R.id.pwd);
                account=(EditText)findViewById(R.id.account);
                user=new User();
                user.setAccount(account.getText().toString().trim());
                user.setPassword(pwd.getText().toString().trim());
                user.setIntention(2);       //1表示注册 2登录
                gson= GsonUtil.getInstance().getGson();
                new FileDownloadAndUpload().uploadFile("user", gson.toJson(user),loginUrl,new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message=new Message();
                        message.what=3;     //系统繁忙
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // user.getToken(); false 登录失败    none 用户的不存在;
                        String responseData = response.body().string();
                      //  System.out.println(responseData+"wkj");
                        User respUser = gson.fromJson(responseData, User.class);
                        loginDialog.cancel();
                        if (respUser.getToken().equals("false")){
                            message=new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }else if (respUser.getToken().equals("none")){
                            message=new Message();
                            message.what=2;
                            handler.sendMessage(message);
                        }else if(responseData==""||responseData==null){
                            message=new Message();
                            message.what=4;
                            handler.sendMessage(message);

                        }else if(respUser instanceof User){
                          //  System.out.println(responseData+respUser.getUid());
                            MyApplication.setUser(respUser);
                            Intent intent=new Intent(LoginActivity.this,LoginService.class);
                            startService(intent);
                            Message message=new Message();
                            message.what=3;         //0登录成功
                            handler.sendMessage(message);
                            }
                        else {
                            Message message = new Message();
                            message.what = 4;         //0登录成功
                            handler.sendMessage(message);
                        }
                    }

                });
            }
        });
    }
}
