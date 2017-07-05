package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.BeginTimePickerFragment;
import com.appmagazine.nardoon.FinishTimePickerFragment;
import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.Utility;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;
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

import static junit.framework.Assert.assertEquals;

public class EditAgahi extends AppCompatActivity {

    EditText price,email,phone , title , content , location ,link;
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
    boolean flag1,flag2,flag3,flag4,flag5,flag6,flag7,flag8 ,flag10=false;
    ImageView image;
    File file1 , file2 , file3;
    private LinearLayout container;
    private LinearLayout llMenu;
    private TextView txtBegin ,txtFinish ,txtBeginTime,txtFinishTime;
    JSONArray menuJsonArray;
    public static String startTime , endTime;
    ImageButton btnAdd;
    Button btnFinish , btnBegin ;
    EditText foodName , foodPrice;
    Button SelectCat;
    CheckBox chkLink , chkTavafoqi;
    CheckBox chkSpecial;
    private int countSpecial;
    private Uri mCropImageUri;

    CropImageView imgCrop;
    private File auxFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agahi);

        SelectCat = (Button) findViewById(R.id.btn_cats);
        SelectImage = (Button) findViewById(R.id.btn_select_image);
        price = (EditText) findViewById(R.id.edt_price);
        email = (EditText) findViewById(R.id.edt_email);
        location = (EditText) findViewById(R.id.edt_location);
        phone = (EditText) findViewById(R.id.edt_phone);
        title = (EditText) findViewById(R.id.edt_title);
        content = (EditText) findViewById(R.id.edt_content);
        LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
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
        tvBack.setText("ویرایش آگهی");
        radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
        final RadioButton radio1 = (RadioButton) findViewById(R.id.rb1);
        final RadioButton radio2 = (RadioButton) findViewById(R.id.rb2);
        final RadioButton radio3 = (RadioButton) findViewById(R.id.rb3);


        txtContent = (TextView) findViewById(R.id.txt_content);
        txtLocation = (TextView) findViewById(R.id.txt_location);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtType = (TextView) findViewById(R.id.txt_type);
        txtCat = (TextView) findViewById(R.id.txt_cat);
        txtImg = (TextView) findViewById(R.id.txt_img_asli);

        container = (LinearLayout) findViewById(R.id.container);
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        LinearLayout llType = (LinearLayout) findViewById(R.id.ll_type);
        LinearLayout llPrice = (LinearLayout) findViewById(R.id.ll_price);
        LinearLayout llBegin = (LinearLayout) findViewById(R.id.ll_begin);
        LinearLayout llFinish = (LinearLayout) findViewById(R.id.ll_finish);
        LinearLayout llSpecial = (LinearLayout) findViewById(R.id.ll_special);

        txtBegin = (TextView) findViewById(R.id.txt_begin);
        txtFinish = (TextView) findViewById(R.id.txt_finish);
        txtBeginTime = (TextView) findViewById(R.id.txt_begin_time);
        txtFinishTime = (TextView) findViewById(R.id.txt_finish_time);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        btnBegin = (Button) findViewById(R.id.btn_begin);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        foodName = (EditText) findViewById(R.id.edt_food_name);
        foodPrice = (EditText) findViewById(R.id.edt_food_price);

        link = (EditText) findViewById(R.id.edt_link);
        chkLink = (CheckBox) findViewById(R.id.chk_link);
        chkTavafoqi = (CheckBox) findViewById(R.id.chk_tavafoqi);
        chkSpecial = (CheckBox) findViewById(R.id.chk_special);
        webServiceCountSpecial();

        url = App.urlApi + "updateagahi/" + Details.idAgahi;
        menuJsonArray = new JSONArray();

        content.setSingleLine(false);
        content.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        title.setText(Details.tvtitle.getText());
        location.setText(Details.tvlocation.getText());
        content.setText(Details.tvcontent.getText());
        phone.setText(Details.mobile);
        email.setText(Details.email);
        link.setText(Details.link);

        if (Details.typetxt.equals("فروشی") ) {
            radio1.setChecked(true);
        }
        if (Details.typetxt.equals("درخواستی") ) {
            radio2.setChecked(true);
        }
        if (Details.typetxt.equals("معاوضه") ) {
            radio3.setChecked(true);
        }

        try {
            price.setText(Details.price);



        } catch (Exception e) {
        }

        Log.i("asasas", Details.special.toString());
        if (Details.special.equals("0")) {
            chkSpecial.setChecked(false);
        }
        if (Details.special.equals("1")) {
            chkSpecial.setChecked(true);
        }

        if (Details.link.equals("")) {
            chkLink.setChecked(false);
        }
        else {
            chkLink.setChecked(true);
            link.setVisibility(View.VISIBLE);
        }
        if (Details.price.equals("-1")) {
            chkTavafoqi.setChecked(true);
            price.setVisibility(View.GONE);

        }
        else {
            chkTavafoqi.setChecked(false);
            price.setVisibility(View.VISIBLE);
        }


        chkLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    link.setVisibility(View.VISIBLE);
                } else {
                    link.setVisibility(View.GONE);
                    link.setText("");
                }
            }
        });
        chkTavafoqi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price.setVisibility(View.GONE);
                    price.setText("-1");
                } else {
                    price.setVisibility(View.VISIBLE);
                    price.setText("");
                }
            }
        });

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


        SelectCat.setText(Details.catname);

        if (SelectCat.getText().toString().equals("رستوران")) {

            txtCat.setVisibility(View.GONE);
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


            startTime = Details.start;
            endTime = Details.end;
            txtBeginTime.setText("شروع سفارش گیری از "+startTime);
            txtFinishTime.setText("پایان سفارش گیری "+endTime);

        }else if(SelectCat.getText().equals("استخدام و کاریابی")) {
            txtCat.setVisibility(View.GONE);
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


        }


        try {
            if (Details.image.equals("0")) {
                imgAsli.setVisibility(View.GONE);
                SelectImage.setText("افزودن عکس");
            }else
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
        } catch (Exception e) {
        }


        EnableRuntimePermission();

        txtCat.setVisibility(View.GONE);
        llForm.setVisibility(LinearLayout.VISIBLE);
        llClose.setVisibility(LinearLayout.VISIBLE);
        llErsal.setVisibility(LinearLayout.VISIBLE);
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
                if (SelectCat.getText().equals("رستوران")) {
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

                    if (phone.getText().toString().matches("")) {
                        txtMobile.setVisibility(View.VISIBLE);
                        flag3 = true;
                    } else {
                        txtMobile.setVisibility(View.GONE);
                        flag3 = false;
                    }

                    if (txtBeginTime.getText().toString().matches("")) {
                        txtBegin.setVisibility(View.VISIBLE);
                        flag1 = true;
                    } else {
                        txtBegin.setVisibility(View.GONE);
                        flag1 = false;
                    }

                    if (txtFinishTime.getText().toString().matches("")) {
                        txtFinish.setVisibility(View.VISIBLE);
                        flag7 = true;
                    } else {
                        txtFinish.setVisibility(View.GONE);
                        flag7 = false;
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


                    if (flag1 == false && flag3 == false && flag4 == false && flag5 == false && flag6 == false && flag7 == false && flag8 == false && flag10 == false) {

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
                }else {
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


                    if (flag1 == false && flag3 == false && flag4 == false && flag5 == false && flag6 == false && flag7 == false && flag8 == false && flag10 == false) {
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

            }
        });

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });


        //////////////////////////////////////

        if(SelectCat.getText().equals("رستوران")) {


        try {
         //   Intent intent = getIntent();
            String order = Details.order;
            menuJsonArray = new JSONArray(order);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < menuJsonArray.length(); i++) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                final TextView txtName = (TextView) addView.findViewById(R.id.txtName);
                final TextView txtPrice = (TextView) addView.findViewById(R.id.txtPrice);

                final JSONObject oo = menuJsonArray.getJSONObject(i);

                txtName.setText(oo.getString("name").toString());
                txtPrice.setText(oo.getString("price").toString() + " تومان ");

                ImageView buttonRemove = (ImageView) addView.findViewById(R.id.remove);

                JSONObject object = new JSONObject();
                final int finalI = i;
                buttonRemove.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        int index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                        addView.setVisibility(View.GONE);

                        try {
                            menuJsonArray.getJSONObject(index).put("name","0");
                        }catch (JSONException e){e.printStackTrace();}


                    }
                });


                container.addView(addView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /////////////////////////////////////

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                TextView txtName = (TextView) addView.findViewById(R.id.txtName);
                TextView txtPrice = (TextView) addView.findViewById(R.id.txtPrice);
                txtName.setText(foodName.getText().toString());
                txtPrice.setText(foodPrice.getText().toString() + " تومان ");
                ImageView buttonRemove = (ImageView) addView.findViewById(R.id.remove);


                JSONObject object = new JSONObject();
                try {
                    object.put("name", foodName.getText().toString());
                    object.put("price", foodPrice.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                menuJsonArray.put(object);

                buttonRemove.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        int index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                        addView.setVisibility(View.GONE);

                        try {
                            menuJsonArray.getJSONObject(index).put("name","0");
                        }catch (JSONException e){e.printStackTrace();}


                    }
                });

                container.addView(addView);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(foodName.getWindowToken(), 0);
                foodName.setText("");
                foodPrice.setText("");
            }
        });


    }

    }

    public  void webServiceEditAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(chkSpecial.isChecked())
        {
            params.put("special", "1");
        }else {
            params.put("special", "0");
        }

        params.put("title", title.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("content", content.getText());
        params.put("link", link.getText());
        params.put("deviceid", App.android_id);
        params.put("devicemodel", App.android_Model);
        params.put("email", email.getText());
        params.put("mobile", phone.getText());
        if(SelectCat.getText().equals("رستوران")) {
            params.put("start",startTime);
            params.put("end",endTime);
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


        if((Details.catId.equals("10") || Details.catId.equals("9"))  ){
            if(Details.validity.equals("3")) {
                params.put("validity", "3");
            }else{
                if((Details.special.equals("1") && chkSpecial.isChecked()) ||(Details.link.equals("")&& chkLink.isChecked())){
                    params.put("validity","3");
                }else {
                    params.put("validity", "0");
                }

            }
        }else{
            if((Details.special.equals("1") && chkSpecial.isChecked()) ||(Details.link.equals("")&& chkLink.isChecked())){
                params.put("validity","3");
            }else {
                params.put("validity", "0");
            }

        }
        
        params.put("location",location.getText());
        if(file1!=null){
            try {
                Log.i("fiiiil133", file1.toString());

                params.put("file", file1);
            } catch(FileNotFoundException e) {}
        }
        else if(file2!=null){
        try {
            params.put("filei", file2);
        } catch(FileNotFoundException e) {}
    }

     else if(file3!=null){
        try {
            params.put("fileii", file3);
        } catch(FileNotFoundException e) {}
    }
         if(file1==null && imgAsli.getVisibility()==View.VISIBLE){
            params.put("image", Details.image);
        }
         if(file2==null && img2.getVisibility()==View.VISIBLE){
            params.put("imagei", Details.url2);
        }
         if(file3==null && img3.getVisibility()==View.VISIBLE){
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
                Intent intent = new Intent(App.context , MyAgahis.class);
                try {
                    MyAgahis.h.sendEmptyMessage(0);
                }catch (RuntimeException r){}
                // intent.putExtra("id", Details.idAgahi+"");
                startActivity(intent);
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
                    App.CustomToast("لطفا دوباره سعی کنید");

                }else{
                    dialog.hide();
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
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
                    SelectImage.setText("افزودن عکس");
                } else if (imgAsli.getVisibility() == View.VISIBLE) {
                    SelectImage.setText("افزودن عکسی دیگر");
                }



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

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditAgahi.this,
                Manifest.permission.CAMERA))
        {

        } else {

            ActivityCompat.requestPermissions(EditAgahi.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }


    public  void webServiceCountSpecial()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"specialcount/"+Details.catId, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
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
