package com.appmagazine.nardoon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.appmagazine.nardoon.activities.Details;
import com.appmagazine.nardoon.activities.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Main extends Fragment {
    RecyclerView recyclerView;
    PosterAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Poster> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    public static ProgressDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);


        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new PosterAdapter(getContext(), array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        llFilter=(LinearLayout) view.findViewById(R.id.ll_Filter);


        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(App.context , Filter.class);
                startActivity(intent);

            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    webServiceGetAgahi();
                }else
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            }
        };

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            webServiceGetAgahi();

        }else
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<Poster>();
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    webServiceGetAgahi();
                }else
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");

                scrollListener.resetState();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(App.context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //Toast.makeText(getContext(), "آیتم شماره " + array.get(position).id + " را کلیک کردید!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext() , Details.class);
                intent.putExtra("id", array.get(position).id+"");
                intent.putExtra("title", array.get(position).title);
                intent.putExtra("image", array.get(position).image);
                intent.putExtra("location", array.get(position).location);
                intent.putExtra("price", array.get(position).price);
                intent.putExtra("time", array.get(position).created_at);
                intent.putExtra("statusbox", "0");

                startActivity(intent);
            }
        }));
        return view;
    }




    public  void webServiceGetAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"agahis", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(getActivity(), null, null,true, false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.setContentView(R.layout.progress_layout_small);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();

                String value = new String(response);

                try {
                    JSONArray posters = new JSONArray(value);

                    for (int i = 0; i < posters.length(); i++) {
                        array.add(new Poster(posters.getJSONObject(i)));
                    }
                    adapter.update(array);
                    swipeRefreshLayout.setRefreshing(false);
                    dialog.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.hide();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی موجود نیست");

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