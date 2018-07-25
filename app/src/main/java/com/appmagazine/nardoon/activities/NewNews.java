package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static junit.framework.Assert.assertEquals;


public class NewNews extends AppCompatActivity {

    EditText price,email,phone , title , content , link;
    TextView txtPrice,txtEmail,txtMobile , txtTitle, txtContent , txtType,txtLocation,txtCat,location,txtImg;
    public static String name , id , type,subid ;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    public static ProgressDialog dialog;
    public static Button SelectImage;
    private ImageView  ivImage2, ivImage3, ivImage4, ivImage5, ivImage6, ivImage7, ivImage8 , ivGone1 , ivGone2, ivGone3;
    private ImageButton  imgDelete2, imgDelete3, imgDelete4, imgDelete5, imgDelete6, imgDelete7, imgDelete8;
    LinearLayout  img2,img3,img4,img5,img6,img7,img8;
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
    boolean flag1,flag2,flag3,flag4,flag5,flag6,flag7,flag8 , flag10=false;
    File file1 , file2,file3,file4,file5,file6,file7,file8,fileAsli;
    Boolean flagImgAsli ;
    CheckBox chkLink , chkTavafoqi;
    CheckBox chkSpecial;
    int AgahiPrice ,linkPrice , specialPrice;
    Button SelectCat , btnFinish , btnBegin ;
    ImageButton btnAdd;
    TextView txtFinish , txtBegin, txtBeginTime , txtFinishTime;
    EditText foodName , foodPrice;
    LinearLayout container , llMenu;
    JSONArray menuJsonArray;
    public static String startTime , endTime;
    private int countSpecial;
    public static Handler h;
    private Uri mCropImageUri;

    CropImageView imgCrop;
    private File auxFile;
    private Spinner spinner;

    LinearLayout llImg;
    ImageView imgNews;
    private String id_confirmaation;
    LinearLayout llDelete;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_news);

        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);
        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
        llDelete = (LinearLayout) findViewById(R.id.ll_del);

        llImg = (LinearLayout) findViewById(R.id.ll_news_img);
        imgNews = (ImageView) findViewById(R.id.news_img);

        llErsal.setVisibility(View.VISIBLE);
        tvBack.setText("خبر جدید");

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

        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("انتخاب کنید..");
        list.add("سرگرمی و آموزشی");
        list.add("اجتماعی");
        list.add("ورزشی");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        title = (EditText) findViewById(R.id.edt_title);
        content = (EditText) findViewById(R.id.edt_content);


        llImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagImgAsli = false;
                CropImage.startPickImageActivity(NewNews.this);
            }
        });
        img2 = (LinearLayout) findViewById(R.id.img2);
        img3 = (LinearLayout) findViewById(R.id.img3);
        img4 = (LinearLayout) findViewById(R.id.img4);
        img5 = (LinearLayout) findViewById(R.id.img5);
        img6 = (LinearLayout) findViewById(R.id.img6);
        img7 = (LinearLayout) findViewById(R.id.img7);
        img8 = (LinearLayout) findViewById(R.id.img8);

        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);
        ivImage4 = (ImageView) findViewById(R.id.ivImage4);
        ivImage5 = (ImageView) findViewById(R.id.ivImage5);
        ivImage6 = (ImageView) findViewById(R.id.ivImage6);
        ivImage7 = (ImageView) findViewById(R.id.ivImage7);
        ivImage8 = (ImageView) findViewById(R.id.ivImage8);

        imgCrop = (CropImageView) findViewById(R.id.cropImageView);
        SelectImage = (Button) findViewById(R.id.btn_select_image);
        imgDelete2 = (ImageButton) findViewById(R.id.img_del2);
        imgDelete3 = (ImageButton) findViewById(R.id.img_del3);
        imgDelete4 = (ImageButton) findViewById(R.id.img_del4);
        imgDelete5 = (ImageButton) findViewById(R.id.img_del5);
        imgDelete6 = (ImageButton) findViewById(R.id.img_del6);
        imgDelete7 = (ImageButton) findViewById(R.id.img_del7);
        imgDelete8 = (ImageButton) findViewById(R.id.img_del8);
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
        imgDelete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img4.setVisibility(View.GONE);
            }
        });
        imgDelete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img5.setVisibility(View.GONE);
            }
        });
        imgDelete6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img6.setVisibility(View.GONE);
            }
        });
        imgDelete7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img7.setVisibility(View.GONE);
            }
        });
        imgDelete8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img8.setVisibility(View.GONE);
            }
        });
        EnableRuntimePermission();
        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        flagImgAsli = true;
                        onSelectImageClick(v);
            }
        });


        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgNews.setImageDrawable(getResources().getDrawable(R.drawable.nopic, getApplicationContext().getTheme()));
                    file1.delete();
                    llDelete.setVisibility(View.GONE);
                } else {
                    imgNews.setImageDrawable(getResources().getDrawable(R.drawable.nopic));
                    file1.delete();
                    llDelete.setVisibility(View.GONE);


                }

            }
        });

        llErsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                String status = prefs2.getString("islogin", "0");
                String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                if (status.matches("1") && !id_confirmaationSH.equals("0")) {

                    id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");


                    if(title.getText().toString().isEmpty()){
                        title.setError("عنوان خبر");
                    }
                    if(content.getText().toString().isEmpty()){
                        content.setError("متن خبر");
                    }
                    if(spinner.getSelectedItem().toString().equals("انتخاب کنید..")){
                        TextView errorText = (TextView)spinner.getSelectedView();
                        errorText.setError("-");
                        errorText.setTextColor(Color.RED);//just to highlight that this is an error
                        errorText.setText("     موضوع خبر    ");//changes the selected item text to this

                    }
                    if(!title.getText().toString().isEmpty() && !content.getText().toString().isEmpty() && !spinner.getSelectedItem().toString().equals("انتخاب کنید..") ) {
                        dialog = ProgressDialog.show(NewNews.this, null, null, true, false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.setContentView(R.layout.progress_layout_small);

                        dialog.show();
                        webServiceNewAgahi();
                    }
                }else {
                    Intent intent = new Intent(App.context, Login.class);
                    startActivity(intent);
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



                if (!flagImgAsli) {

                    imgNews.setVisibility(View.VISIBLE);
                    file1 = destination;
                    Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    imgNews.setImageBitmap(reducedSizeBitmap);
                    llDelete.setVisibility(View.VISIBLE);

                    FileOutputStream fo;
                    try {

                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        flagImgAsli = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    if (img2.getVisibility() == View.GONE) {
                        img2.setVisibility(View.VISIBLE);
                        file2 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage2.setImageBitmap(reducedSizeBitmap);

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


                    } else if (img2.getVisibility() == View.VISIBLE && img3.getVisibility() == View.GONE) {
                        img3.setVisibility(View.VISIBLE);
                        file3 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage3.setImageBitmap(reducedSizeBitmap);

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
                    } else if (img3.getVisibility() == View.VISIBLE && img4.getVisibility() == View.GONE) {
                        img4.setVisibility(View.VISIBLE);
                        file4 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage4.setImageBitmap(reducedSizeBitmap);

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
                    } else if (img4.getVisibility() == View.VISIBLE && img5.getVisibility() == View.GONE) {
                        img5.setVisibility(View.VISIBLE);
                        file5 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage5.setImageBitmap(reducedSizeBitmap);

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
                    } else if (img5.getVisibility() == View.VISIBLE && img6.getVisibility() == View.GONE) {
                        img6.setVisibility(View.VISIBLE);
                        file6 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage6.setImageBitmap(reducedSizeBitmap);

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
                    } else if (img6.getVisibility() == View.VISIBLE && img7.getVisibility() == View.GONE) {
                        img7.setVisibility(View.VISIBLE);
                        file7 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage7.setImageBitmap(reducedSizeBitmap);

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
                    } else if (img7.getVisibility() == View.VISIBLE && img8.getVisibility() == View.GONE) {
                        img8.setVisibility(View.VISIBLE);
                        file8 = destination;
                        Bitmap reducedSizeBitmap = getBitmap(result.getUri().getPath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        reducedSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        ivImage8.setImageBitmap(reducedSizeBitmap);

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

    public boolean  EnableRuntimePermission(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("tag","Permission is granted");
            return true;
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


    public  void webServiceNewAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("subject",spinner.getSelectedItem().toString());
        params.put("title",title.getText().toString());
        params.put("content",content.getText().toString());
        params.put("point","0");
        params.put("pointm","0");
        params.put("confirmation_id",id_confirmaation);
        params.put("deviceid",App.android_id);


        try {
            params.put("file", file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("filei", file2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("fileii", file3);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("fileiii", file4);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("fileiv", file5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("filev", file6);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("filevi", file7);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            params.put("filevii", file8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        client.post(App.urlApi+"news", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();
                //  String value = new String(response);
                Intent intent = new Intent(App.context, MyNews.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                App.CustomToast("خبر با موفقیت ثبت شد");
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
                    Log.i("myerror" , new String(errorResponse));
                }
            }
        });
    }


}
