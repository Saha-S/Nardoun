package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.Utility;
import com.appmagazine.nardoon.fragments.NiniAx;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

import static junit.framework.Assert.assertEquals;

public class NewNini extends AppCompatActivity {

    EditText edtName,edtAge;
    TextView txtName, txtAge,txtImg;
    public static String name , id , type,subid ;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    public static ProgressDialog dialog;
    public static Button SelectImage;
    private ImageView ivImageAsli;
    private ImageButton imgDelete1;
    LinearLayout imgAsli ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    File destination ;
    private static int REQUEST_PICTURE = 1;
    private static int REQUEST_CROP_PICTURE = 2;
    ImageView image;
    File file;
    Uri uri , uri2;
    Intent CamIntent, GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    DisplayMetrics displayMetrics ;
    int width, height;
    boolean flag1,flag2,flag3,flag4,flag5,flag6,flag7,flag8=false;
    File file1 , file2,file3,fileAsli;
    CheckBox chkLink;
    CheckBox chkSpecial;
    int AgahiPrice ,linkPrice , specialPrice;
    Button SelectCat;
    private String id_confirmaation;
    private Uri mCropImageUri;

    CropImageView imgCrop;
    private File auxFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_nini);

        SelectImage = (Button) findViewById(R.id.btn_select_image);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtAge = (EditText) findViewById(R.id.edt_age);

        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
        LinearLayout llClose = (LinearLayout) findViewById(R.id.ll_close);
        ImageButton close = (ImageButton) findViewById(R.id.close);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);


        imgAsli = (LinearLayout) findViewById(R.id.img_asli);
        ivImageAsli = (ImageView) findViewById(R.id.ivImage_asli);
        imgDelete1 = (ImageButton) findViewById(R.id.img_del1);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtAge = (TextView) findViewById(R.id.txt_age);
        txtImg = (TextView) findViewById(R.id.txt_img_asli);


        llClose.setVisibility(LinearLayout.VISIBLE);
        llErsal.setVisibility(LinearLayout.VISIBLE);


        EnableRuntimePermission();

        imgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAsli.setVisibility(View.GONE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewNini.this.finish();
                Intent intent = new Intent(App.context, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewNini.this.finish();
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
                    flag2=true;

                }else {
                    txtAge.setVisibility(View.GONE);
                    flag2 = false;
                }

                if(imgAsli.getVisibility()==View.GONE ){
                    txtImg.setVisibility(View.VISIBLE);
                    flag3=true;
                }else{
                    txtImg.setVisibility(View.GONE);
                    flag3=false;
                }


                if (flag1 == false && flag2 == false && flag3 == false ) {

                    dialog = ProgressDialog.show(NewNini.this, null, null, true, false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.progress_layout_small);

                    SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                    SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                    String status = prefs2.getString("islogin", "0");
                    String id_confirmaationSH = prefs.getString("id_confirmaation", "0");
                    Log.i("iddddd" , id_confirmaationSH.toString());

                    if (status.matches("1") && !id_confirmaationSH.equals("0")) {

                        id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");
                        Log.i("iddddd2" , id_confirmaation.toString());

                        webServiceNewAgahi();
                    }else {
                        dialog.hide();
                        Intent intent = new Intent(App.context, Login.class);
                        startActivity(intent);
                    }

                }
            }
        });

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);

            }
        });

    }

    public  void webServiceNewAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("validity","0");
        params.put("point","0");
        params.put("pointm","0");
        params.put("commment","-");

        params.put("name", edtName.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("age", edtAge.getText());
        params.put("confirmation_id", id_confirmaation);

        try {
            params.put("file", file1);
        } catch(FileNotFoundException e) {}


        client.post(App.urlApi+"nini", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();
              //  String value = new String(response);
                Intent intent = new Intent(App.context, MyNini.class);
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




    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);



            destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            Uri uri = Uri.fromFile(destination);


            auxFile = new File(uri.getPath());
            assertEquals(destination.getAbsolutePath(), auxFile.getAbsolutePath());


            if (resultCode == RESULT_OK) {




                if (imgAsli.getVisibility() == View.GONE) {

                    imgAsli.setVisibility(View.VISIBLE);
                    file1 = destination;
                    Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    ivImageAsli.setImageBitmap(reducedSizeBitmap);

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


                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            //  Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setBorderLineColor(Color.RED)
                .setGuidelinesColor(Color.GREEN)
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setFixAspectRatio(true)
                .setRequestedSize(600,600)
                .start(this);
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(NewNini.this,
                Manifest.permission.CAMERA))
        {

        } else {

            ActivityCompat.requestPermissions(NewNini.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

}
