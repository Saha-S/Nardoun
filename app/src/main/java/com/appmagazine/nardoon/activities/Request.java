package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.R;

public class Request extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                //File write logic here
                FileOperations file = new FileOperations();

                if (file.read("likes").equalsIgnoreCase("nofile")) {

                    file.write("likes", "");
                    file.write("dislikes", "");
                    file.write("favorite", "");
                }
                App.newactivity(Request.this,Main.class);
                finish();

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }

        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {


            App.newactivity(Request.this,Main.class);
            finish();
        }else {
            App.newactivity(Request.this,Request.class);
            finish();
        }
    }

}
