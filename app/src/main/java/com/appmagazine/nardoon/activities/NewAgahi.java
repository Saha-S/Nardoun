package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.appmagazine.nardoon.BeginTimePickerFragment2;
import com.appmagazine.nardoon.FinishTimePickerFragment;
import com.appmagazine.nardoon.FinishTimePickerFragment2;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.BeginTimePickerFragment;
import com.appmagazine.nardoon.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static junit.framework.Assert.assertEquals;

public class NewAgahi extends AppCompatActivity {

    EditText price,email,phone , title , content , link;
    TextView txtPrice,txtEmail,txtMobile , txtTitle, txtContent , txtType,txtLocation,txtCat,location,txtImg;
    public static String name , id , type,subid ;
    RadioGroup radioTypeGroup;
    RadioButton radioTypeButton;
    public static ProgressDialog dialog;
    public static Button SelectImage;
    private ImageView ivImageAsli, ivImage2, ivImage3 , ivGone1 , ivGone2, ivGone3;
    private ImageButton imgDelete1, imgDelete2, imgDelete3;
    LinearLayout imgAsli , img2,img3;
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
    boolean flag1,flag2,flag3,flag4,flag5,flag6,flag7,flag8 , flag10, flag11, flag12=false;
    File file1 , file2,file3,fileAsli;
    CheckBox chkLink , chkTavafoqi;
    CheckBox chkSpecial;
    int AgahiPrice ,linkPrice , specialPrice;
    Button SelectCat , btnFinish , btnBegin , btnFinishn , btnBeginn ;
    ImageButton btnAdd;
    TextView txtFinish , txtBegin,txtFinishn , txtBeginn, txtBeginTime , txtFinishTime, txtBeginTimen , txtFinishTimen;
    EditText foodName , foodPrice;
    LinearLayout container , llMenu;
    JSONArray menuJsonArray;
    public static String startTime , endTime;
    public static String startTimen , endTimen;
    private int countSpecial;
    public static Handler h;
    private Uri mCropImageUri;

    CropImageView imgCrop;
    private File auxFile;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        h = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        finish();
                        break;

                }
            }

        };

        menuJsonArray = new JSONArray();

        SelectCat = (Button) findViewById(R.id.btn_cats);

        SelectImage = (Button) findViewById(R.id.btn_select_image);

        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnBegin = (Button) findViewById(R.id.btn_begin);

        btnFinishn = (Button) findViewById(R.id.btn_finish2);
        btnBeginn = (Button) findViewById(R.id.btn_begin2);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);

        price = (EditText) findViewById(R.id.edt_price);
        email = (EditText) findViewById(R.id.edt_email);
        location = (EditText) findViewById(R.id.edt_location);
        phone = (EditText) findViewById(R.id.edt_phone);
        title = (EditText) findViewById(R.id.edt_title);
        content = (EditText) findViewById(R.id.edt_content);
        link = (EditText) findViewById(R.id.edt_link);

        foodName = (EditText) findViewById(R.id.edt_food_name);
        foodPrice = (EditText) findViewById(R.id.edt_food_price);

        chkLink = (CheckBox) findViewById(R.id.chk_link);
        chkTavafoqi = (CheckBox) findViewById(R.id.chk_tavafoqi);
        chkSpecial = (CheckBox) findViewById(R.id.chk_special);

        LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
        LinearLayout llClose = (LinearLayout) findViewById(R.id.ll_close);

        LinearLayout llType = (LinearLayout) findViewById(R.id.ll_type);
        LinearLayout llPrice = (LinearLayout) findViewById(R.id.ll_price);
        LinearLayout llBegin = (LinearLayout) findViewById(R.id.ll_begin);
        LinearLayout llFinish = (LinearLayout) findViewById(R.id.ll_finish);
        LinearLayout llSpecial = (LinearLayout) findViewById(R.id.ll_special);

        ImageButton close = (ImageButton) findViewById(R.id.close);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);

        container = (LinearLayout) findViewById(R.id.container);
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);

        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);

        imgAsli = (LinearLayout) findViewById(R.id.img_asli);
        img2 = (LinearLayout) findViewById(R.id.img2);
        img3 = (LinearLayout) findViewById(R.id.img3);

        ivImageAsli = (ImageView) findViewById(R.id.ivImage_asli);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);

        imgCrop = (CropImageView) findViewById(R.id.cropImageView);


        imgDelete1 = (ImageButton) findViewById(R.id.img_del1);
        imgDelete2 = (ImageButton) findViewById(R.id.img_del2);
        imgDelete3 = (ImageButton) findViewById(R.id.img_del3);

        txtContent = (TextView) findViewById(R.id.txt_content);
        txtLocation = (TextView) findViewById(R.id.txt_location);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtCat = (TextView) findViewById(R.id.txt_cat);
        txtImg = (TextView) findViewById(R.id.txt_img_asli);

        txtBeginn = (TextView) findViewById(R.id.txt_begin2);
        txtFinishn = (TextView) findViewById(R.id.txt_finish2);

        txtBegin = (TextView) findViewById(R.id.txt_begin);
        txtFinish = (TextView) findViewById(R.id.txt_finish);

        txtBeginTimen = (TextView) findViewById(R.id.txt_begin_time2);
        txtFinishTimen = (TextView) findViewById(R.id.txt_finish_time2);

        txtBeginTime = (TextView) findViewById(R.id.txt_begin_time);
        txtFinishTime = (TextView) findViewById(R.id.txt_finish_time);


        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        String status = prefs2.getString("islogin", "0");
        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

        if (status.matches("1") && !id_confirmaationSH.equals("0")) {


            SharedPreferences prefsMobile = getSharedPreferences("MOBILE", MODE_PRIVATE);
            final String mobile = prefsMobile.getString("mobile", "0");

            phone.setText(mobile);

            content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            chkLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        link.setVisibility(View.VISIBLE);
                    }
                    else{
                        link.setVisibility(View.GONE);
                        link.setText("");
                    }
                }
            });
            chkTavafoqi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        price.setVisibility(View.GONE);
                        price.setText("-1");
                    }
                    else{
                        price.setText("");
                        price.setVisibility(View.VISIBLE);
                    }
                }
            });
            ScrollView scroll = (ScrollView) findViewById(R.id.scroll) ;


            Intent intent=getIntent();
            name = intent.getStringExtra("NAME");
            id = intent.getStringExtra("CATID");
            subid = intent.getStringExtra("SUBID");

            webServiceCountSpecial();
            EnableRuntimePermission();

            if (imgAsli.getVisibility() == View.GONE) {
                SelectImage.setText("افزودن عکس");
            } else if (imgAsli.getVisibility() == View.VISIBLE) {
                SelectImage.setText("افزودن عکسی دیگر");
            }

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row, null);
                    TextView txtName = (TextView)addView.findViewById(R.id.txtName);
                    TextView txtPrice = (TextView)addView.findViewById(R.id.txtPrice);
                    txtName.setText(foodName.getText().toString());
                    txtPrice.setText(foodPrice.getText().toString()+" تومان ");
                    ImageView buttonRemove = (ImageView)addView.findViewById(R.id.remove);

                    JSONObject object = new JSONObject();
                    try {
                        object.put("name", foodName.getText().toString());
                        object.put("price", foodPrice.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    menuJsonArray.put(object);

                    buttonRemove.setOnClickListener(new View.OnClickListener(){

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(View v) {
                            int index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                            addView.setVisibility(View.GONE);

                            try {
                                menuJsonArray.getJSONObject(index).put("name","0");
                            }catch (JSONException e){e.printStackTrace();}


                        }});

                    container.addView(addView);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(foodName.getWindowToken(), 0);
                    foodName.setText("");
                    foodPrice.setText("");
                }});





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


            if (name!= null){

                SelectCat.setText(name);
                if(SelectCat.getText().equals("رستوران")){
                    txtCat.setVisibility(View.GONE);
                    //  txtCat.setText("ثبت آگهی در این بخش رایگان نمی باشد");
                    txtCat.setVisibility(View.VISIBLE);
                    llType.setVisibility(View.GONE);
                    llPrice.setVisibility(View.GONE);
                    llBegin.setVisibility(View.VISIBLE);
                    llFinish.setVisibility(View.VISIBLE);
                    container.setVisibility(View.VISIBLE);
                    llMenu.setVisibility(View.VISIBLE);
                    llForm.setVisibility(LinearLayout.VISIBLE);
                    llClose.setVisibility(LinearLayout.VISIBLE);
                    llErsal.setVisibility(LinearLayout.VISIBLE);
                    llSpecial.setVisibility(LinearLayout.GONE);


                }else
                if(SelectCat.getText().equals("استخدام و کاریابی")) {
                    txtCat.setVisibility(View.GONE);
                    txtCat.setText("ثبت آگهی در این بخش رایگان نمی باشد");
                    txtCat.setVisibility(View.VISIBLE);
                    llType.setVisibility(View.GONE);
                    llPrice.setVisibility(View.GONE);
                    llBegin.setVisibility(View.GONE);
                    llFinish.setVisibility(View.GONE);
                    container.setVisibility(View.GONE);
                    llMenu.setVisibility(View.GONE);
                    llForm.setVisibility(LinearLayout.VISIBLE);
                    llClose.setVisibility(LinearLayout.VISIBLE);
                    llErsal.setVisibility(LinearLayout.VISIBLE);
                    llSpecial.setVisibility(LinearLayout.VISIBLE);


                }else
                {
                    txtCat.setVisibility(View.GONE);
                    llForm.setVisibility(LinearLayout.VISIBLE);
                    llClose.setVisibility(LinearLayout.VISIBLE);
                    llErsal.setVisibility(LinearLayout.VISIBLE);
                    llBegin.setVisibility(View.GONE);
                    llFinish.setVisibility(View.GONE);
                    container.setVisibility(View.GONE);
                    llMenu.setVisibility(View.GONE);
                    llSpecial.setVisibility(LinearLayout.VISIBLE);


                }

            }

            btnBeginn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new BeginTimePickerFragment2();
                    newFragment.show(getFragmentManager(),"TimePicker");

                }
            });
            btnBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new BeginTimePickerFragment();
                    newFragment.show(getFragmentManager(),"TimePicker");

                }
            });
            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new FinishTimePickerFragment();
                    newFragment.show(getFragmentManager(),"TimePicker");

                }
            });
            btnFinishn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new FinishTimePickerFragment2();
                    newFragment.show(getFragmentManager(),"TimePicker");

                }
            });


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


                    if(SelectCat.getText().equals("رستوران")){

                        if(chkSpecial.isChecked()){
                            webServiceCountSpecial();
                            if(countSpecial>=5) {
                                flag10 = true;
                                App.CustomToast("در حال حاضر امکان انتخاب آگهی ویژه برای این دسته بندی وجود ندارد .");
                            }
                        } else {
                            flag10 = false;
                        }
                        if (phone.getText().toString().matches("")) {
                            txtMobile.setVisibility(View.VISIBLE);
                            flag3 = true;
                        } else {
                            txtMobile.setVisibility(View.GONE);
                            flag3 = false;
                        }

                        if (txtBeginTime.getText().toString().matches("") ) {
                            txtBegin.setVisibility(View.VISIBLE);
                            flag1 = true;
                        } else {
                            txtBegin.setVisibility(View.GONE);
                            flag1 = false;
                        }
                        if (txtBeginTimen.getText().toString().matches("") ) {
                            txtBeginn.setVisibility(View.VISIBLE);
                            flag11 = true;
                        } else {
                            txtBeginn.setVisibility(View.GONE);
                            flag11 = false;
                        }

                        if (txtFinishTime.getText().toString().matches("")) {
                            txtFinish.setVisibility(View.VISIBLE);
                            flag7 = true;
                        } else {
                            txtFinish.setVisibility(View.GONE);
                            flag7 = false;
                        }
                        if (txtFinishTimen.getText().toString().matches("")) {
                            txtFinishn.setVisibility(View.VISIBLE);
                            flag12 = true;
                        } else {
                            txtFinishn.setVisibility(View.GONE);
                            flag12 = false;
                        }


                        if (location.getText().toString().matches("")) {
                            txtLocation.setVisibility(View.VISIBLE);
                            flag4 = true;
                        } else {
                            txtLocation.setVisibility(View.GONE);
                            flag4 = false;
                        }


                        if (title.getText().toString().matches("")) {
                            txtTitle.setVisibility(View.VISIBLE);
                            flag5 = true;
                        } else {
                            txtTitle.setVisibility(View.GONE);
                            flag5 = false;
                        }


                        if (content.getText().toString().matches("")) {
                            txtContent.setVisibility(View.VISIBLE);
                            flag6 = true;
                        } else {
                            txtContent.setVisibility(View.GONE);
                            flag6 = false;
                        }


                        if (imgAsli.getVisibility() == View.GONE && (img2.getVisibility() == View.VISIBLE || img3.getVisibility() == View.VISIBLE)) {
                            txtImg.setVisibility(View.VISIBLE);
                            flag8 = true;
                        } else {
                            txtImg.setVisibility(View.GONE);
                            flag8 = false;
                        }


                        if (flag1 == false && flag3 == false && flag4 == false && flag5 == false && flag6 == false && flag7 == false && flag8 == false && flag10 == false&& flag11 == false&& flag12 == false) {

                            dialog = ProgressDialog.show(NewAgahi.this, null, null, true, false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            dialog.setContentView(R.layout.progress_layout_small);

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            radioTypeButton = (RadioButton) findViewById(selectedId);

                            if (radioTypeButton != null) {

                                type = radioTypeButton.getText().toString();
                            }
                            webServiceNewAgahi();

                        }
                    }
                    else if(SelectCat.getText().equals("استخدام و کاریابی")){

                        if(chkSpecial.isChecked()){
                            webServiceCountSpecial();
                            if(countSpecial>=5) {
                                flag10 = true;
                                App.CustomToast("در حال حاضر امکان انتخاب آگهی ویژه برای این دسته بندی وجود ندارد .");
                            }
                        } else {
                            flag10 = false;
                        }
                        if (phone.getText().toString().matches("")) {
                            txtMobile.setVisibility(View.VISIBLE);
                            flag3 = true;
                        } else {
                            txtMobile.setVisibility(View.GONE);
                            flag3 = false;
                        }


                        if (location.getText().toString().matches("")) {
                            txtLocation.setVisibility(View.VISIBLE);
                            flag4 = true;
                        } else {
                            txtLocation.setVisibility(View.GONE);
                            flag4 = false;
                        }


                        if (title.getText().toString().matches("")) {
                            txtTitle.setVisibility(View.VISIBLE);
                            flag5 = true;
                        } else {
                            txtTitle.setVisibility(View.GONE);
                            flag5 = false;
                        }


                        if (content.getText().toString().matches("")) {
                            txtContent.setVisibility(View.VISIBLE);
                            flag6 = true;
                        } else {
                            txtContent.setVisibility(View.GONE);
                            flag6 = false;
                        }


                        if (imgAsli.getVisibility() == View.GONE && (img2.getVisibility() == View.VISIBLE || img3.getVisibility() == View.VISIBLE)) {
                            txtImg.setVisibility(View.VISIBLE);
                            flag8 = true;
                        } else {
                            txtImg.setVisibility(View.GONE);
                            flag8 = false;
                        }


                        if ( flag3 == false && flag4 == false && flag5 == false && flag6 == false  && flag8 == false && flag10 == false) {

                            dialog = ProgressDialog.show(NewAgahi.this, null, null, true, false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            dialog.setContentView(R.layout.progress_layout_small);

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            radioTypeButton = (RadioButton) findViewById(selectedId);

                            if (radioTypeButton != null) {

                                type = radioTypeButton.getText().toString();
                            }
                            webServiceNewAgahi();

                        }
                    }
                    else{

                        if(chkSpecial.isChecked()){
                            webServiceCountSpecial();
                            if(countSpecial>=5) {
                                flag10 = true;
                                App.CustomToast("در حال حاضر امکان انتخاب آگهی ویژه برای این دسته بندی وجود ندارد .");
                            }
                            else {
                                flag10 = false;
                            }
                        } else {
                            flag10 = false;
                        }

                        if (price.getText().toString().matches("")) {
                            txtPrice.setVisibility(View.VISIBLE);
                            flag1 = true;

                        } else {
                            txtPrice.setVisibility(View.GONE);
                            flag1 = false;
                        }

                        if (phone.getText().toString().matches("")) {
                            txtMobile.setVisibility(View.VISIBLE);
                            flag3 = true;
                        } else {
                            txtMobile.setVisibility(View.GONE);
                            flag3 = false;
                        }


                        if (location.getText().toString().matches("")) {
                            txtLocation.setVisibility(View.VISIBLE);
                            flag4 = true;
                        } else {
                            txtLocation.setVisibility(View.GONE);
                            flag4 = false;
                        }


                        if (title.getText().toString().matches("")) {
                            txtTitle.setVisibility(View.VISIBLE);
                            flag5 = true;
                        } else {
                            txtTitle.setVisibility(View.GONE);
                            flag5 = false;
                        }


                        if (content.getText().toString().matches("")) {
                            txtContent.setVisibility(View.VISIBLE);
                            flag6 = true;
                        } else {
                            txtContent.setVisibility(View.GONE);
                            flag6 = false;
                        }


                        if (radioTypeGroup.getCheckedRadioButtonId() == -1) {
                            txtType.setVisibility(View.VISIBLE);
                            flag7 = true;
                        } else {
                            txtType.setVisibility(View.GONE);
                            flag7 = false;
                        }


                        if (imgAsli.getVisibility() == View.GONE && (img2.getVisibility() == View.VISIBLE || img3.getVisibility() == View.VISIBLE)) {
                            txtImg.setVisibility(View.VISIBLE);
                            flag8 = true;
                        } else {
                            txtImg.setVisibility(View.GONE);
                            flag8 = false;
                        }


                        if (flag1 == false && flag3 == false && flag4 == false && flag5 == false && flag6 == false && flag7 == false && flag8 == false && flag10 == false)  {

                            dialog = ProgressDialog.show(NewAgahi.this, null, null, true, false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            dialog.setContentView(R.layout.progress_layout_small);

                            int selectedId = radioTypeGroup.getCheckedRadioButtonId();
                            radioTypeButton = (RadioButton) findViewById(selectedId);

                            if (radioTypeButton != null) {

                                type = radioTypeButton.getText().toString();
                            }
                            webServiceNewAgahi();

                        }
                    }
                }
            });

            SelectCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SelectCat.getText().equals("انتخاب"))
                    {

                        Intent intent = new Intent(App.context, cats.class);

                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(App.context, cats.class);

                        startActivity(intent);
                        //    finish();
                    }
                }
            });



            SelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectImageClick(v);
                }
            });

        }else {
            Intent intent = new Intent(App.context, Login.class);
            startActivity(intent);
            finish();
        }
    }

    public  void webServiceNewAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if(chkSpecial.isChecked())
        {
            params.put("special", "1");
        }
        else {
            params.put("special", "0");
        }

        //  if(chkLink.isChecked() || chkSpecial.isChecked() || SelectCat.getText().equals("استخدام و کاریابی") || SelectCat.getText().equals("رستوران")){
        if(chkLink.isChecked() || chkSpecial.isChecked() || SelectCat.getText().equals("استخدام و کاریابی")){
            params.put("validity","3");
        }
        if(SelectCat.getText().equals("رستوران")) {
            params.put("start",startTime);
            params.put("end",endTime);
            params.put("startn",startTimen);
            params.put("endn",endTimen);
            params.put("special", "0");



            final JSONArray finalJsonArray = new JSONArray();
            try {


                for (int i=0;i<menuJsonArray.length();i++){

                    if(!menuJsonArray.getJSONObject(i).getString("name").equalsIgnoreCase("0")){

                        finalJsonArray.put(menuJsonArray.getJSONObject(i));
                    }

                }
            }catch (JSONException e){e.printStackTrace();}

            params.put("menu",finalJsonArray.toString());
            params.put("type","0");
            params.put("price","0");


        }
        if(SelectCat.getText().equals("استخدام و کاریابی")) {
            params.put("type","0");
            params.put("price","0");
        }
        else {
            params.put("price", price.getText());
            params.put("type",type);
        }

        params.put("link",link.getText());
        params.put("title", title.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("content", content.getText());
        params.put("email", email.getText());
        params.put("mobile", phone.getText());
        params.put("comment", "-");
        params.put("category_id",id);

        params.put("subcategory_id",subid);
        params.put("deviceid",App.android_id);
        params.put("devicemodel",App.android_Model);
        params.put("location",location.getText());
        try {
            params.put("file", file1);
        } catch(FileNotFoundException e) {}
        try {
            params.put("filei", file2);
        } catch(FileNotFoundException e) {}
        try {
            params.put("fileii", file3);
        } catch(FileNotFoundException e) {}

        client.post(App.urlApi+"agahis", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();
                String value = new String(response);
                Intent intent = new Intent(App.context, Details.class);
                intent.putExtra("id", value);
                intent.putExtra("statusbox", "1");
                intent.putExtra("title", title.getText().toString());
                try {
                    if(file1.toString()!="") {
                        intent.putExtra("image", App.urlimages + file1.toString());
                    }
                }catch (RuntimeException e) {
                }
                intent.putExtra("location", location.getText().toString());
                intent.putExtra("price", price.getText().toString());

                intent.putExtra("time", "لحظاتی پیش");
                intent.putExtra("permission", "1");

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


                } else if (imgAsli.getVisibility() == View.VISIBLE && img2.getVisibility() == View.GONE) {
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


    public  void webServiceCountSpecial()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"specialcount/"+id, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                countSpecial = Integer.parseInt(value);


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    //     App.CustomToast(" لطفا دوباره سعی کنید ");

                }else{
                    //  App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
