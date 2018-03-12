package com.shieh.rain.ftpclient;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            if (PackageManager.PERMISSION_GRANTED != this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
            }
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                final boolean success = FTPUtils.getInstance().initFTPSetting("192.168.86.3", 2121, "way", "way");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onResult(success);
                    }
                });

            }
        }.start();
    }

    private void onResult(boolean success){
        if(success){
            Toast.makeText(this,"连接成功,下载图片",Toast.LENGTH_LONG).show();
            downLoad();
        }else{
            Toast.makeText(this,"连接失败",Toast.LENGTH_LONG).show();
        }
    }

    private void downLoad() {
        try {
            //要存放的文件的路径
            final String filePath = Environment.getExternalStorageDirectory().getPath() + "/ftpImages";
//            final String filePath = Environment.getExternalStorageDirectory().getPath() ;
            new Thread(){
                @Override
                public void run() {
                    downLoadImage(filePath);
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoadImage(final String filePath) {
        final boolean success = FTPUtils.getInstance().downLoadFile(filePath, "1.jpg");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    Toast.makeText(MainActivity.this,"下载图片成功",Toast.LENGTH_LONG).show();
                    final String pathName = filePath+"/1.jpg";
                   ImageView iv = findViewById(R.id.image);
                    Bitmap bitmap = BitmapFactory.decodeFile(pathName);
                    if (bitmap != null) {
                        iv.setImageBitmap(bitmap);
                    }

                }else{
                    Toast.makeText(MainActivity.this,"下载图片失败",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
