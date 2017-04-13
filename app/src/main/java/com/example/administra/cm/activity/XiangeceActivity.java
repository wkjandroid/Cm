package com.example.administra.cm.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administra.cm.R;
import com.example.administra.cm.UploadsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.administra.cm.activity.UploadActivity.calculateInSampleSize;

public class XiangeceActivity extends AppCompatActivity implements GridView.OnItemClickListener, View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private LinearLayout share_back;
    private TextView text1;
    private Button bu_select,btn_confirm;
    private CheckBox del;
    private RelativeLayout share_confirm;
    private ArrayAdapter<LoadedImage> photos;
    private GridView gv;
    private FileAdapter fileadapter;
    private String[] imagename;
    private boolean isSelected=true;
    private int countall;
    List<String> paths=new ArrayList<String>();
    ArrayList<String> selectedpath=new ArrayList<String>();
    ArrayList<String> selectedpathcheck=new ArrayList<String>();
    private  String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imagexc);
        init();
        new AsynLoadedImage().execute();
        Intent intentget=getIntent();
        selectedpathcheck=intentget.getStringArrayListExtra("hadSelected");
        text=intentget.getStringExtra("text");
        if(selectedpathcheck!=null) {


            selectedpath.addAll(selectedpathcheck);
        }
    }
    private void init() {
        share_back=(LinearLayout)findViewById(R.id.share);

        bu_select=(Button)findViewById(R.id.select);
        btn_confirm=(Button)findViewById(R.id.confirm);

        share_confirm=(RelativeLayout)findViewById(R.id.dellay);
        gv=(GridView)findViewById(R.id.gv);
        gv.setOnItemClickListener(this);
        fileadapter=new FileAdapter(XiangeceActivity.this);
        gv.setAdapter(fileadapter);
        bu_select.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.back_text:


                Intent intent=new Intent(XiangeceActivity.this, UploadsActivity.class);
                startActivity(intent);
                finish();
                break;*/
            case R.id.select:
                if(isSelected){
                    bu_select.setText(getResources().getString(R.string.cancle));

                    isSelected=false;
                    fileadapter.setDelposition(1);
                    fileadapter.notifyDataSetChanged();
                }else if((bu_select.getText().toString()).equals("取消")){

                    /*for(int i=0;i<fileadapter.photos.size();i++){
                        fileadapter.photos.get(i).setFlag(false);
                    }*/
                    btn_confirm.setText("未选择");
                   /* selectedpath.removeAll(selectedpath);*/
                    isSelected=true;
                    fileadapter.setDelposition(0);
                    fileadapter.notifyDataSetChanged();
                    Intent intent=new Intent(XiangeceActivity.this,UploadsActivity.class);
                    intent.putStringArrayListExtra("selected",selectedpath);

                    startActivity(intent);
                    finish();
                }
                break;
           /*case R.id.del:
               boolean isSelected=false;
               for(int i=0;i<fileadapter.photos.size();i++){
                   if(fileadapter.photos.get(i).isFlag()){
                       isSelected=true;
                       break;
                   }
               }
               if(isSelected){
                   Message message=new Message();
                   message.what=0;
                   *//*delHandler.sendMessage(message);*//*
               }else{
                   Toast.makeText(ImageLActivity.this,"选择为空",Toast.LENGTH_SHORT).show();
               }
               break;*/
            case R.id.confirm:

                Intent intent=new Intent(XiangeceActivity.this,UploadsActivity.class);
                intent.putStringArrayListExtra("selected",selectedpath);
                intent.putExtra("text",text);
                startActivity(intent);
                finish();
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*if(isChecked){

            for(int i=0;i<fileadapter.photos.size();i++){
                fileadapter.photos.get(i).setFlag(false);
            }
        }else{

            for(int i=0;i<fileadapter.photos.size();i++){
                fileadapter.photos.get(i).setFlag(false);
            }
        }
        fileadapter.notifyDataSetChanged();*/
    }
    class FileAdapter extends BaseAdapter {
        private Context mcontext;
        LayoutInflater inflater;
        private static final int EDIT=1;
        private static final int UNEDIT=0;
        private int delposition=UNEDIT;
        private ArrayList<CheckBox> cbs=new ArrayList<CheckBox>();
        private ArrayList<LoadedImage> photos=new ArrayList<LoadedImage>();
        public FileAdapter(Context context){
            mcontext=context;
            inflater=LayoutInflater.from(mcontext);
        }
        public void addphoto(LoadedImage photo){
            photos.add(photo);

        }
        public void addcb(CheckBox cbn){cbs.add(cbn);}

        public void setDelposition(int deleposition)
        {
            this.delposition=deleposition;
        }

        @Override
        public int getCount() {
            return photos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final int pos=position;
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder=new ViewHolder();

                convertView=inflater.inflate(R.layout.share_photo_item,null);

                viewHolder.image_share=(ImageView)convertView.findViewById(R.id.imageviewi);
                viewHolder.pic_check=(CheckBox)convertView.findViewById(R.id.cbb);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder)convertView.getTag();
            }
            viewHolder.image_share.setPadding(0,1,1,1);
            viewHolder.image_share.setImageBitmap(photos.get(position).getMbit());
            if(delposition==UNEDIT){
                viewHolder.pic_check.setVisibility(View.GONE);
            }else{
                viewHolder.pic_check.setVisibility(View.VISIBLE);
            }
            viewHolder.pic_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        int checkhadselected=0;
                        photos.get(pos).setFlag(true);
                        for(int i=0;i<selectedpath.size();i++) {
                            if (selectedpath.get(i).equals(photos.get(pos).getPicpath())){
                                checkhadselected=1;
                            }
                        }
                        if(checkhadselected==0) {
                            selectedpath.add(photos.get(pos).getPicpath());
                        }
                        btn_confirm.setText("确定("+selectedpath.size()+"张)");
                        System.out.println("123333333333333"+selectedpath.size());
                    }else{
                        photos.get(pos).setFlag(false);


                    }
                }
            });

            if(!((bu_select.getText().toString()).equals("选择"))){
                System.out.println("5616");

                final ViewHolder finalViewHolder = viewHolder;
                viewHolder.image_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(finalViewHolder.pic_check.isChecked())) {
                            photos.get(position).setFlag(true);
                            System.out.println("5616" + bu_select.getText().toString() + photos.get(position).isFlag());
                            finalViewHolder.pic_check.setChecked(true);

                        }else{
                            photos.get(position).setFlag(false);
                            finalViewHolder.pic_check.setChecked(false);

                            selectedpath.remove(selectedpath.size()-1);
                            System.out.println("123333333333333"+selectedpath.size());
                            btn_confirm.setText("确定("+selectedpath.size()+"张)");
                        }
                    }
                });

            }

            for (int i = 0; i < selectedpath.size(); i++) {

                    if(selectedpath.get(i).equals(photos.get(position).getPicpath())) {
                        viewHolder.pic_check.setChecked(true);

                    }

            }
            viewHolder.pic_check.setChecked(photos.get(position).isFlag());
            return convertView;
        }
    }
    private static class LoadedImage{
        String picpath;
        Bitmap mbit;

        boolean flag;
        LoadedImage(Bitmap bitmap,String picpath,boolean flag){
            this.mbit=bitmap;
            this.picpath=picpath;
            this.flag=flag;
        }
        public void setFlag(boolean flag){
            this.flag=flag;
        }
        public String getPicpath() {
            return picpath;
        }

        public Bitmap getMbit() {
            return mbit;
        }

        public boolean isFlag() {
            return flag;
        }
    }
    final class ViewHolder{
        public ImageView image_share;
        public CheckBox pic_check;
    }
    /*private static String[] imageFormatSet=new String[]{".jpg","png"};
    private static boolean isImgeFile(String path){
        for(String format:imageFormatSet){
            if(path.contains(format)){
                return true;
            }
        }
        return false;
    }*/
    /*private void getFiles(String url){
        File files=new File(url);
        File[] file=files.listFiles();
        try{
            for(File f:file){
                if(f.isDirectory()){
                    getFiles(f.getAbsolutePath());
                }else{
                    if(isImgeFile(f.getPath())){
                        paths.add(f.getPath());
                        System.out.println(f.getPath());
                        f.getName();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

    class AsynLoadedImage extends AsyncTask<Object,LoadedImage,Object> {

        @Override
        protected Object doInBackground(Object... params) {
            final  String path= Environment.getExternalStorageDirectory()+"/";
            File file=new File(path);
            if(!file.exists()){
                file.mkdirs();
            }else{
                File[] files=file.listFiles();
                Cursor cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
                while(cursor.moveToNext()){
                    String pathc=cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                    paths.add(pathc);
                }//获取本地图片


                countall=files.length;

                int i=0;
                for(File f:files){




                    try{
                        BitmapFactory.Options options=new BitmapFactory.Options();
                        options.inJustDecodeBounds=true;
                        Bitmap bitmap=BitmapFactory.decodeFile(paths.get(i),options);
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);
                        options.inJustDecodeBounds=false;
                        Bitmap newbm=BitmapFactory.decodeFile(paths.get(i),options);
                        if(newbm!=null){
                            publishProgress(new LoadedImage(newbm,paths.get(i),false));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    i++;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(LoadedImage... values) {
            addImage(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            setProgressBarIndeterminateVisibility(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final GridView grid=gv;
        final int count=grid.getChildCount();
        ImageView iv=null;
        View view=null;

        for(int i=0;i<count;i++){
            view=grid.getChildAt(i);
            iv=(ImageView)view.findViewById(R.id.imageviewi);
            ((BitmapDrawable)iv.getDrawable()).setCallback(null);
        }
        System.gc();
    }

    private void addImage(LoadedImage[] values) {
        for(LoadedImage image:values){
            fileadapter.addphoto(image);
            fileadapter.notifyDataSetChanged();
        }
    }
}

