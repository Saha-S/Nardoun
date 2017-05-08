package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyOrders extends AppCompatActivity {

    private LinearLayout container;
    private String menuOrder , allPrice;
    private JSONArray menuJsonArray;
    private JSONArray orderJsonArray = new JSONArray();
    private JSONObject jsnobject;
    private int num2;
    TextView txtTime;
    int index;
    private ProgressDialog dialog;
    private String id_confirmaation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        txtTime = (TextView) findViewById(R.id.txt_time);
        container = (LinearLayout) findViewById(R.id.container);
        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText("سفارش من");
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


        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        String status = prefs2.getString("islogin", "0");
        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

        if (status.matches("1")) {
            id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");

        }else {
            Intent intent = new Intent(App.context, Login.class);
            startActivity(intent);
        }


        try {
            Intent intent = getIntent();
            String order = intent.getStringExtra("order");
            menuJsonArray = new JSONArray(order);
            Log.i("aaaaaaa122322",order );
            Log.i("aaaaaaa12234433",menuJsonArray.toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("aaaaaaa122333",menuJsonArray.toString() );
        try {
        for (int i = 0; i < menuJsonArray.length(); i++) {

            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.row_orders, null);
            final TextView txtName = (TextView)addView.findViewById(R.id.txtName);
            final TextView txtPrice = (TextView)addView.findViewById(R.id.txtPrice);
            final TextView txtNumber = (TextView)addView.findViewById(R.id.txtNumber);

            final JSONObject oo = menuJsonArray.getJSONObject(i);
            Log.i("aaaaaaa12233113",oo.getString("name").toString() );

            txtName.setText(oo.getString("name").toString());
            txtPrice.setText(oo.getString("price").toString()+" تومان ");
            txtNumber.setText(oo.getString("number").toString()+" عدد ");



            container.addView(addView);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }


            }





    public  void webServiceNewOrder()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();



        client.get(App.urlApi+"factorsbyconfirmation/"+id_confirmaation, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(MyOrders.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();
                //  String value = new String(response);
                //   Intent intent = new Intent(App.context, NiniAx.class);
                //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //   startActivity(intent);
                App.CustomToast("سفارش شما ثبت شد");

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

}
