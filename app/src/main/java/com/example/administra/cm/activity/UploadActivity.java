package com.example.administra.cm.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administra.cm.util.FileDownloadAndUpload;
import com.example.administra.cm.po.MyApplication;
import com.example.administra.cm.R;
import com.example.administra.cm.po.CmFile;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{
private   Uri imageUri=null;
    String realpath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
         LitePal.getDatabase();
        Button register=(Button)findViewById(R.id.register_button);
       Button login=(Button)findViewById(R.id.login_button);
        Button button=(Button)findViewById(R.id.select_button);

        button.setOnClickListener(UploadActivity.this);
        Button uploadImage=(Button)findViewById(R.id.upload_photo);
        uploadImage.setOnClickListener(UploadActivity.this);
        Button takeBtn=(Button)findViewById(R.id.take_photo);
        takeBtn.setOnClickListener(UploadActivity.this);
        if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UploadActivity.this,new String[]{Manifest.permission.INTERNET},1);
        }
        register.setOnClickListener(UploadActivity.this);
        login.setOnClickListener(UploadActivity.this);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.upload_photo:
                final  ProgressDialog uploadDialog = new ProgressDialog(UploadActivity.this);
                uploadDialog.setMessage("正在上传...");
                uploadDialog.show();
                Gson gson=new Gson();
                CmFile cmFile=new CmFile();
                File uploadfile = new File(realpath);
                if (uploadfile.exists()){
                    try {
                        InputStream in = new FileInputStream(uploadfile);
                        BufferedInputStream is = new BufferedInputStream(in);
                        byte[] b = new byte[is.available()];
                        is.read(b);
                        cmFile.setFile(b);
                        cmFile.setIntention(1);

                        cmFile.setId(MyApplication.getUser().getUid());
                        System.out.println("hello"+"开始上传"+MyApplication.getUser().getUid());
                    }catch (Exception e){
                    }
                    String uploadUrl="http://119.29.154.229/CommentPrj/FileManagerServlet";

                    new FileDownloadAndUpload().uploadFile("cmfile",gson.toJson(cmFile), uploadUrl,new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseData = response.body().string();

                                    uploadDialog.cancel();
                                    //System.out.println(responseData);
                                }
                            });

                }
                break;
            case R.id.select_button:
                Intent selectImage=new Intent(Intent.ACTION_PICK);
                selectImage.setType("image/*");
                startActivityForResult(selectImage,1);
                break;
            case R.id.take_photo:
               File file=new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                if (file.exists()){
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT>=24){
                    imageUri= FileProvider.getUriForFile(UploadActivity.this,"com.example.administra.cm.FileProvider",
                            file);
                }else {
                    imageUri=Uri.fromFile(file);
               }
                Intent takephotoIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takephotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(takephotoIntent,3);
                break;
            default:
                break;
        }
    }

    @Override
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
                    Toast.makeText(UploadActivity.this,"请求被拒绝，请开启权限!!!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
