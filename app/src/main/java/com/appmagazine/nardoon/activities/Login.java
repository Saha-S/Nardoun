package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    private ProgressDialog dialog;
    private String myDevice;
    EditText edtMobile;
    Button btnLogin;
    Menu nav_Menu;
    TextView nav_mobile;
    Button btnExit;
    private DrawerLayout drawerLayout;

    EditText edtValidation;
    Button btnLogin2 , btnRetry;
    private String id;
    Long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        final String timee = prefs.getString("time", "0");
        time = Long.valueOf(timee);



        edtMobile = (EditText) findViewById(R.id.edt_mobile);
        btnLogin = (Button) findViewById(R.id.btn_login);
        LinearLayout llLogin = (LinearLayout) findViewById(R.id.ll_login);
        LinearLayout llValidity = (LinearLayout) findViewById(R.id.ll_validaty);

        edtValidation = (EditText) findViewById(R.id.edt_validation);
        btnLogin2 = (Button) findViewById(R.id.btn_login2);
        btnRetry = (Button) findViewById(R.id.btn_retry);


        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        Typeface tfmorvarid = Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
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

        myDevice = App.android_id;

        if(time<System.currentTimeMillis() || time==0){

            llLogin.setVisibility(View.VISIBLE);
            llValidity.setVisibility(View.GONE);
            appbarTitle.setText("ورود");


        }
        if(time>System.currentTimeMillis()){

            llLogin.setVisibility(View.GONE);
            llValidity.setVisibility(View.VISIBLE);
            appbarTitle.setText("تایید شماره");

            SharedPreferences prefs2 = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
            String loginId = prefs2.getString("id", null);
            if (loginId != null) {
                id = prefs2.getString("id", "0");//"No name defined" is the default value.
            }
            final Timer timer= new Timer();

            timer.scheduleAtFixedRate(new TimerTask(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    runOnUiThread(new Runnable() {
                        public void run() {

                            int minutes = (int) ((60000-(System.currentTimeMillis()-time) / (1000 * 60)) % 60);
                            int seconds = (int) ((60000-(System.currentTimeMillis()-time) / 1000) % 60);

                            btnRetry.setText(
                                   String.format("%02d", minutes)
                                    + ":" + String.format("%02d", seconds));
                            if (btnRetry.getText().toString().equals("00:00")){
                                btnRetry.setText("ارسال مجدد");
                                btnRetry.setBackgroundColor(Color.parseColor("#5A2456"));
                                btnRetry.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(App.context ,Login.class );
                                        startActivity(intent);
                                    }
                                });
                                timer.cancel();

                            }

                           // btnRetry.setText(10-(System.currentTimeMillis()-time)+ "");
                        }
                    });

                }

            }, 0, 1000);



        }






    }

    public void isconfirmed(View view) {

        if (edtMobile.getText().toString().trim().length() < 11 || edtMobile.getText().toString().trim().length() > 11) {
            App.CustomToast("لطفا یک شماره تلفن معتبر وارد کنید.");
        }else{

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("deviceid", myDevice);
        params.put("mobile", edtMobile.getText());

        client.post(App.urlApi + "isconfirmed", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Login.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                SharedPreferences.Editor editor = getSharedPreferences("IS_LOGIN", MODE_PRIVATE).edit();
                editor.putString("islogin", "1");
                SharedPreferences.Editor editor2 = getSharedPreferences("LOGIN_ID", MODE_PRIVATE).edit();
                editor2.putString("mobile", edtMobile.getText().toString());
                editor2.putString("id_confirmaation", value.toString().replace("[{\"id\":", "").replace("}]", ""));

                editor.commit();
                editor2.commit();
                try {
                    Main.h.sendEmptyMessage(0);
                }catch (RuntimeException r){}
                dialog.hide();
                finish();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();

                if (statusCode == 401) {
                    confirmation();

                } else {
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }

}

    public  void confirmation()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("deviceid",myDevice);
        params.put("mobile", edtMobile.getText());

        client.post(App.urlApi+"confirmation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                dialog.hide();
                long time= System.currentTimeMillis()+600000;


                SharedPreferences.Editor editor = getSharedPreferences("LOGIN_ID", MODE_PRIVATE).edit();
                editor.putString("id", value);
                editor.putString("time", String.valueOf(time));
                editor.putString("mobile", edtMobile.getText().toString());

                editor.commit();

                Intent intent = new Intent(App.context , Login.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();

                if(statusCode==401)
                {
                    App.CustomToast("خطا");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }

    /////////////////////// VALIDATION /////////////////////////////////

    public  void isValid(View view)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("id",id);
        params.put("activation_code", edtValidation.getText());

        client.post(App.urlApi+"confirmation/validate", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Login.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);



            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                dialog.hide();
                //  App.CustomToast("");

                SharedPreferences.Editor editor = getSharedPreferences("IS_LOGIN", MODE_PRIVATE).edit();
                editor.putString("islogin", "1");
                editor.commit();

                SharedPreferences.Editor editor2 = getSharedPreferences("LOGIN_ID", MODE_PRIVATE).edit();
                editor2.putString("id_confirmaation", value.toString().replace("[{\"id\":", "").replace("}]" , ""));
                editor2.commit();

                try {
                    Main.h.sendEmptyMessage(0);
                }catch (RuntimeException r){}
                //  Intent intent = new Intent(App.context , Main.class);
                //  startActivity(intent);
                finish();



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();

                if(statusCode==401)
                {
                    App.CustomToast(" کد وارد شده اشتباه است ");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }



}