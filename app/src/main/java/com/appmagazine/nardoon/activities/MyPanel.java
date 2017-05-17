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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.Adapter.MyAgahiAdapter;
import com.appmagazine.nardoon.Adapter.MyPanelAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.MyPay;
import com.appmagazine.nardoon.NetUtilsCatsAgahi;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MyPanel extends AppCompatActivity {
    RecyclerView recyclerView;
    MyPanelAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<MyPay> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    String myDevice;
    private String id_confirmaation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_panel);

        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText("تاریخچه خرید");
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

        SharedPreferences prefs = App.context.getSharedPreferences("LOGIN_ID", 0);
        SharedPreferences prefs2 = App.context.getSharedPreferences("IS_LOGIN", 0);
        String status = prefs2.getString("islogin", "0");
        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

        if (status.matches("1") && !id_confirmaationSH.equals("0")) {
            id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");



        myDevice=App.android_id;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);


        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new MyPanelAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            webServiceGetAgahi();
            }
        };


            webServiceGetAgahi();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<MyPay>();
            webServiceGetAgahi();
                scrollListener.resetState();
            }
        });
            
        } else {
            Intent intent = new Intent(App.context, Login.class);
            startActivity(intent);
        }

    }
    public  void webServiceGetAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("username", ""); //  ********** parametr  ersali dar surate niaz
//        params.put("password", "");
        client.get(App.urlApi+"buylog/"+id_confirmaation , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
        @Override
        public void onStart() {
            swipeRefreshLayout.setRefreshing(true);
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            swipeRefreshLayout.setRefreshing(false);

            String value = new String(response);

            try {

                JSONArray posters = new JSONArray(value);
                for (int i = 0; i < posters.length(); i++) {
                    array.add(new MyPay(posters.getJSONObject(i)));
                }
                adapter.update(array);
                swipeRefreshLayout.setRefreshing(false);
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
                swipeRefreshLayout.setRefreshing(false);
                App.CustomToast(" خطا.لطفا مجددا امتحان کنید ");

            }else{
                swipeRefreshLayout.setRefreshing(false);
                App.CustomToast(" خطا.لطفا مجددا امتحان کنید ");
            }
        }

        @Override
        public void onRetry(int retryNo) {
            // called when request is retried
        }
    });
}

            }