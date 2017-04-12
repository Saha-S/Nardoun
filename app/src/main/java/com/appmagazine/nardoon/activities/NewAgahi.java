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
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.View;
        import android.view.WindowManager;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.ScrollView;
        import android.widget.TextView;

        import com.appmagazine.nardoon.App;
        import com.appmagazine.nardoon.R;
        import com.appmagazine.nardoon.Utility;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;

        import cz.msebera.android.httpclient.Header;

        public class NewAgahi extends AppCompatActivity {

            EditText price,email,phone , title , content;
            TextView txtPrice,txtEmail,txtMobile , txtTitle, txtContent , txtType,txtLocation,txtCat;
            String name , id , type,subid,location_id;
            RadioGroup radioTypeGroup;
            RadioButton radioTypeButton;
            public static ProgressDialog dialog;
            public static Button SelectLocation;
            public static Button SelectImage;
            private ImageView ivImage;
            private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
            private String userChoosenTask;
            File destination;
            private static int REQUEST_PICTURE = 1;
            private static int REQUEST_CROP_PICTURE = 2;

            File file;
            Uri uri;
            Intent CamIntent, GalIntent, CropIntent ;
            public  static final int RequestPermissionCode  = 1 ;
            DisplayMetrics displayMetrics ;
            int width, height;




            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_new);

                Button SelectCat = (Button) findViewById(R.id.btn_cats);
                SelectLocation = (Button) findViewById(R.id.btn_location);
                SelectImage = (Button) findViewById(R.id.btn_select_image);
                price = (EditText) findViewById(R.id.edt_price);
                email = (EditText) findViewById(R.id.edt_email);
                phone = (EditText) findViewById(R.id.edt_phone);
                title = (EditText) findViewById(R.id.edt_title);
                content = (EditText) findViewById(R.id.edt_content);
                LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
                LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
                LinearLayout llSave = (LinearLayout) findViewById(R.id.ll_save);
                LinearLayout llClose = (LinearLayout) findViewById(R.id.ll_close);
                ImageButton close = (ImageButton) findViewById(R.id.close);
                LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
                ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
                TextView tvBack = (TextView) findViewById(R.id.tv_back);
                radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
                ivImage = (ImageView) findViewById(R.id.ivImage);

                txtContent = (TextView) findViewById(R.id.txt_content);
                txtEmail = (TextView) findViewById(R.id.txt_email);
                txtLocation = (TextView) findViewById(R.id.txt_location);
                txtMobile = (TextView) findViewById(R.id.txt_mobile);
                txtPrice = (TextView) findViewById(R.id.txt_price);
                txtTitle = (TextView) findViewById(R.id.txt_title);
                txtType = (TextView) findViewById(R.id.txt_type);
                txtCat = (TextView) findViewById(R.id.txt_cat);

                ScrollView scroll = (ScrollView) findViewById(R.id.scroll) ;


                Intent intent=getIntent();
                name = intent.getStringExtra("NAME");
                id = intent.getStringExtra("CATID");
                subid = intent.getStringExtra("SUBID");


                location_id = intent.getStringExtra("location_id");

                EnableRuntimePermission();



                if (name!= null){
                    SelectCat.setText(name);
                    txtCat.setVisibility(View.GONE);
                    llForm.setVisibility(LinearLayout.VISIBLE);
                    llClose.setVisibility(LinearLayout.VISIBLE);
                    llErsal.setVisibility(LinearLayout.VISIBLE);
                    llSave.setVisibility(LinearLayout.VISIBLE);

                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewAgahi.this.finish();
                        Intent intent = new Intent(App.context, Main.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
                llClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewAgahi.this.finish();
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


                        if(name!= null){
                            if(price.getText().toString().matches("")){
                                txtPrice.setVisibility(View.VISIBLE);
                            }else {
                                txtPrice.setVisibility(View.GONE);
                            }

                            if(email.getText().toString().matches("")){
                                txtEmail.setVisibility(View.VISIBLE);
                            }else {
                                txtEmail.setVisibility(View.GONE);
                            }

                            if(phone.getText().toString().matches("")){
                                txtMobile.setVisibility(View.VISIBLE);
                            }else {
                                txtMobile.setVisibility(View.GONE);
                            }

                            if(SelectLocation.getText().toString().matches("انتخاب محل")){
                                txtLocation.setVisibility(View.VISIBLE);
                            }else {
                                txtLocation.setVisibility(View.GONE);
                            }

                            if(title.getText().toString().matches("")){
                                txtTitle.setVisibility(View.VISIBLE);
                            }else {
                                txtTitle.setVisibility(View.GONE);
                            }

                            if(content.getText().toString().matches("")){
                                txtContent.setVisibility(View.VISIBLE);
                            }else {
                                txtContent.setVisibility(View.GONE);
                            }

                            if(radioTypeGroup.getCheckedRadioButtonId() == -1){
                                txtType.setVisibility(View.VISIBLE);
                            }else {
                                txtType.setVisibility(View.GONE);
                            }

                        }else {

                            dialog = ProgressDialog.show(NewAgahi.this, null, null,true, false);
                            dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

                            dialog.setContentView(R.layout.progress_layout_small);

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            radioTypeButton = (RadioButton) findViewById(selectedId);

                            if(radioTypeButton!=null) {

                                type = radioTypeButton.getText().toString();
                            }
                            webServiceNewAgahi();

                        }
                    }
                });

                SelectCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.context , cats.class);
                        startActivity(intent);
                    }
                });

                SelectLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.context , Locations.class);
                        startActivity(intent);
                    }
                });
                SelectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }

            public  void webServiceNewAgahi()
            {

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
               // File myFile = new File("/path/to/file.png");

                params.put("title", title.getText()); //  ********** parametr  ersali dar surate niaz
                params.put("content", content.getText());
                params.put("price", price.getText());
                params.put("email", email.getText());
                params.put("mobile", phone.getText());
                params.put("type",type);
                params.put("category_id",id);
                Intent intent=getIntent();
                if(subid.toString() !=null){
                    params.put("subcategory_id",subid);
                }else {
                    params.put("subcategory_id","1");
                }


               // params.put("image","jja");
                params.put("deviceid",App.android_id);
                params.put("devicemodel",App.android_Model);
                params.put("location_id",Locations.id_location);
                try {
                    params.put("file", destination);
                } catch(FileNotFoundException e) {}
                Log.i("locaaa" , "loca:" + destination);

                client.post(App.urlApi+"agahis", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        dialog.hide();
                        App.CustomToast("آگهی شما با موفقیت ثبت شد و پس از بررسی منتشر خواهد شد");
                        Intent intent = new Intent(App.context, Main.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        dialog.hide();

                        if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                        {
                            App.CustomToast("خطا");

                        }else{
                            App.CustomToast(" لطفا دوباره سعی کنید ");
                            Log.i("myerror" , errorResponse.toString());
                        }
                    }
                });
            }




            private void selectImage() {
                final CharSequence[] items = { "دوربین", "گالری",
                        "انصراف" };

                AlertDialog.Builder builder = new AlertDialog.Builder(NewAgahi.this);
                builder.setTitle("اضافه کردن تصویر");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result= Utility.checkPermission(NewAgahi.this);

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
                uri = Uri.fromFile(file);

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

                       // Bundle bundle = data.getExtras();
                      //  Bitmap thumbnail = bundle.getParcelable("data");


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




                        ivImage.setVisibility(View.VISIBLE);
                        ivImage.setImageBitmap(thumbnail);

                    }
                }
            }

            public void ImageCropFunction() {

                // Image Crop Code
                try {
                    CropIntent = new Intent("com.android.camera.action.CROP");

                    CropIntent.setDataAndType(uri, "image/*");

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

                if (ActivityCompat.shouldShowRequestPermissionRationale(NewAgahi.this,
                        Manifest.permission.CAMERA))
                {

                   // Toast.makeText(NewAgahi.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(NewAgahi.this,new String[]{
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
/*
            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                switch (requestCode) {
                    case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            if(userChoosenTask.equals("دوربین"))
                                cameraIntent();
                            else if(userChoosenTask.equals("گالری"))
                                galleryIntent();
                        } else {
                            //code for deny
                        }
                        break;
                }
            }

            private void selectImage() {
                final CharSequence[] items = { "دوربین", "گالری",
                        "انصراف" };

                AlertDialog.Builder builder = new AlertDialog.Builder(NewAgahi.this);
                builder.setTitle("اضافه کردن تصویر");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result= Utility.checkPermission(NewAgahi.this);

                        if (items[item].equals("دوربین")) {
                            userChoosenTask ="دوربین";
                            if(result)
                                cameraIntent();

                        } else if (items[item].equals("گالری")) {
                            userChoosenTask ="گالری";
                            if(result)
                                galleryIntent();

                        } else if (items[item].equals("انصراف")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }

            private void galleryIntent()
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(getCropIntent(intent), "انتخاب فایل"),SELECT_FILE);

            }
            private Intent getCropIntent(Intent intent) {
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 320);
                intent.putExtra("outputY", 320);
                intent.putExtra("return-data", true);
                return intent;
            }

            private void cameraIntent()
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
               // Log.i("myfile",data.toString());

                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == SELECT_FILE)
                        try {
                            onSelectFromGalleryResult(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    else if (requestCode == REQUEST_CAMERA)
                        onCaptureImageResult(data);
                }
            }

            private void onCaptureImageResult(Intent data) {
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
                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageBitmap(thumbnail);
            }

            @SuppressWarnings("deprecation")
            private void onSelectFromGalleryResult(Intent data) throws IOException {

                Bitmap bm=null;
                if (data != null) {

                    try {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    }catch (IOException e){}

                }
                if (bm != null) { // sanity check
                    File outputDir = App.context.getCacheDir(); // Activity context
                    File outputFile = File.createTempFile("image", ".jpg", outputDir); // follow the API for createTempFile

                    FileOutputStream stream = new FileOutputStream (outputFile, false); // Add false here so we don't append an image to another image. That would be weird.
                    // This line actually writes a bitmap to the stream. If you use a ByteArrayOutputStream, you end up with a byte array. If you use a FileOutputStream, you end up with a file.
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    stream.close(); // cleanup

                    destination = new File(outputFile.toURI());

                }

                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageBitmap(bm);
            }
*/

