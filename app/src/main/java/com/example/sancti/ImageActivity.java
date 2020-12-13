package com.example.sancti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageActivity extends AppCompatActivity {

    ImageView choosenImage;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent=getIntent();
        url =intent.getStringExtra("choosenImage");

        choosenImage=findViewById(R.id.choosenImage);
        Glide.with(this).load(url).into(choosenImage);

        choosenImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(ImageActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getResources().getString(R.string.dimage))
                        .setMessage(getResources().getString(R.string.ddimage))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ImageActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(ImageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                    }else{
                                        download(url);
                                    }
                                }

                            }

                        })
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
                return false;
            }
        });

    }

    public void download(String uri){
        try {
            File direct = new File(Environment.getExternalStorageDirectory()
                    + "/SanctiImages");

            if (!direct.exists()) {
                direct.mkdirs();
            }

            DownloadManager mgr = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(uri);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("image download")
                    .setDestinationInExternalPublicDir("/SanctiImages", "fileName.jpg");

            mgr.enqueue(request);
            Toast.makeText(this, getResources().getString(R.string.ds), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults
    ) {
        if (requestCode == 0) {
            int index = 0;
            Map<String, Integer> permissionsMap = new HashMap<>();
            for (String permission : permissions) {
                permissionsMap.put(permission, grantResults[index]);
                index++;
            }

            if (permissionsMap.containsKey(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && permissionsMap.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
                download(url);
            }
        }
    }
}