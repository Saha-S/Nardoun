package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.appmagazine.nardoon.Adapter.MyNiniAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.NetUtilsCatsAgahi;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.appmagazine.nardoon.R.id.status;

public class MyNini extends AppCompatActivity {
    RecyclerView recyclerView;
    MyNiniAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Nini> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    private String id_confirmaation;
    private String status;
    private String id_confirmaationSH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_nini);

        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText("نی نی عکس های من");
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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);


        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new MyNiniAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        status = prefs2.getString("islogin", "0");
        id_confirmaationSH = prefs.getString("id_confirmaation", "0");

        if (status.matches("1") && !id_confirmaationSH.equals("0")) {
            id_confirmaation=id_confirmaationSH.replace("[{\"id\":", "").replace("}]" , "");
            Log.i("aaaaaaa" , App.urlApi+"nini/"+id_confirmaation);
            ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected() || isMobileDataEnabled()) {
                webServiceGetNini();
                swipeRefreshLayout.setRefreshing(true);

            }else {
                App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                swipeRefreshLayout.setRefreshing(false);
            }
            }else {
            Intent intent = new Intent(App.context, Login.class);
            startActivity(intent);
            finish();
        }


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (status.matches("1") && !id_confirmaationSH.equals("0")) {
                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {
                        webServiceGetNini();
                        swipeRefreshLayout.setRefreshing(true);

                    }else {
                        App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    }else {
                    Intent intent = new Intent(App.context, Login.class);
                    startActivity(intent);
                }
            }
        };


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<Nini>();
                if (status.matches("1") && !id_confirmaationSH.equals("0")) {
                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {
                        webServiceGetNini();
                        swipeRefreshLayout.setRefreshing(true);

                    }else {
                        App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    }else {
                    Intent intent = new Intent(App.context, Login.class);
                    startActivity(intent);
                }
                scrollListener.resetState();
            }
        });

    }

    public  void webServiceGetNini()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/"+id_confirmaation , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                swipeRefreshLayout.setRefreshing(false);

                String value = new String(response);

                try {

                    JSONArray posters = new JSONArray(value);
                    for (int i = 0; i < posters.length(); i++) {
                        array.add(new Nini(posters.getJSONObject(i)));
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
                    App.CustomToast(" نی نی عکسی موجود نیست");

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
    public Boolean isMobileDataEnabled(){
        Object connectivityService = App.context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}