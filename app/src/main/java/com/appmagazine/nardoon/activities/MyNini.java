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
    public static ProgressDialog dialog;
    private String id_confirmaation;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_nini);

        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
        TextView tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        tvtitle.setTypeface(tfmorvarid);


        ibmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        dialog = ProgressDialog.show(MyNini.this, null, null,true, false);
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog.setContentView(R.layout.progress_layout_small);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new MyNiniAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        status = prefs2.getString("islogin", "0");
        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

        if (status.matches("1")) {
            id_confirmaation=id_confirmaationSH.replace("[{\"id\":", "").replace("}]" , "");
            Log.i("aaaaaaa" , App.urlApi+"nini/"+id_confirmaation);
            webServiceGetNini();
        }else {
            Intent intent = new Intent(App.context, Login.class);
            startActivity(intent);
        }


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (status.matches("1")) {
                    webServiceGetNini();
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
                if (status.matches("1")) {
                    webServiceGetNini();
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
                // called before request is started

                // loginpb1.setVisibility(View.VISIBLE);      *************** inja  progressbar faal mishe
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();

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