package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.AgahiOrdersAdapter;
import com.appmagazine.nardoon.Adapter.MyAgahiAdapter;
import com.appmagazine.nardoon.AgahiOrder;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AgahiOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    AgahiOrdersAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<AgahiOrder> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    String myDevice;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agahi_order);


        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText("سفارشات دریافت شده");
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

        myDevice=App.android_id;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);


        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new AgahiOrdersAdapter(App.context, array);

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

                array = new ArrayList<AgahiOrder>();
            webServiceGetAgahi();
                scrollListener.resetState();
            }
        });
    }
    public  void webServiceGetAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("username", ""); //  ********** parametr  ersali dar surate niaz
//        params.put("password", "");
        client.get(App.urlApi+"factorsbyagahi/"+ MyAgahiAdapter.idAgahi, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
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
                    array.add(new AgahiOrder(posters.getJSONObject(i)));
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
                App.CustomToast("سفارشی موجود نیست");

            }else{

                swipeRefreshLayout.setRefreshing(false);
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