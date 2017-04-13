package com.example.administra.cm;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administra.cm.R;
import com.example.administra.cm.activity.UploadActivity;
import com.example.administra.cm.activity.XiangeceActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administra.cm.activity.UploadActivity.calculateInSampleSize;
import static com.example.administra.cm.activity.UploadActivity.getSmallBitmap;

/**
 * Created by Administra on 2017/3/30.
 */

public class UploadsActivity extends AppCompatActivity implements View.OnClickListener{
    private   Uri imageUri=null;
    private String[] types=new String[]{"爱情","亲情","友情","摇滚"};
    MediaRecorder recorder;
    MediaPlayer mediaPlayer;
   private ProgressBar proBar;
    @BindView(R.id.whellview)
    WheelView wheelView;
    String input=null;
    String realpath=null;
    int i=1,k;
    private GridView gridView1;              //网格显示缩略图
    private Button buttonPublish;            //发布按钮
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private static ArrayList<String> selectedimages=new ArrayList<String>();
    private ArrayList<String> selectcheck=new ArrayList<String>();
    PopupWindow window;
    Random random=new Random();
    private int TAG=0;
    private File file;
    private EditText edit;
    private String text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploads);
        edit=(EditText)findViewById(R.id.edittext);
        Intent intent1=getIntent();
        text=intent1.getStringExtra("text");
        if(text!=null){
            edit.setText(text);
        }

        ButterKnife.bind(this);
        wheelView = (WheelView) findViewById(R.id.whellview);
        after();

        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                input = item;
                System.out.println(input);
            }
        });

        k=(i*random.nextInt(1000));


        if (ContextCompat.checkSelfPermission(UploadsActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UploadsActivity.this,new String[]{Manifest.permission.INTERNET},1);
        }
         /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //获取控件对象
        gridView1 = (GridView) findViewById(R.id.gw);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        //获取资源图片加号
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tianjia);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", R.drawable.tianjia);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageview});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                /*if( imageItem.size() == 10) { //第一张为默认图片
                    Toast.makeText(UploadsActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                }
                else */if(position == 0) { //点击图片位置为+ 0对应0张图片

                    //选择图片
                showPopwindow();

                    //通过onResume()刷新数据
                }
                else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第"+(position + 1)+" 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent=getIntent();
        selectedimages=intent.getStringArrayListExtra("selected");
        if(selectedimages!=null) {
            System.out.println(selectedimages.size() + "556666666666666");
        }
    }

    private void showPopwindow() {
        LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.share_popu_menu,null);
        window=new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        ColorDrawable dw=new ColorDrawable(0000000000);
        window.setBackgroundDrawable(dw);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(UploadsActivity.this.findViewById(R.id.gw), Gravity.BOTTOM,0,0);
        Button takebutton=(Button)view.findViewById(R.id.take_photo);

        Button selectbutton=(Button)view.findViewById(R.id.selected_photo);
        Button cancel=(Button)view.findViewById(R.id.cancle);
        takebutton.setOnClickListener(this);
        selectbutton.setOnClickListener(this);
        cancel.setOnClickListener(this);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }


    void after() {
        List<String> list = new ArrayList();
        for (int i = 0; i <types.length; i++) {
            list.add(types[i]);
        }

            wheelView.setItems(list);

    }




    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== 1){
            Uri uri=data.getData();
            Cursor cursor=getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA},
                    null,null,null);
            if (cursor!=null){
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(index);
                if (path.endsWith("jpg") || path.endsWith("png")||
                        path.endsWith("PNG")||path.endsWith("JPG")||
                        path.endsWith("bmp")||path.endsWith("gif")){
                    try {
                        realpath=path;
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        ImageView imageView=(ImageView)findViewById(R.id.imag_view);
                        imageView.setImageBitmap(bitmap);
                    }catch (Exception e){
                    }
                }
            }
        }
        if (requestCode==3)
        {  Bitmap bitmap=null;
            try {
                bitmap = getSmallBitmap(imageUri.getPath());
                OutputStream baos = new FileOutputStream(imageUri.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                baos.flush();
                baos.close();
            }catch (Exception e){

            }
            realpath=imageUri.getPath();
            ImageView imageView=(ImageView)findViewById(R.id.imag_view);
            imageView.setImageBitmap(bitmap);

        }
    }
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(UploadsActivity.this,"请求被拒绝，请开启权限!!!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }*/


  /*  //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }  //end if 打开图片
    }*/

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();

        if(selectedimages!=null && TAG!=1) {
            for(int i=0;i<selectedimages.size();i++) {
                pathImage=selectedimages.get(i);

                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
                Bitmap bitmap=BitmapFactory.decodeFile(pathImage,options);
                options.inSampleSize = calculateInSampleSize(options, 200, 200);
                options.inJustDecodeBounds=false;
                Bitmap newbm=BitmapFactory.decodeFile(pathImage,options);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", newbm);
                    imageItem.add(map);
                    simpleAdapter = new SimpleAdapter(this,
                            imageItem, R.layout.griditem_addpic,
                            new String[]{"itemImage"}, new int[]{R.id.imageview});
                    simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data,
                                                    String textRepresentation) {
                            // TODO Auto-generated method stub
                            if (view instanceof ImageView && data instanceof Bitmap) {
                                ImageView i = (ImageView) view;
                                i.setImageBitmap((Bitmap) data);
                                return true;
                            }
                            return false;
                        }
                    });
                    gridView1.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();
                    //刷新后释放防止手机休眠后自动添加
                    pathImage = null;

            }
        }
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadsActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.take_photo:
                TAG=1;
                Calendar now=Calendar.getInstance();
                String filename= now.get(Calendar.YEAR)+now.get(Calendar.MONTH)+now.get(Calendar.DAY_OF_MONTH)+now.get(Calendar.HOUR_OF_DAY)+now.get(Calendar.MINUTE)+now.get(Calendar.SECOND)+"CMuploads.jpg";
                file=new File(Environment.getExternalStorageDirectory(),filename);
                if (file.exists()){
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(UploadsActivity.this,"com.example.administra.cm.FileProvider",
                            file);
                }else {
                    imageUri=Uri.fromFile(file);
                }
                Intent takephotoIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takephotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(takephotoIntent,3);
                break;

            case R.id.selected_photo:
                text=edit.getText().toString();
                Intent intent = new Intent(UploadsActivity.this, XiangeceActivity.class);
                intent.putExtra("text",text);
                intent.putStringArrayListExtra("hadSelected",selectedimages);
                startActivity(intent);
                finish();
                break;
            case R.id.cancle:
                window.dismiss();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri=Uri.fromFile(file);
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        this.sendBroadcast(intent);
        Bitmap bitmap=null;
        try {
            bitmap = getSmallBitmap(imageUri.getPath());
            OutputStream baos = new FileOutputStream(imageUri.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
            baos.flush();
            baos.close();
        }catch (Exception e){

        }
        String imagepath=imageUri.getPath();
        if(selectedimages!=null) {
            selectedimages.add(imagepath);
        }else{
            selectcheck.add(imagepath);
            selectedimages=selectcheck;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bitmap);
        imageItem.add(map);


        simpleAdapter.notifyDataSetChanged();






    }
}



