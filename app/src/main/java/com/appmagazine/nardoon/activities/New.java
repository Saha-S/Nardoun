package com.appmagazine.nardoon.activities;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.net.Uri;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.provider.SyncStateContract;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
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
        import android.widget.Toast;

        import com.android.camera.CropImageIntentBuilder;
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

        public class New extends AppCompatActivity {

            EditText price,email,phone , title , content;
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




                Intent intent=getIntent();
                name = intent.getStringExtra("NAME");
                id = intent.getStringExtra("CATID");
                subid = intent.getStringExtra("SUBID");
                location_id = intent.getStringExtra("location_id");

                if (name!= null){
                    SelectCat.setText(name);
                    llForm.setVisibility(LinearLayout.VISIBLE);
                    llClose.setVisibility(LinearLayout.VISIBLE);
                    llErsal.setVisibility(LinearLayout.VISIBLE);
                    llSave.setVisibility(LinearLayout.VISIBLE);
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        New.this.finish();
                        Intent intent = new Intent(App.context, Main.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
                llClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        New.this.finish();
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

                        dialog = ProgressDialog.show(New.this, null, null,true, false);
                        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

                        dialog.setContentView(R.layout.progress_layout_small);

                        int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                        radioTypeButton = (RadioButton) findViewById(selectedId);

                        if(radioTypeButton!=null) {

                            type = radioTypeButton.getText().toString();
                        }
                        webServiceNewAgahi();
                    }
                });

                SelectCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.context , Categorys.class);
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
                params.put("subcategory_id",subid);
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
                            App.CustomToast("آگهی با این شماره وجود ندارد !");

                        }else{
                            App.CustomToast("fail "+statusCode);
                            App.CustomToast(" لطفا دوباره سعی کنید ");
                            Log.i("myerror" , errorResponse.toString());
                        }
                    }
                });
            }

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

                AlertDialog.Builder builder = new AlertDialog.Builder(New.this);
                builder.setTitle("اضافه کردن تصویر");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result= Utility.checkPermission(New.this);

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


        }