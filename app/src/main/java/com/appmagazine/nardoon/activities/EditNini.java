package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.MyNiniAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.Utility;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class EditNini extends AppCompatActivity {

    EditText edtName,edtAge;
    TextView txtName, txtAge,txtImg;
    public static String name , id , subid ;
    public static ProgressDialog dialog;
    public static Button SelectImage;
    private ImageView ivImageAsli;
    private ImageButton imgDelete1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String url , userChoosenTask;
    File destination;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    LinearLayout imgAsli ;
    File file;
    Uri uri ;
    Intent CamIntent, GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;
    boolean flag1,flag2,flag3=false;
    ImageView image;
    File file1 ;
    private String id_confirmaation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nini);

        SelectImage = (Button) findViewById(R.id.btn_select_image);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtAge = (EditText) findViewById(R.id.edt_age);

        LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
        LinearLayout llClose = (LinearLayout) findViewById(R.id.ll_close);

        imgAsli = (LinearLayout) findViewById(R.id.img_asli);

        ivImageAsli = (ImageView) findViewById(R.id.ivImage_asli);

        imgDelete1 = (ImageButton) findViewById(R.id.img_del1);

        ImageButton close = (ImageButton) findViewById(R.id.close);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);

        txtAge = (TextView) findViewById(R.id.txt_age);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtImg = (TextView) findViewById(R.id.txt_img_asli);

        url                 =App.urlApi+"updatenini/"+ MyNiniAdapter.idNini;
        Log.i("url", "...:" + App.urlApi+"updatenini/"+ MyNiniAdapter.idNini);

        edtAge.setText(MyNiniAdapter.age);
        edtName.setText(MyNiniAdapter.name);


        imgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAsli.setVisibility(View.GONE);
            }
        });




        try {
            if (MyNiniAdapter.image.equals("0")) {
                imgAsli.setVisibility(View.GONE);
                SelectImage.setText("افزودن عکس");
            }
            if (!MyNiniAdapter.image.equals("0")) {
                Glide.with(this).load(App.urlimages + MyNiniAdapter.image).into(ivImageAsli);
                imgAsli.setVisibility(View.VISIBLE);
            }


        }catch (Exception e){}


        EnableRuntimePermission();

            llForm.setVisibility(LinearLayout.VISIBLE);
            llClose.setVisibility(LinearLayout.VISIBLE);
            llErsal.setVisibility(LinearLayout.VISIBLE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNini.this.finish();
                Intent intent = new Intent(App.context, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNini.this.finish();
                Intent intent = new Intent(App.context, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llErsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtName.getText().toString().matches("")){
                    txtName.setVisibility(View.VISIBLE);
                    flag1=true;
                }else {
                    txtName.setVisibility(View.GONE);
                    flag1 = false;
                }

                if(edtAge.getText().toString().matches("")){
                    txtAge.setVisibility(View.VISIBLE);
                    flag3=true;
                }else{
                    txtAge.setVisibility(View.GONE);
                    flag3=false;
                }

                if(imgAsli.getVisibility()==View.GONE ){
                    txtImg.setVisibility(View.VISIBLE);
                    flag2=true;
                }else{
                    txtImg.setVisibility(View.GONE);
                    flag2=false;
                }


                if (flag1 == false && flag3 == false && flag2 == false && flag2 == false ) {
                    dialog = ProgressDialog.show(EditNini.this, null, null, true, false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.progress_layout_small);
                    SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                    SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                    String status = prefs2.getString("islogin", "0");
                    String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                    if (status.matches("1") && !id_confirmaationSH.equals("0")) {
                        id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");
                        webServiceEditAgahi();
                    }else {
                        Intent intent = new Intent(App.context, Login.class);
                        startActivity(intent);
                    }
                }

            }
        });

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    selectImage(ivImageAsli);

            }
        });
    }

    public  void webServiceEditAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("validity","0");
        params.put("point","0");
        params.put("pointm","0");


        params.put("name", edtName.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("age", edtAge.getText());
        params.put("confirmation_id", id_confirmaation);

        if(file1!=null){
            try {
                params.put("file", file1);
            } catch(FileNotFoundException e) {}
        }
         if(file1==null && imgAsli.getVisibility()==View.VISIBLE){
            params.put("image", MyNiniAdapter.image);
        }


        client.post(url, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
              //  Intent intent = new Intent(App.context , Details.class);
              //  intent.putExtra("id", Details.idAgahi+"");
               // startActivity(intent);
                App.CustomToast("آگهی ویرایش شد !");
                Intent intent = new Intent(App.context , MyNini.class);
                startActivity(intent);

                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast(" خطایی رخ داده است . لطفا دقایقی دیگر امتحان کنید ");

                }else{
                    dialog.hide();
                 //   App.CustomToast("fail "+statusCode);
                    App.CustomToast(" خطایی رخ داده است . لطفا دقایقی دیگر امتحان کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void selectImage(ImageView img) {
        final CharSequence[] items = { "دوربین", "گالری",
                "انصراف" };
        image= img;
        AlertDialog.Builder builder = new AlertDialog.Builder(EditNini.this);
        builder.setTitle("اضافه کردن تصویر");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(EditNini.this);

                if (items[item].equals("دوربین")) {
                    userChoosenTask ="دوربین";
                    if(result)
                        ClickImageFromCamera() ;

                } else if (items[item].equals("گالری")) {
                    userChoosenTask ="گالری";
                    if(result)
                        GetImageFromGallery();

                } else if (items[item].equals("انصراف")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    ////////////////////////////////////////////////////

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = FileProvider.getUriForFile(App.context, App.context.getApplicationContext().getPackageName() + ".provider", file);
        App.context.grantUriPermission("com.android.camera",uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.i("myuri" , "aa  :  " + uri.toString()  );

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "انتخاب عکس از گالری"), 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();

            }
        }
        else if (requestCode == 1) {

            if (data != null) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                    file1 = destination;

                image.setImageBitmap(thumbnail);

            }
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            CropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("outputX", 300);
            CropIntent.putExtra("outputY", 300);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditNini.this,
                Manifest.permission.CAMERA))
        {

            // Toast.makeText(NewAgahi.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditNini.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    //   Toast.makeText(NewAgahi.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    //  Toast.makeText(NewAgahi.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}