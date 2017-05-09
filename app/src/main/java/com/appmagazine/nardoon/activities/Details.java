package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.DetailsImagePagerAdapter;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import ir.moslem_deris.apps.zarinpal.PaymentBuilder;
import ir.moslem_deris.apps.zarinpal.ZarinPal;
import ir.moslem_deris.apps.zarinpal.enums.ZarinPalError;
import ir.moslem_deris.apps.zarinpal.listeners.OnPaymentListener;
import ir.moslem_deris.apps.zarinpal.models.Payment;

public class Details extends AppCompatActivity {
    boolean FLAG_COLLAPSED = true;
    public static String validity,permission;
    public static TextView tvtitle,tvcontent,tvprice,tvlocation , tvtime ,tvtype ,tvLink , txt;
    public static String url, catname , mobile , email , price , image , location,time ,special , link , start , end  , order;
    public static int idRadio  ;
    ImageView  ivshare ;
    ToggleButton ivFavorites ;
    Button btnDelete, btnEdit;
    CollapsingToolbarLayout collapsingToolbar;
    public static ProgressDialog dialog;
    public static String idAgahi , url1 , url2 , url3 ;
    Context context;
    private ArrayList<String> ids;
    LinearLayout llVizhe ,llPrice, llType , llMenu , llLink , llDetails;
    ViewPager viewPager;
    Button pay ,btnMenu;
    int AgahiPrice,EstekhdamPrice , LinkPrice , SpecialPrice  , RestaurantPrice= 0;
    String id_confirmaation , peygiri , linkPos, specialPos , catPos , foodPos;
    private String why;
    private String menuOrder ;
    DetailsImagePagerAdapter myCustomPagerAdapter;
    String[] favoritearray;
    FileOperations file;
    public static String catId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        context = this;
        dialog = ProgressDialog.show(Details.this, null, null, true, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_layout_small);

        final Intent intent = getIntent();
        idAgahi=intent.getStringExtra("id");
        location=intent.getStringExtra("location");
        permission = intent.getStringExtra("permission");
        file = new FileOperations();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        tvtitle = (TextView) findViewById(R.id.txtTitle);
        tvlocation = (TextView) findViewById(R.id.txtLocation);
        tvprice = (TextView) findViewById(R.id.txtPrice);
        tvtype = (TextView) findViewById(R.id.txtType);
        tvcontent = (TextView) findViewById(R.id.txtContent);
        tvtime = (TextView) findViewById(R.id.txtTime);
        tvLink = (TextView) findViewById(R.id.txtLink);
        txt = (TextView) findViewById(R.id.txt);
        // ivtitle=(ImageView) findViewById(R.id.iv_title);
        ivshare = (ImageView) findViewById(R.id.iv_share);
        ivFavorites = (ToggleButton) findViewById(R.id.iv_favorites);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        llVizhe = (LinearLayout) findViewById(R.id.llVizhe);
        llPrice = (LinearLayout) findViewById(R.id.ll_price);
        llType = (LinearLayout) findViewById(R.id.ll_type);
        llLink = (LinearLayout) findViewById(R.id.ll_link);
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        llDetails = (LinearLayout) findViewById(R.id.ll_details);
        pay = (Button) findViewById(R.id.btn_pay);
        btnMenu = (Button) findViewById(R.id.btn_menu);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab_call);
        getValidity();

        /////////////////////////////////// FAVORITE
        String favoritestr =file.read("favorite");
        favoritearray=favoritestr.split("-");


        if(Arrays.asList(favoritearray).contains(idAgahi+"")){
            ivFavorites.setChecked(true);
        }
        //////////////////////////////////
        String typetxt = tvtype.getText().toString();
        if (typetxt == "فروشی") {idRadio = 0;}
        if (typetxt == "درخواستی") {idRadio = 1;}

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(App.context, Call.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context, Order.class);
                intent.putExtra("menu" , menuOrder);
                intent.putExtra("start" , start);
                intent.putExtra("end" , end);
                startActivity(intent);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                String status = prefs2.getString("islogin", "0");
                String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                if (status.matches("1")) {
                    id_confirmaation=id_confirmaationSH.replace("[{\"id\":", "").replace("}]" , "");
                    pay();
                }else {
                    Intent intent = new Intent(App.context, Login.class);
                    startActivity(intent);
                }
            }
        });

        ivshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(

                        android.content.Intent.ACTION_SEND);

                i.setType("text/plain");

                i.putExtra(

                        android.content.Intent.EXTRA_TEXT, "http://www.nardoun.ir");

                startActivity(Intent.createChooser(

                        i,

                        "اشتراک گذاری ناردون"));
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

//                // set title
//                alertDialogBuilder.setTitle("-");

                // set dialog message
                alertDialogBuilder
                        .setMessage("آیا میخواهید آگهی خود را حذف کنید؟")
                        .setCancelable(true)
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                webServiceDeleteAgahi();

                            }
                        })
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

                TextView msgtv = (TextView) alertDialog.findViewById(android.R.id.message);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
                //alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(Font_Far_Koodak);
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);
                msgtv.setTextSize(19);


            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(permission.equals("1")){
                    Intent intent = new Intent(App.context, EditAgahi.class);
                    startActivity(intent);
                }else {
                    App.CustomToast("ویرایش آگهی 2 روز پس از ایجاد آگهی ممکن میباشد.");
                }
            }
        });


        ivFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivFavorites.isChecked()== false) {

                    String likes= "";
                for (int i=0;i<favoritearray.length;i++) {
                    if (!favoritearray[i].equals( idAgahi )) {
                        if(likes.equals(""))
                            likes+=favoritearray[i];
                        else
                            likes+="-"+favoritearray[i];

                    }
                }
                file.write("favorite" , likes);

            }else{
                    App.CustomToast("آگهی نشان شد");
                    String likes = file.read("favorite");
                if(likes.equals("")) {
                    file.write("favorite", idAgahi);
                }else {
                    file.write("favorite",likes +"-"+idAgahi);
                }
            }


        }
        });

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("??");
                finish();
            }
        });


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);


        url = App.urlApi + "agahis/" + idAgahi;
        Log.i("aaaaurl" , url);
        idAgahi = intent.getStringExtra("id");
        webServiceGetAgahi();


    }

    public  void webServiceDeleteAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.delete(url, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {


            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                App.CustomToast(" آگهی با موفقیت حذف شد. ");
                Intent intent = new Intent(App.context, MyAgahis.class);
                startActivity(intent);
                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
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





    public  void webServiceGetAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(url, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                try {
                    JSONObject obj =new JSONArray(value).getJSONObject(0);  //********* chon ye json array ba 1 json objecte injur migirimesh

                    if(obj.getString("link").isEmpty())
                    {
                        llLink.setVisibility(View.GONE);
                    }else{
                        llLink.setVisibility(View.VISIBLE);
                        tvLink.setText(obj.getString("link"));
                        tvLink.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    String content= obj.getString("content");
                    String type= obj.getString("type");
                    catname= obj.getString("category_name");
                    if(catname.equals("استخدام و کاریابی")){
                        catPos = "1";
                    }else{
                        catPos = "0";
                    }
                    if(catname.equals("رستوران")){
                        llPrice.setVisibility(View.GONE);
                        llType.setVisibility(View.GONE);
                        llMenu.setVisibility(View.VISIBLE);
                        foodPos = "1";
                    }else{
                        foodPos = "0";
                    }
                    email= obj.getString("email");
                    mobile= obj.getString("mobile");
                    special= obj.getString("special");
                    if(special.equals("1")){
                        specialPos = "1";
                    }else{
                        specialPos = "0";
                    }
                    link= obj.getString("link");
                    if(!link.equals("")){
                        linkPos = "1";
                    }else{
                        linkPos = "0";
                    }

                    catId=obj.getString("category_id");
                    url1= obj.getString("image");
                    url2= obj.getString("imagei");
                    url3= obj.getString("imageii");
                    validity= obj.getString("validity");
                    collapsingToolbar.setTitle("");

                    tvcontent.setText(content);
                    tvtype.setText(type);


                    Intent intent=getIntent();
                    image = intent.getStringExtra("image");
                    why = intent.getStringExtra("comment");
                    try {
                        menuOrder = obj.getString("menu");
                        order = menuOrder.toString();
                        start = obj.getString("start");
                        end = obj.getString("end");

                    }catch (JSONException e){}

                    if(url2.equals("0") && url3.equals("0")){
                        String images[] = {App.urlimages+url1};
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                        viewPager.setAdapter(myCustomPagerAdapter);
                    }
                    if(url2.equals("0") && !url3.equals("0")){
                        String images[] = {App.urlimages+url1,App.urlimages+url3};
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                    }
                    if(!url2.equals("0") && url3.equals("0")){
                        String images[] = {App.urlimages+url1,App.urlimages+url2};
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                        viewPager.setAdapter(myCustomPagerAdapter);
                    }
                    if(!url2.equals("0") && !url3.equals("0")){
                        final  String images[] = {App.urlimages+url1,App.urlimages+url2,App.urlimages+url3};
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                        viewPager = (ViewPager)findViewById(R.id.viewPager);
                        myCustomPagerAdapter = new DetailsImagePagerAdapter(Details.this, images);
                        viewPager.setAdapter(myCustomPagerAdapter);
                    }


                    price = intent.getStringExtra("price");
                    time = intent.getStringExtra("time");
                    tvlocation               .setText(location);
                    tvtime               .setText(time);
                    Log.i("imageee","image : "+ App.urlimages+intent.getStringExtra("image"));
                    tvtitle               .setText(intent.getStringExtra("title"));
                    tvprice               .setText(price+" تومان ");

                    getValidity();

                    /////////////////// if Vizheee ///////////////////////////
                    ///////////////////END if Vizheee ////////////////////////

                    llDetails.setVisibility(View.VISIBLE);
                    dialog.hide();

                } catch (JSONException e1) {

                    e1.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
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
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_back, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        /*    case R.id.mShare:
                Intent  i = new Intent(
                        android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(
                        android.content.Intent.EXTRA_TEXT, "The string you want to share, which can include URLs");
                startActivity(Intent.createChooser(
                        i,
                        "Title of your share dialog"));
                        */
            case R.id.mBack:
                finish();

                break;

        }

        return super.onOptionsItemSelected(item);

    }

    private void pay(){
        Payment payment = new PaymentBuilder()
                .setMerchantID("f1bd82da-273d-11e7-9b41-005056a205be")  //  This is an example, put your own merchantID here.
                .setAmount(AgahiPrice)                                        //  In Toman
                .setDescription("پرداخت تست پلاگین اندروید")
                .setEmail("moslem.deris@gmail.com")                     //  This field is not necessary.
                .setMobile("09123456789")                               //  This field is not necessary.
                .create();

        dialog = ProgressDialog.show(Details.this, null, null, true, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_layout_small);

        ZarinPal.pay(this, payment, new OnPaymentListener() {
            @Override
            public void onSuccess(String refID) {
                dialog.hide();
                webServiceEditAgahi();
                Log.wtf("TAG", "::ZarinPal::  RefId: " + refID);
                peygiri  = refID;
                pay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(ZarinPalError error) {
                String errorMessage = "";
                dialog.hide();
                switch (error){
                    case INVALID_PAYMENT: errorMessage = "پرداخت تایید نشد";           break;
                    case USER_CANCELED:   errorMessage = "پرداخت توسط کاربر متوقف شد"; break;
                    case NOT_ENOUGH_DATA: errorMessage = "اطلاعات پرداخت کافی نیست";    break;
                    case UNKNOWN:         errorMessage = "خطای ناشناخته";              break;
                }
                Log.wtf("TAG", "::ZarinPal::  ERROR: " + errorMessage);
                //   textView.setText("خطا!!!" + "\n" + errorMessage);
            }
        });
    }

    public  void webServiceEditAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("validity","0");

        if(url1.toString().equals("0")){
            params.put("image", "0");
            Log.i("aaaaa0" , url1);

        }else {
            params.put("image", url1);
            Log.i("aaaaa" , url1);

        }
        if(url2.toString().equals("0")){
            params.put("imagei", "0");
            Log.i("aaaaa0" , url2);

        }else {
            params.put("imagei", url2);
            Log.i("aaaaa" , url2);

        }
        if(url3.toString().equals("0")){
            params.put("imageii", "0");
            Log.i("aaaaa0" , url3);

        }else {
            params.put("imageii", url2);
            Log.i("aaaaa" , url3);

        }
        client.post(App.urlApi+"updateagahi/"+idAgahi, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Details.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
                //  Intent intent = new Intent(App.context , Details.class);
                //  intent.putExtra("id", Details.idAgahi+"");
                // startActivity(intent);
                // App.CustomToast("آگهی ویرایش شد !");
                webServiceBuylog();
                //  finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast("ویرایش با خطا مواجه شد !");

                }else{
                    dialog.hide();
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" ویرایش نشد ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public  void webServiceBuylog()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("confirmation_id",id_confirmaation.toString());
        if(specialPos.equals("1")){        params.put("type","4"); }
        if(linkPos.equals("1")){        params.put("type","5"); }
        if(catPos.equals("1")){        params.put("type","6"); }
        if(foodPos.equals("1")){        params.put("type","8"); }
        if(foodPos.equals("1") && linkPos.equals("1")){        params.put("type","85"); }
        if(specialPos.equals("1") && foodPos.equals("1")){        params.put("type","84"); }
        if(specialPos.equals("1") && foodPos.equals("1")&& linkPos.equals("1")){        params.put("type","845"); }

        if(specialPos.equals("1") && linkPos.equals("1")){        params.put("type","45"); }
        if(specialPos.equals("1") && catPos.equals("1")){        params.put("type","46"); }
        if(specialPos.equals("1") && catPos.equals("1")&& linkPos.equals("1")){        params.put("type","456"); }
        params.put("related_id",idAgahi);
        params.put("description","آگهی ویژه و لینک");
        params.put("price",String.valueOf(AgahiPrice));
        params.put("traking_code",peygiri);
        params.put("isused","1");

        client.post(App.urlApi+"buylog", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Details.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
                //  Intent intent = new Intent(App.context , Details.class);
                //  intent.putExtra("id", Details.idAgahi+"");
                // startActivity(intent);
                App.CustomToast("اطلاعات خرید ثبت شد");

                //  finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast("ثبت اطلاعات خرید با مشکل مواجه شد !");

                }else{
                    dialog.hide();
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast("اطلاعات خرید ثبت نشد ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    public  void getValidity(){
        //////////////////////// if validity/////////////////////////
        Intent intent = getIntent();
        String status = intent.getStringExtra("statusbox");
        try {

            if (status.equals("1")) {

                llVizhe.setVisibility(View.VISIBLE);

                if(special.equals("1")){
                    SpecialPrice=App.priceVizhe;
                }

                if(catname.toString().equals("استخدام و کاریابی")){
                    EstekhdamPrice=App.priceEstekhdam;
                }
                if(catname.toString().equals("رستوران")){
                    RestaurantPrice=App.priceRestaurant;
                }

                if(!link.equals("")){
                    LinkPrice=App.priceLink;
                }
                AgahiPrice=LinkPrice+SpecialPrice+EstekhdamPrice+RestaurantPrice;

                if(validity.equals("1")) {
                    llVizhe.setBackgroundColor(Color.parseColor("#ccffdd"));
                    txt.setText(R.string.montasher);
                    pay.setVisibility(View.GONE);
                }
                if(validity.equals("0")) {
                    llVizhe.setBackgroundColor(Color.parseColor("#ffe6cc"));
                    txt.setText(R.string.entezar);
                    pay.setVisibility(View.GONE);
                }
                if(validity.equals("2")) {
                    llVizhe.setBackgroundColor(Color.parseColor("#ffc2b3"));
                    txt.setText(R.string.rad);
                    pay.setVisibility(View.GONE);
                }
                if(validity.equals("3")) {
                    llVizhe.setBackgroundColor(Color.parseColor("#ffe6cc"));
                    txt.setText(R.string.entezarpardakht);
                    pay.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            llVizhe.setVisibility(View.GONE);
        }

        ////////////////////////END if validity/////////////////////////
    }
}
