package com.appmagazine.nardoon.activities;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.Adapter.MyPanelAdapter;
import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.MyPay;
import com.appmagazine.nardoon.NetUtilsCatsAgahi;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.appmagazine.nardoon.fragments.SMS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.SM;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailsSms extends AppCompatActivity {

    EditText edtMobile,edtmatn ;
    String matn;
    TextView txtCharacter , txtTedadMojaz;
    int countSMS=1;
    int credit;
    private String flag , id;
    int eteber;
    int tedadMojaz;
    private ProgressDialog dialog;
    Button send , test;
    int webCredit;
    int daemi , etebari , irancell;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_sms);

        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText("ارسال پیامک");
        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
        tvtitle.setTypeface(tfmorvarid);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        appbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTedadMojaz=(TextView) findViewById(R.id.txt_tedad_mojaz);
        txtCharacter = (TextView) findViewById(R.id.txt_character);
        edtmatn = (EditText) findViewById(R.id.edt_matn);
        send = (Button) findViewById(R.id.btn_send);
        test = (Button) findViewById(R.id.btn_test);
       // edtContent = (EditText) findViewById(R.id.edt_content);
        edtMobile = (EditText) findViewById(R.id.edt_mobile);

        Intent intent=getIntent();
        matn = intent.getStringExtra("MATN");
        flag = intent.getStringExtra("FLAG");
        id = intent.getStringExtra("ID");
        webServiceGetCredit();

        if(matn!=null) {
            edtmatn.setText(matn.toString());
        }


        TextWatcher inputTextWatcherMatn = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length()<=70){
                    txtCharacter.setText("1 پیامک / "+s.length());
                    countSMS=1;
                    mojazSms();
                }
                if(s.length()<=134 && s.length()> 70) {
                    txtCharacter.setText("2 پیامک / "+s.length());
                    countSMS = 2;
                    mojazSms();
                }
                if(s.length()<=201 && s.length()> 134) {
                    txtCharacter.setText("3 پیامک / "+s.length());
                    countSMS = 3;mojazSms();
                }
                if(s.length()<=268 && s.length()> 201) {
                    txtCharacter.setText("4 پیامک / "+s.length());
                    countSMS = 4;mojazSms();
                }
                if(s.length()<=335 && s.length()> 268) {
                    txtCharacter.setText("5 پیامک / "+s.length());
                    countSMS = 5;mojazSms();
                }
                if(s.length()<=402 && s.length()> 335) {
                    txtCharacter.setText("6 پیامک / "+s.length());
                    countSMS = 6;mojazSms();
                }
                if(s.length()<=469 && s.length()> 402) {
                    txtCharacter.setText("7 پیامک / "+s.length());
                    countSMS = 7;mojazSms();
                }
                if(s.length()<=536 && s.length()> 469) {
                    txtCharacter.setText("8 پیامک / "+s.length());
                    countSMS = 8;mojazSms();
                }
                if(s.length()<=603 && s.length()> 536) {
                    txtCharacter.setText("9 پیامک / "+s.length());
                    countSMS = 9;mojazSms();
                }
                if(s.length()<=670 && s.length()> 603) {
                    txtCharacter.setText("10 پیامک / "+s.length());
                    countSMS = 10;mojazSms();
                }
                if(s.length()<=737 && s.length()> 670) {
                    txtCharacter.setText("10 پیامک / "+s.length());
                    countSMS = 11;mojazSms();

                }
                if(s.length()>737){
                    send.setVisibility(View.GONE);
                }


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        edtmatn.addTextChangedListener(inputTextWatcherMatn);

        test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMobile.getText().toString().trim().length() < 11 || edtMobile.getText().toString().trim().length() > 11) {
                    App.CustomToast("لطفا یک شماره تلفن معتبر وارد کنید.");
                }else {
                webServiceTestSMS();
                }
            }
        });


    }

    public  void  mojazSms(){
        eteber = credit*App.priceSms;
        tedadMojaz = eteber / (countSMS*App.priceSms);
        if(tedadMojaz==0){
            txtTedadMojaz.setVisibility(View.VISIBLE);
            send.setClickable(false);
            test.setClickable(false);
        }else {
            txtTedadMojaz.setVisibility(View.GONE);
            send.setClickable(true);
            test.setClickable(true);
        }

    }
    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("ارسال انبوه به" +tedadMojaz +" شماره، آیا مطمئن هستید؟");
                alertDialogBuilder.setPositiveButton("بلی",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                send.setClickable(false);
                                webServiceSendSMS();

                            }
                        });

        alertDialogBuilder.setNegativeButton("خیر",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public  void webServiceSendSMS()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // File myFile = new File("/path/to/file.png");

        params.put("content", edtmatn.getText()); //  ********** parametr  ersali dar surate niaz
      //  params.put("count", intent.getStringExtra("COUNT"));
        if(flag.equals("sms")) {
                params.put("cnt11", etebari);
                params.put("cnt1", daemi);
                params.put("cnt2", irancell);
        }
        if(flag.equals("panel")){
            params.put("cnt11", etebari);
            params.put("cnt1", daemi);
            params.put("cnt2", irancell);
        }


        client.post("http://nardoun.ir/api/sendsms", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(DetailsSms.this, null, null,true, false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.setContentView(R.layout.progress_layout_small);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();

                String value = new String(response);
                App.CustomToast("ارسال شد");
              //  App.CustomToast(value);
               // Log.i("mylog3" ,"ersal"+value );
                webServiceUpdate("1");

                Intent intent = new Intent(App.context, MyPanel.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                //dialog.hide();

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast(String.valueOf(R.string.failSend));

                }else{
                    dialog.hide();
                    App.CustomToast(String.valueOf(R.string.failSend));
                //    Log.i("myerror" , errorResponse.toString());
                }
            }
        });
    }

    public  void webServiceTestSMS()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // File myFile = new File("/path/to/file.png");

        Intent intent=getIntent();
        params.put("content", edtmatn.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("number", edtMobile.getText());



        client.post("http://nardoun.ir/api/testsms", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //dialog.hide();
                App.CustomToast("پیامک با موفقیت ارسال شد");
                webCredit = webCredit - (1 * countSMS );
                webServiceUpdate("0");

                Intent intent = new Intent(App.context, DetailsSms.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("MATN" , edtmatn.getText().toString() );

                startActivity(intent);
                finish();



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                //dialog.hide();

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast(String.valueOf(R.string.failSend));

                }else{
                    App.CustomToast(String.valueOf(R.string.failSend));
                }
            }
        });
    }


    public  void webServiceUpdate(String isused)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("credit",webCredit);
        params.put("isused",isused);

        client.put(App.urlApi+"buylog/"+id, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                App.CustomToast("اطلاعات خرید ثبت شد");

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    //dialog.hide();
                    App.CustomToast("ثبت اطلاعات خرید با مشکل مواجه شد !");

                }else{
                    // dialog.hide();
                    App.CustomToast("اطلاعات خرید ثبت نشد ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public  void webServiceGetCredit()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"showbuylog/"+id , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
                webCredit = Integer.parseInt(value);
                if(flag.equals("sms")) {
                    daemi=SMS.Sdaemi;
                    etebari =SMS.Setebari;
                    irancell = SMS.Sirancell;
                    if(daemi>0){
                        credit = webCredit+etebari+irancell;
                        daemi= webCredit;
                    }
                    else if(etebari>0){
                        credit = daemi+webCredit+irancell;
                        etebari=webCredit;
                    }
                    else if(irancell>0){
                        credit = daemi+etebari+webCredit;
                        irancell=webCredit;
                    }

                }if(flag.equals("panel")){
                    daemi=PayDetails.Sdaemi;
                    etebari =PayDetails.Setebari;
                    irancell = PayDetails.Sirancell;
                    if(daemi>0){
                        credit = webCredit+etebari+irancell;
                        daemi= webCredit;
                    }
                    else if(etebari>0){
                        credit = daemi+webCredit+irancell;
                        etebari=webCredit;
                    }
                    else if(irancell>0){
                        credit = daemi+etebari+webCredit;
                        irancell=webCredit;
                    }

                }
                mojazSms();


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                  //  App.CustomToast(" خطا.لطفا مجددا امتحان کنید ");

                }else{
                  //  App.CustomToast(" خطا.لطفا مجددا امتحان کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }


}
