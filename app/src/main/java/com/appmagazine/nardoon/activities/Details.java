package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Details extends AppCompatActivity {
    boolean FLAG_COLLAPSED = true;
    int positionID;
    TextView tvtitle,tvcontent,tvprice,tvlocation , tvtime;
    String url, catname , mobile , email ;
    ImageView ivtitle;
    CollapsingToolbarLayout collapsingToolbar;
    public static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dialog = ProgressDialog.show(Details.this, null, null,true, false);
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog.setContentView(R.layout.progress_layout_small);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvtitle = (TextView) findViewById(R.id.txtTitle);
        tvlocation = (TextView) findViewById(R.id.txtLocation);
        tvprice = (TextView) findViewById(R.id.txtPrice);
        tvcontent = (TextView) findViewById(R.id.txtContent);
        tvtime = (TextView) findViewById(R.id.txtTime);
        ivtitle=(ImageView) findViewById(R.id.iv_title);
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab_call);

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(App.context , Call.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("email", email);
                startActivity(intent);

            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("??");
                finish();
            }
        });


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        Intent intent=getIntent();


        tvtitle               .setText(intent.getStringExtra("title"));
        tvprice               .setText(intent.getStringExtra("price")+" تومان ");
        tvlocation               .setText(intent.getStringExtra("location"));
        tvtime               .setText(intent.getStringExtra("time"));

       // collapsingToolbar   .setTitle(intent.getStringExtra("catname"));
        url                 =App.urlApi+"agahis/"+intent.getStringExtra("id");
        Glide.with(this)
                .load("http://nardoun.ir/upload/"+intent.getStringExtra("image"))
                .placeholder(R.mipmap.nopic)
                .into(ivtitle);
        webServiceGetAgahi();


    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mShare:

                Intent  i = new Intent(

                        android.content.Intent.ACTION_SEND);

                i.setType("text/plain");

                i.putExtra(

                        android.content.Intent.EXTRA_TEXT, "The string you want to share, which can include URLs");

                startActivity(Intent.createChooser(

                        i,

                        "Title of your share dialog"));

                break;

        }

        return super.onOptionsItemSelected(item);

    }

    public  void webServiceGetAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("username", ""); //  ********** parametr  ersali dar surate niaz
//        params.put("password", "");

        client.get(url, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                // called before request is started

                // loginpb1.setVisibility(View.VISIBLE);      *************** inja  progressbar faal mishe
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK" ************** inja vaqti successful shod code 200 daryaft kard mituni json parse koni

                // loginpb1.setVisibility(View.INVISIBLE);
                String value = new String(response);
                try {
                    JSONObject obj =new JSONArray(value).getJSONObject(0);  //********* chon ye json array ba 1 json objecte injur migirimesh

                    String content= obj.getString("content");
                    String type= obj.getString("type");
                    catname= obj.getString("category_name");
                    email= obj.getString("email");
                    mobile= obj.getString("mobile");
                    collapsingToolbar.setTitle(catname);

                    tvcontent.setText(content);
                    dialog.hide();

                } catch (JSONException e1) {

                    e1.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
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




}
