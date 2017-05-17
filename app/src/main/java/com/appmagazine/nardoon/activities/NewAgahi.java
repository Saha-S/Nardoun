package com.appmagazine.nardoon.activities;

        import android.Manifest;
        import android.app.DialogFragment;
        import android.app.ProgressDialog;
        import android.content.ActivityNotFoundException;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Environment;
        import android.support.annotation.RequiresApi;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.FileProvider;
        import android.support.v4.view.ViewPager;
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

        import com.appmagazine.nardoon.App;
        import com.appmagazine.nardoon.DetailsImagePagerAdapter;
        import com.appmagazine.nardoon.FinishTimePickerFragment;
        import com.appmagazine.nardoon.R;
        import com.appmagazine.nardoon.BeginTimePickerFragment;
        import com.appmagazine.nardoon.Utility;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;

        import cz.msebera.android.httpclient.Header;

        public class NewAgahi extends AppCompatActivity {

            EditText price,email,phone , title , content , link;
            TextView txtPrice,txtEmail,txtMobile , txtTitle, txtContent , txtType,txtLocation,txtCat,location,txtImg;
            public static String name , id , type,subid ;
            RadioGroup radioTypeGroup;
            RadioButton radioTypeButton;
            public static ProgressDialog dialog;
            public static Button SelectImage;
            private ImageView ivImageAsli, ivImage2, ivImage3;
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
            boolean flag1,flag2,flag3,flag4,flag5,flag6,flag7,flag8 , flag10=false;
            File file1 , file2,file3,fileAsli;
            CheckBox chkLink;
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


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_new);

                menuJsonArray = new JSONArray();

                SelectCat = (Button) findViewById(R.id.btn_cats);
                SelectImage = (Button) findViewById(R.id.btn_select_image);

                btnFinish = (Button) findViewById(R.id.btn_finish);
                btnBegin = (Button) findViewById(R.id.btn_begin);
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
                chkSpecial = (CheckBox) findViewById(R.id.chk_special);

                LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
                LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
                LinearLayout llClose = (LinearLayout) findViewById(R.id.ll_close);

                LinearLayout llType = (LinearLayout) findViewById(R.id.ll_type);
                LinearLayout llPrice = (LinearLayout) findViewById(R.id.ll_price);
                LinearLayout llBegin = (LinearLayout) findViewById(R.id.ll_begin);
                LinearLayout llFinish = (LinearLayout) findViewById(R.id.ll_finish);

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

                txtBegin = (TextView) findViewById(R.id.txt_begin);
                txtFinish = (TextView) findViewById(R.id.txt_finish);
                txtBeginTime = (TextView) findViewById(R.id.txt_begin_time);
                txtFinishTime = (TextView) findViewById(R.id.txt_finish_time);

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
                    ScrollView scroll = (ScrollView) findViewById(R.id.scroll) ;


                Intent intent=getIntent();
                name = intent.getStringExtra("NAME");
                id = intent.getStringExtra("CATID");
                subid = intent.getStringExtra("SUBID");

                webServiceCountSpecial();
                EnableRuntimePermission();


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
                        txtCat.setText("ثبت آگهی در این بخش رایگان نمی باشد");
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

                    }else if(SelectCat.getText().equals("استخدام و کاریابی")) {
                            txtCat.setVisibility(View.GONE);
                            txtCat.setText("ثبت آگهی در این بخش رایگان نمی باشد");
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

                    }

                }

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
                        Intent intent = new Intent(App.context , cats.class);
                        startActivity(intent);
                    }
                });

                SelectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(imgAsli.getVisibility()==View.GONE) {
                            selectImage(ivImageAsli);
                        }else  if(imgAsli.getVisibility()==View.VISIBLE && img2.getVisibility()==View.GONE) {
                            selectImage(ivImage2);
                        }else if(img2.getVisibility()==View.VISIBLE && img3.getVisibility()==View.GONE) {
                            selectImage(ivImage3);
                        }

                    }
                });

            }

            public  void webServiceNewAgahi()
            {

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

                if(chkSpecial.isChecked())
                {
                    params.put("special", "1");
                }
               if(chkLink.isChecked() || chkSpecial.isChecked() || SelectCat.getText().equals("استخدام و کاریابی") || SelectCat.getText().equals("رستوران")){
                   params.put("validity","3");
               }
                if(SelectCat.getText().equals("رستوران")) {
                    params.put("start",startTime);
                    params.put("end",endTime);


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


                }else {
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




            private void selectImage(ImageView img) {
                final CharSequence[] items = { "دوربین", "گالری",
                        "انصراف" };
                image= img;

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
            //   uri = Uri.fromFile(file);
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
                if(resultCode != RESULT_CANCELED) {

                    if (requestCode == 0 && resultCode == RESULT_OK) {

                        ImageCropFunction();

                    } else if (requestCode == 2) {

                        if (data != null) {

                            uri = data.getData();

                            ImageCropFunction();

                        }
                    } else if (requestCode == 1) {

                        if (data != null) {

                            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            thumbnail.compress(Bitmap.CompressFormat.JPEG,90, bytes);

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


                            if (imgAsli.getVisibility() == View.GONE) {
                                SelectImage.setText("افزودن عکس");
                            } else if (imgAsli.getVisibility() == View.VISIBLE) {
                                SelectImage.setText("افزودن عکسی دیگر");
                            }

                            if (imgAsli.getVisibility() == View.GONE) {
                                imgAsli.setVisibility(View.VISIBLE);
                                file1 = destination;
                            } else if (imgAsli.getVisibility() == View.VISIBLE && img2.getVisibility() == View.GONE) {
                                img2.setVisibility(View.VISIBLE);
                                file2 = destination;

                            } else if (img2.getVisibility() == View.VISIBLE && img3.getVisibility() == View.GONE) {
                                img3.setVisibility(View.VISIBLE);
                                file3 = destination;

                            }
                            image.setImageBitmap(thumbnail);
                        }
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

                if (ActivityCompat.shouldShowRequestPermissionRationale(NewAgahi.this,
                        Manifest.permission.CAMERA))
                {

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


        }
