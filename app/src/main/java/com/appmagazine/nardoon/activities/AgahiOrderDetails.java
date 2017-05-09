package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.Adapter.AgahiOrdersAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AgahiOrderDetails extends Activity {

    String type ,id;
    private JSONArray menuJsonArray;
    private ProgressDialog dialog;
    private String idAgahi;
    TextView txtDone;
    Button send;
    String isused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agahi_order_details);

        Intent intent=getIntent();

        txtDone = (TextView) findViewById(R.id.txt_done);
        TextView txtPrice = (TextView) findViewById(R.id.txt_total_price);
        TextView txtTime = (TextView) findViewById(R.id.time);
        final TextView mobile = (TextView) findViewById(R.id.mobile);
        TextView AgahiId = (TextView) findViewById(R.id.idAgahi);
        TextView agahiMobile = (TextView) findViewById(R.id.agahi_mobile);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LinearLayout llCall = (LinearLayout) findViewById(R.id.ll_call);

         send = (Button) findViewById(R.id.btnSend);

            isused = AgahiOrdersAdapter.isdone;
            idAgahi = AgahiOrdersAdapter.id;
            mobile.setText(AgahiOrdersAdapter.mobile);
            agahiMobile.setText(AgahiOrdersAdapter.agahiMobile);
            AgahiId.setText(idAgahi);

        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobile.getText()));
                startActivity(intent);


            }
        });

        //   id =  intent.getStringExtra("ID").toString();
            Log.i("FFFFFFFFFFF" ,isused );
            Log.i("FFFFFFFFFFF" ,idAgahi );

            if(isused.equals("0")){
                send.setVisibility(View.VISIBLE);
                txtDone.setText("تحویل داده نشده");
            }
            if(isused.equals("1")){
                send.setVisibility(View.GONE);
                txtDone.setText("تحویل داده شده");

            }

            txtPrice.setText(AgahiOrdersAdapter.price+" تومان");
            txtTime.setText(AgahiOrdersAdapter.time.toString());

        try {
            //   Intent intent = getIntent();
            String order = AgahiOrdersAdapter.order;
            menuJsonArray = new JSONArray(order);
            //     Log.i("aaaaaaa122322", order);
            Log.i("aaaaaaa12234433", menuJsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("aaaaaaa122333", menuJsonArray.toString());
        try {
            for (int i = 0; i < menuJsonArray.length(); i++) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row_orders, null);
                final TextView txtName = (TextView) addView.findViewById(R.id.txtName);
                final TextView txtNumber = (TextView) addView.findViewById(R.id.txtNumber);
                final TextView txtfoodPrice = (TextView) addView.findViewById(R.id.txtPrice);

                final JSONObject oo = menuJsonArray.getJSONObject(i);
                Log.i("aaaaaaa12233113", oo.getString("name").toString());

                txtName.setText(oo.getString("name").toString());
                txtNumber.setText(oo.getString("number").toString());
                txtfoodPrice.setText(oo.getString("price").toString() + " تومان ");


                JSONObject object = new JSONObject();

                container.addView(addView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceEditAgahi();
            }
        });

    }

    public  void webServiceEditAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("isdone","1");


        client.put(App.urlApi+"factor/"+idAgahi, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(AgahiOrderDetails.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
                send.setVisibility(View.GONE);
                txtDone.setText("تحویل داده شده");
                isused="1";
                //  Intent intent = new Intent(App.context , Details.class);
                //  intent.putExtra("id", Details.idAgahi+"");
                // startActivity(intent);
                // App.CustomToast("آگهی ویرایش شد !");
               // webServiceBuylog();
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



}