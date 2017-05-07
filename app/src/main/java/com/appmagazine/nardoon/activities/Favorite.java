package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.appmagazine.nardoon.Adapter.FavoriteAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Favorite extends AppCompatActivity {
    RecyclerView recyclerView;
    FavoriteAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Poster> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    public static ProgressDialog dialog;
    String myDevice;
    public static String[] favoritearray;
    public static int numbers=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        FileOperations file = new FileOperations();
        String favoritestr =file.read("favorite");
        favoritearray = favoritestr.split("-");
        numbers = favoritearray.length;


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

        dialog = ProgressDialog.show(Favorite.this, null, null,true, false);
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog.setContentView(R.layout.progress_layout_small);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new FavoriteAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                webServiceGetFavorite();
            }
        };


        webServiceGetFavorite();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<Poster>();
                webServiceGetFavorite();
                scrollListener.resetState();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(App.context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //Toast.makeText(getContext(), "آیتم شماره " + array.get(position).id + " را کلیک کردید!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(App.context , Details.class);
                intent.putExtra("id", array.get(position).id+"");
                intent.putExtra("title", array.get(position).title);
                intent.putExtra("image", array.get(position).image);
                intent.putExtra("location", array.get(position).location);
                intent.putExtra("price", array.get(position).price);
                intent.putExtra("time", array.get(position).created_at);
                intent.putExtra("validity", array.get(position).validity);
                startActivity(intent);
            }
        }));
    }

    public  void webServiceGetFavorite()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"agahis" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
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
                            array.add(new Poster(posters.getJSONObject(i)));
                        Log.i("ssssss2" , array.toString());

                    }
                    adapter.update(array);
                    Log.i("ssssss1" , array.toString());

                    //  swipeRefreshLayout.setRefreshing(false);
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
                    dialog.hide();
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    dialog.hide();
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