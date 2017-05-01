package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EditAgahi extends AppCompatActivity {

    EditText price,email,phone , title , content , location;
    TextView txtPrice,txtEmail,txtMobile , txtTitle, txtContent , txtType,txtLocation,txtCat , txtImg;
   // String name , id , type,subid,location_id;
    String    type;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    public static ProgressDialog dialog;
    public static Button SelectImage;
    private ImageView ivImageAsli, ivImage2, ivImage3;
    private ImageButton imgDelete1, imgDelete2, imgDelete3;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String url , userChoosenTask;
    File destination;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    LinearLayout imgAsli , img2,img3;
    File file;
    Uri uri , uri2;
    Intent CamIntent, GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;
    boolean flag1,flag2,flag3,flag4,flag5,flag6,flag7,flag8=false;
    ImageView image;
    File file1 , file2 , file3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agahi);

        Button SelectCat = (Button) findViewById(R.id.btn_cats);
        SelectImage = (Button) findViewById(R.id.btn_select_image);
        price = (EditText) findViewById(R.id.edt_price);
        email = (EditText) findViewById(R.id.edt_email);
        location = (EditText) findViewById(R.id.edt_location);
        phone = (EditText) findViewById(R.id.edt_phone);
        title = (EditText) findViewById(R.id.edt_title);
        content = (EditText) findViewById(R.id.edt_content);
        LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
        LinearLayout llSave = (LinearLayout) findViewById(R.id.ll_save);
        LinearLayout llClose = (LinearLayout) findViewById(R.id.ll_close);

        imgAsli = (LinearLayout) findViewById(R.id.img_asli);
        img2 = (LinearLayout) findViewById(R.id.img2);
        img3 = (LinearLayout) findViewById(R.id.img3);

        ivImageAsli = (ImageView) findViewById(R.id.ivImage_asli);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);

        imgDelete1 = (ImageButton) findViewById(R.id.img_del1);
        imgDelete2 = (ImageButton) findViewById(R.id.img_del2);
        imgDelete3 = (ImageButton) findViewById(R.id.img_del3);

        ImageButton close = (ImageButton) findViewById(R.id.close);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);
        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
        final RadioButton radio1 = (RadioButton)findViewById(R.id.rb1);
        final RadioButton radio2 = (RadioButton)findViewById(R.id.rb2);


        txtContent = (TextView) findViewById(R.id.txt_content);
        txtLocation = (TextView) findViewById(R.id.txt_location);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtCat = (TextView) findViewById(R.id.txt_cat);
        txtImg = (TextView) findViewById(R.id.txt_img_asli);

        url                 =App.urlApi+"updateagahi/"+Details.idAgahi;

        title.setText(Details.tvtitle.getText());
        location.setText(Details.tvlocation.getText());
        content.setText(Details.tvcontent.getText());
        phone.setText(Details.mobile);
        email.setText(Details.email);
        price.setText(Details.price);


        imgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAsli.setVisibility(View.GONE);
            }
        });
        imgDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img2.setVisibility(View.GONE);
            }
        });
        imgDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img3.setVisibility(View.GONE);
            }
        });



        if(Details.idRadio==0){
            radio1.setChecked(true);
        }
        if(Details.idRadio==1){
            radio2.setChecked(true);
        }

        SelectCat.setText(Details.catname);

        try {
            if (Details.image.equals("0")) {
                imgAsli.setVisibility(View.GONE);
                SelectImage.setText("افزودن عکس");
            }
            if (!Details.image.equals("0")) {
                Glide.with(this).load(App.urlimages + Details.image).into(ivImageAsli);
                imgAsli.setVisibility(View.VISIBLE);
                SelectImage.setText("افزودن عکسی دیگر");
                Log.i("url", "...:" + Details.image);
            }
            if (Details.url2.toString().equals("0")) {
                img2.setVisibility(View.GONE);
            }
            if (!Details.url2.toString().equals("0")) {
                Glide.with(this).load(App.urlimages + Details.url2).into(ivImage2);
                img2.setVisibility(View.VISIBLE);
                SelectImage.setText("افزودن عکسی دیگر");
                Log.i("url2", "...:" + Details.url2);

            }

            if (Details.url3.toString().equals("0")) {
                img3.setVisibility(View.GONE);
            }
            if (!Details.url3.toString().equals("0")) {
                Glide.with(this).load(App.urlimages + Details.url3).into(ivImage3);
                img3.setVisibility(View.VISIBLE);
                SelectImage.setText("افزودن عکسی دیگر");
                Log.i("url3", "...:" + Details.url3);
            }
        }catch (Exception e){}


        EnableRuntimePermission();

            txtCat.setVisibility(View.GONE);
            llForm.setVisibility(LinearLayout.VISIBLE);
            llClose.setVisibility(LinearLayout.VISIBLE);
            llErsal.setVisibility(LinearLayout.VISIBLE);
            llSave.setVisibility(LinearLayout.VISIBLE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAgahi.this.finish();
                Intent intent = new Intent(App.context, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAgahi.this.finish();
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

                if(price.getText().toString().matches("")){
                    txtPrice.setVisibility(View.VISIBLE);
                    flag1=true;
                }else {
                    txtPrice.setVisibility(View.GONE);
                    flag1 = false;
                }

                if(phone.getText().toString().matches("")){
                    txtMobile.setVisibility(View.VISIBLE);
                    flag3=true;
                }else{
                    txtMobile.setVisibility(View.GONE);
                    flag3=false;
                }

                if(location.getText().toString().matches("")){
                    txtLocation.setVisibility(View.VISIBLE);
                    flag4=true;
                }else {
                    txtLocation.setVisibility(View.GONE);
                    flag4 = false;
                }

                if(title.getText().toString().matches("")){
                    txtTitle.setVisibility(View.VISIBLE);
                    flag5=true;
                }else {
                    txtTitle.setVisibility(View.GONE);
                    flag5 = false;
                }

                if(content.getText().toString().matches("")){
                    txtContent.setVisibility(View.VISIBLE);
                    flag6=true;
                }else {
                    txtContent.setVisibility(View.GONE);
                    flag6 = false;
                }

                if(radioTypeGroup.getCheckedRadioButtonId() == -1){
                    txtType.setVisibility(View.VISIBLE);
                    flag7=true;
                }else{
                    txtType.setVisibility(View.GONE);
                    flag7=false;
                }
                if(imgAsli.getVisibility()==View.GONE && (img2.getVisibility()==View.VISIBLE || img3.getVisibility()==View.VISIBLE )){
                    txtImg.setVisibility(View.VISIBLE);
                    flag8=true;
                }else{
                    txtImg.setVisibility(View.GONE);
                    flag8=false;
                }


                if (flag1 == false && flag3 == false && flag4 == false && flag5 == false && flag6 == false && flag7 == false && flag8 == false) {
                    dialog = ProgressDialog.show(EditAgahi.this, null, null, true, false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.progress_layout_small);
                    int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                    radioTypeButton = (RadioButton) findViewById(selectedId);
                    if (radioTypeButton != null) {
                        type = radioTypeButton.getText().toString();
                    }
                    webServiceEditAgahi();
                }

            }
        });

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgAsli.getVisibility()==View.GONE) {
                    selectImage(ivImageAsli);
                } else if(imgAsli.getVisibility()==View.VISIBLE && img2.getVisibility()==View.GONE) {
                    selectImage(ivImage2);
                }else if(img2.getVisibility()==View.VISIBLE && img3.getVisibility()==View.GONE) {
                    selectImage(ivImage3);
                }

            }
        });
    }

    public  void webServiceEditAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("title", title.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("content", content.getText());
        params.put("deviceid", App.android_id);
        params.put("devicemodel", App.android_Model);
        params.put("price", price.getText());
        params.put("email", email.getText());
        params.put("mobile", phone.getText());
        params.put("type",type);
        params.put("validity","0");
        params.put("location",location.getText());
        if(file1!=null){
            try {
                Log.i("file: " ,"....:" + file1.toString());
                params.put("file", file1);
            } catch(FileNotFoundException e) {}
        }else if(file2!=null){
        try {
            Log.i("filei: " ,"....:" + file2.toString());
            params.put("filei", file2);
        } catch(FileNotFoundException e) {}
    }
    else if(file3!=null){
        try {
            Log.i("fileii: " ,"....:" + file1.toString());
            params.put("fileii", file3);
        } catch(FileNotFoundException e) {}
    }
         if(file1==null && imgAsli.getVisibility()==View.VISIBLE){
            Log.i("file11: " ,"....:" + Details.image.toString());
            params.put("image", Details.image);
        }
         if(file2==null && img2.getVisibility()==View.VISIBLE){
            Log.i("file22: " ,"....:" + Details.url2.toString());
            params.put("imagei", Details.url2);
        }
         if(file3==null && img3.getVisibility()==View.VISIBLE){
            Log.i("file33: " ,"....:" + Details.url3.toString());
            params.put("imageii", Details.url3);
        }
         if(file1==null && imgAsli.getVisibility()==View.GONE){
            params.put("image", "0");
        }
         if( img2.getVisibility()==View.GONE){
            params.put("imagei", "0");
        }
         if(img3.getVisibility()==View.GONE){
            params.put("imageii", "0");
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

                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    dialog.hide();
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAgahi.this);
        builder.setTitle("اضافه کردن تصویر");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(EditAgahi.this);

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
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

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
                if(imgAsli.getVisibility()==View.GONE) {
                    SelectImage.setText("افزودن عکس");
                }else if(imgAsli.getVisibility()==View.VISIBLE) {
                    SelectImage.setText("افزودن عکسی دیگر");
                }

                if(imgAsli.getVisibility()==View.GONE) {
                    imgAsli.setVisibility(View.VISIBLE);
                    file1 = destination;
                    Log.i("file1" , "1: "+file1.toString());
                }else if(imgAsli.getVisibility()==View.VISIBLE && img2.getVisibility()==View.GONE){
                    img2.setVisibility(View.VISIBLE);
                    file2 = destination;
                    Log.i("file2" , "2: "+file2.toString());

                }else if(img2.getVisibility()==View.VISIBLE && img3.getVisibility()==View.GONE) {
                    img3.setVisibility(View.VISIBLE);
                    file3 = destination;
                    Log.i("file3" , "3: "+file3.toString());

                }

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
            CropIntent.putExtra("outputX", 100);
            CropIntent.putExtra("outputY", 100);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectY", 3);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditAgahi.this,
                Manifest.permission.CAMERA))
        {

            // Toast.makeText(NewAgahi.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditAgahi.this,new String[]{
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