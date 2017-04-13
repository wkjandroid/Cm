package com.example.administra.cm.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.example.administra.cm.util.FileDownloadAndUpload;
import com.example.administra.cm.util.GsonUtil;
import com.example.administra.cm.R;
import com.example.administra.cm.po.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final String registerUrl="http://119.29.154.229/CommentPrj/LoginServlet";
    String responseData;
    EditText account ;
    EditText username;
    EditText pwd ;
    EditText qw ;
    User user;
    RadioButton gender;
    Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this,"系统繁忙，请稍候...",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegisterActivity.this,"该用户已存在，请重新注册！",Toast.LENGTH_SHORT).show();
                   break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        TabHost tabHost =(TabHost)findViewById(R.id.tab_host);
        tabHost.setup();
        TabSpec page1 = tabHost.newTabSpec("tab1")
                .setIndicator("作词会员")
                .setContent(R.id.ci);
        tabHost.addTab(page1);
        TabSpec page2 = tabHost.newTabSpec("tab2")
                .setIndicator("作曲会员")
                .setContent(R.id.qu_tab);
        tabHost.addTab(page2);
        Button cregisterBtn=(Button)findViewById(R.id.ci_register_btn);
        cregisterBtn.setOnClickListener(new MyOnclick());
        Button qregisterBtn=(Button)findViewById(R.id.qu_register_btn);
        qregisterBtn.setOnClickListener(new MyOnclick());

    }
    class MyOnclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ci_register_btn:
                    final ProgressDialog registerDialog = new ProgressDialog(RegisterActivity.this);
                    registerDialog.setMessage("稍等...");
                    registerDialog.show();
                     account = (EditText) findViewById(R.id.ci_account);
                     username = (EditText) findViewById(R.id.ci_username);
                     pwd = (EditText) findViewById(R.id.ci_password);
                     gender = (RadioButton) findViewById(R.id.ci_gender);
                    qw = (EditText) findViewById(R.id.ci_qw);
                    user = new User(1, account.getText().toString().trim(),
                            username.getText().toString().trim(), pwd.getText().toString().trim(), gender.isChecked(),
                            qw.getText().toString().trim(), 1, "token", "".getBytes(), 1);
                    gson= GsonUtil.getInstance().getGson();
                    String userjson = gson.toJson(user);
                    new FileDownloadAndUpload().uploadFile("user",userjson,registerUrl, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                           Message message=new Message();
                            message.what=2;
                            handler.sendMessage(message);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                             responseData = response.body().string();
                            gson=new Gson();
                            Log.d("response",responseData+"wkj");
                            registerDialog.cancel();
                            User respUser = gson.fromJson(responseData, User.class);
                            if (respUser.getToken().equals("success"))
                            {
                                Message message=new Message();
                                message.what=1;
                                handler.sendMessage(message);
                            }else if (respUser.getToken().equals("exist")) {
                                Message message=new Message();
                                message.what=3;
                                handler.sendMessage(message);
                            }
                        }
                    });
                    break;
                case R.id.qu_register_btn:
                     account = (EditText) findViewById(R.id.qu_account);
                     username = (EditText) findViewById(R.id.q_username);
                     pwd = (EditText) findViewById(R.id.qu_password);
                     gender = (RadioButton) findViewById(R.id.qu_gender);
                     qw = (EditText) findViewById(R.id.qu_qw);
                     user = new User(1, account.getText().toString().trim(),
                            username.getText().toString().trim(), pwd.getText().toString().trim(),  gender.isChecked(),
                            qw.getText().toString().trim(), 2, "token", "".getBytes(), 1);
                    gson=GsonUtil.getInstance().getGson();
                    String userjson1 = gson.toJson(user);
                    new FileDownloadAndUpload().uploadFile("user",userjson1,registerUrl, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message=new Message();
                            message.what=2;
                            handler.sendMessage(message);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            responseData = response.body().string();
                            gson=GsonUtil.getInstance().getGson();
                            User respUser = gson.fromJson(responseData, User.class);
                            if (respUser.getToken().equals("success"))
                            {
                                respUser.save();
                                Message message=new Message();
                                message.what=1;
                                handler.sendMessage(message);
                            }else if (respUser.getToken().equals("exist")) {
                                Message message=new Message();
                                message.what=3;
                                handler.sendMessage(message);
                            }
                        }
                    });
                    break;
            }
        }
    }
}
