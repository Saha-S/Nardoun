package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.DetailsImagePagerAdapter;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.R;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

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
    public static String url, catname , mobile , email , price , image , location,time ,special , link , start , end  , order , allPrice;
    public static int idRadio  ;
    ImageView  ivshare ;
    ToggleButton ivFavorites ;
    Button btnDelete, btnEdit;
    CollapsingToolbarLayout collapsingToolbar;
    public static ProgressDialog dialog;
    public static String idAgahi , url1 , url2 , url3 ;
    Context context;
    private ArrayList<String> ids;
    LinearLayout llVizhe ,llPrice, llType , llOrder, llLink , llDetails;
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
    SliderLayout sliderShow;
    HashMap<String,String> url_maps;
    private Button btnsabt;
    private TextView totalprice;
    private LinearLayout container;
    public static  JSONArray finalJson;
    String[] factorarray;
    private JSONArray menuJsonArray;
    private JSONArray orderJsonArray = new JSONArray();
    int index ,num2;
    TextView txtTime;
    EditText address;

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

        url = App.urlApi + "agahis/" + idAgahi;
        idAgahi = intent.getStringExtra("id");
        webServiceGetAgahi();


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
        llOrder = (LinearLayout) findViewById(R.id.ll_order);
        llDetails = (LinearLayout) findViewById(R.id.ll_details);
        pay = (Button) findViewById(R.id.btn_pay);
        btnsabt = (Button) findViewById(R.id.btn_sabt);
        totalprice = (TextView) findViewById(R.id.txt_all_price);
        container = (LinearLayout) findViewById(R.id.container);
        txtTime = (TextView) findViewById(R.id.txt_time);
        address = (EditText) findViewById(R.id.edt_address);


        sliderShow = (SliderLayout) findViewById(R.id.slider);
        sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
       // sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);

        sliderShow.stopAutoCycle();


        url_maps = new HashMap<String, String>();

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
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                String status = prefs2.getString("islogin", "0");
                String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                if (status.matches("1") && !id_confirmaationSH.equals("0")) {
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
                    finish();
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
                MyAgahis.h.sendEmptyMessage(0);
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
                        tvLink.setMovementMethod(LinkMovementMethod.getInstance());

                        String text = "<a href='"+obj.getString("link")+"'> "+obj.getString("link")+" </a>";
                        tvLink.setText(Html.fromHtml(text));

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
                        llOrder.setVisibility(View.VISIBLE);
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

                    DefaultSliderView SliderView = new DefaultSliderView(Details.this);
                 /*   if(url1.equals("0") ){
                        SliderView
                                .image(R.drawable.nopic);
                    }
*/
                    if(url1.equals("0") && url2.equals("0") && url3.equals("0")){
                        int images[] = {R.drawable.nopic};
                        sliderShow.setPagerTransformer(false, new BaseTransformer() {

                            @Override
                            public void onTransform(View view, float position) {
                            }

                        });
                        for(int i = 0 ; i<images.length ; i++){

                            SliderView
                                    .image(images[i]);

                        }
                        sliderShow.addSlider(SliderView);



                    }else {
                        if(url2.equals("0") && url3.equals("0")){
                            String images[] = {App.urlimages+url1};
                            sliderShow.setPagerTransformer(false, new BaseTransformer() {

                                @Override
                                public void onTransform(View view, float position) {
                                }

                            });
                            for(int i = 0 ; i<images.length ; i++){

                                SliderView
                                        .image(images[i]);

                            }
                            sliderShow.addSlider(SliderView);


                        }


                        if (url2.equals("0") && !url3.equals("0")) {
                            url_maps.put("1", App.urlimages + url1);
                            url_maps.put("2", App.urlimages + url3);

                        }
                        if (!url2.equals("0") && url3.equals("0")) {
                            url_maps.put("1", App.urlimages + url1);
                            url_maps.put("2", App.urlimages + url2);

                        }
                        if (!url2.equals("0") && !url3.equals("0")) {
                            url_maps.put("1", App.urlimages + url1);
                            url_maps.put("2", App.urlimages + url2);
                            url_maps.put("3", App.urlimages + url3);


                        }
                        for (String name : url_maps.keySet()) {


                            DefaultSliderView textSliderView = new DefaultSliderView(Details.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(url_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            sliderShow.addSlider(textSliderView);
                        }

                    }
                    price = intent.getStringExtra("price");
                    time = intent.getStringExtra("time");
                    tvlocation               .setText(location);
                    tvtime               .setText(time);
                    Log.i("imageee","image : "+ App.urlimages+intent.getStringExtra("image"));
                    tvtitle               .setText(intent.getStringExtra("title"));
                    Locale farsi = new Locale("fa", "IR");
                    NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

                    String c = numberFormatDutch.format(new BigDecimal(price.toString()));
                    String cc = c.replace("ریال",   " تومان " + "\u200e") ;
                    tvprice               .setText(cc);

                    getValidity();

//////////////////////////////////////// ORDER///////////////////////////////////////////////////
                    try {

                        txtTime.setText("ثبت سفارش از ساعت " + start + " الی " + end + " امکان پذیر می باشد.");
                        try {
                            menuJsonArray = new JSONArray(menuOrder);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            for (int i = 0; i < menuJsonArray.length(); i++) {


                                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
                                final View addView = layoutInflater.inflate(R.layout.order_row, null);
                                final TextView txtName = (TextView) addView.findViewById(R.id.txtName);
                                final TextView txtPrice = (TextView) addView.findViewById(R.id.txtPrice);
                                final TextView txtNumber = (TextView) addView.findViewById(R.id.txt_food_number);
                                final TextView txtTotalPrice = (TextView) addView.findViewById(R.id.txt_all_price);
                                final ImageView ivPlus = (ImageView) addView.findViewById(R.id.iv_plus);
                                final ImageView ivMinus = (ImageView) addView.findViewById(R.id.iv_minus);
                                final ToggleButton ivSelect = (ToggleButton) addView.findViewById(R.id.iv_select);


                                final JSONObject oo = menuJsonArray.getJSONObject(i);

                                txtName.setText(oo.getString("name").toString());
                                txtPrice.setText(oo.getString("price").toString() + " تومان ");


                                //---

                                final JSONObject object = new JSONObject();

                                object.put("name", oo.getString("name").toString());
                                object.put("price", oo.getString("price").toString());
                                object.put("number", "0");

                                orderJsonArray.put(object);
                                //---

                                ivSelect.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        if (!ivSelect.isChecked()) {
                                            ivSelect.setChecked(false);
                                            ivMinus.setClickable(false);
                                            ivPlus.setClickable(false);
                                            try {
                                                index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                                                orderJsonArray.getJSONObject(index).put("number", "0");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            settotalprice();

                                        } else {
                                            ivSelect.setChecked(true);
                                            ivMinus.setClickable(true);
                                            ivPlus.setClickable(true);
                                            final int num = Integer.parseInt(txtNumber.getText().toString());
                                            num2 = num;
                                            try {
                                                index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                                                orderJsonArray.getJSONObject(index).put("number", num + "");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            settotalprice();

                                            ivPlus.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    try {

                                                        index = ((LinearLayout) addView.getParent()).indexOfChild(addView);

                                                        num2 = orderJsonArray.getJSONObject(index).getInt("number") + 1;
                                                        txtNumber.setText(String.valueOf(num2));
                                                        txtPrice.setText(num2 * orderJsonArray.getJSONObject(index).getInt("price") + " تومان ");
                                                        orderJsonArray.getJSONObject(index).put("number", num2 + "");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    settotalprice();
                                                }
                                            });

                                            ivMinus.setOnClickListener(new View.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void onClick(View v) {
                                                    try {
                                                        index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                                                        num2 = orderJsonArray.getJSONObject(index).getInt("number");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    ;


                                                    if (num2 >= 1) {

                                                        try {
                                                            index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                                                            num2 = orderJsonArray.getJSONObject(index).getInt("number") - 1;
                                                            txtNumber.setText(String.valueOf(num2));

                                                            if (num2 >= 1) {

                                                                txtPrice.setText(num2 * orderJsonArray.getJSONObject(index).getInt("price") + " تومان ");
                                                            }

                                                            orderJsonArray.getJSONObject(index).put("number", num2 + "");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }


                                                        settotalprice();
                                                    }


                                                }
                                            });

                                        }

                                    }
                                });


                                container.addView(addView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        btnsabt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                final JSONArray finalJsonArray = new JSONArray();
                                finalJson = finalJsonArray;

                                for (int i = 0; i < orderJsonArray.length(); i++) {

                                    try {

                                        if (orderJsonArray.getJSONObject(i).getInt("number") > 0) {

                                            finalJsonArray.put(orderJsonArray.getJSONObject(i));
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                                SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                                String status = prefs2.getString("islogin", "0");
                                String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                                if (status.matches("1")) {
                                    id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Details.this);

//                // set title
//                alertDialogBuilder.setTitle("-");

                                    // set dialog message
                                    if(address.getText().length()<10){
                                        App.CustomToast("لطفا یک آدرس معتبر وارد نمایید.");
                                    }else {

                                        alertDialogBuilder
                                                .setMessage("سفارش شما قابل حذف یا ویرایش نیست . آیا مطمئن هستید؟")
                                                .setCancelable(true)
                                                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        webServiceNewOrder(finalJsonArray);


                                                    }
                                                })
                                                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        alertDialog.show();
                                    }
                                } else {
                                    Intent intent = new Intent(App.context, Login.class);
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                    catch (Exception e){}
//////////////////////////////////////// ORDER///////////////////////////////////////////////////


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
               //     App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
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
                .setDescription("پرداخت به ناردون")
                .setEmail("info@nardoun.ir")                     //  This field is not necessary.
                .setMobile("09175006484")                               //  This field is not necessary.
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
        }else {
            params.put("image", url1);
            Log.i("aaaaa" , url1);

        }
        if(url2.toString().equals("0")){
            params.put("imagei", "0");
            Log.i("aaaaa0" , url2);

        }else {
            params.put("imagei", url2);

        }
        if(url3.toString().equals("0")){
            params.put("imageii", "0");

        }else {
            params.put("imageii", url2);

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
                   // App.CustomToast("ویرایش با خطا مواجه شد !");

                }else{
                    dialog.hide();
                  //  App.CustomToast(" ویرایش نشد ");
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
                  //  App.CustomToast("ثبت اطلاعات خرید با مشکل مواجه شد !");

                }else{
                    dialog.hide();
                  //  App.CustomToast("fail "+statusCode);
                 //   App.CustomToast("اطلاعات خرید ثبت نشد ");
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
                }else {
                    SpecialPrice=0;
                }

                if(catname.toString().equals("استخدام و کاریابی")){
                    EstekhdamPrice=App.priceEstekhdam;
                }
                else {
                    EstekhdamPrice=0;

                }

                if(catname.toString().equals("رستوران")){
                    RestaurantPrice=App.priceRestaurant;
                }else {
                    RestaurantPrice=0;

                }

                if(!link.equals("")){
                    LinkPrice=App.priceLink;
                }else {
                    LinkPrice=0;
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
                    txt.setText(R.string.rad   );
                    txt.setText(txt.getText()+ " - " + why   );
                    pay.setVisibility(View.GONE);
                }
                if(validity.equals("3")) {
                    llVizhe.setBackgroundColor(Color.parseColor("#ffe6cc"));
                    txt.setText(R.string.entezarpardakht);
                    pay.setVisibility(View.VISIBLE);
                }
                if(validity.equals("4")) {
                    llVizhe.setBackgroundColor(Color.parseColor("#ffc2b3"));
                    txt.setText("آگهی شما منقضی شده است.");
                    pay.setVisibility(View.GONE);
                    btnEdit.setText("تمدید");
                }

            }

        } catch (Exception e) {
            llVizhe.setVisibility(View.GONE);
        }

        ////////////////////////END if validity/////////////////////////
    }
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    public void settotalprice(){



        int allprice = 0;

        for (int i=0; i<orderJsonArray.length(); i++){

            try {

                if(orderJsonArray.getJSONObject(i).getInt("number")>0){

                    allprice +=orderJsonArray.getJSONObject(i).getInt("number") * orderJsonArray.getJSONObject(i).getInt("price");
                }



            }catch (JSONException e){e.printStackTrace();}


        }

        totalprice.setText(allprice+"");
        allPrice= String.valueOf(allprice);


    }

    public  void webServiceNewOrder(JSONArray j)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("agahi_id",Details.idAgahi);
        params.put("order", j);
        params.put("price", allPrice);
        params.put("confirmation_id", id_confirmaation);
        params.put("address", address.getText().toString());


        client.post(App.urlApi+"factor", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Details.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();
                String value = new String(response);
                factorarray = value.split("-");

                Intent intent = new Intent(App.context, MyOrders.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("confirmation_id",id_confirmaation);
                intent.putExtra("order",finalJson.toString());
                intent.putExtra("factor_id",factorarray[1]);
                intent.putExtra("factor_name",factorarray[0]);
                intent.putExtra("price",allPrice);
                intent.putExtra("address",address.getText().toString());

                startActivity(intent);
                App.CustomToast("سفارش شما ثبت شد");

                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();

                if(statusCode==403)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("ثبت سفارش در این زمان امکان پذیر نمی باشد");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }


}
