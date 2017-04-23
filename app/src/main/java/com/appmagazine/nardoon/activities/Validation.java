package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

import cz.msebera.android.httpclient.Header;

public class Validation extends AppCompatActivity {

    private ProgressDialog dialog;
    private String myDevice;
    EditText edtValidation;
    Button btnLogin;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);


        edtValidation = (EditText) findViewById(R.id.edt_validation);
        btnLogin = (Button) findViewById(R.id.btn_login);

        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);
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





        myDevice = App.android_id;

        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        String loginId = prefs.getString("id", null);
        if (loginId != null) {
             id = prefs.getString("id", "0");//"No name defined" is the default value.
        }



    }

    public  void isValid(View view)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("id",id);
        params.put("activation_code", edtValidation.getText());

        client.post(App.urlApi+"confirmation/validate", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Validation.this, null, null, true, false);
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

                Intent intent = new Intent(App.context , Main.class);
                startActivity(intent);
                finish();



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();

                if(statusCode==401)
                {
                    confirmation();

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }
    public  void confirmation()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("deviceid",myDevice);
        params.put("mobile", edtValidation.getText());

        client.post(App.urlApi+"confirmation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Validation.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                dialog.hide();
                App.CustomToast(value);

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

}