package com.appmagazine.nardoon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appmagazine.nardoon.Adapter.NiniAdapter;
import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.MyPay;
import com.appmagazine.nardoon.NetUtils;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.appmagazine.nardoon.activities.Details;
import com.appmagazine.nardoon.activities.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NiniAx extends Fragment implements TextWatcher {
    RecyclerView recyclerView;
    NiniAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Nini> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nini_ax, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        EditText editText=(EditText)view.findViewById(R.id.editText_main_search);
        editText.addTextChangedListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new NiniAdapter(getContext(), array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);



        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    webServiceGetNini();
                    swipeRefreshLayout.setRefreshing(true);
                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }

                }
        };

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            webServiceGetNini();
            swipeRefreshLayout.setRefreshing(true);
        }else {
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            swipeRefreshLayout.setRefreshing(false);
        }


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<Nini>();
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    webServiceGetNini();
                    swipeRefreshLayout.setRefreshing(true);

                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }
                    scrollListener.resetState();
            }
        });


        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public  void webServiceGetNini()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
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
                        if (( Arrays.asList(posters.getJSONObject(i).getString("validity")).contains("10")) ||( Arrays.asList(posters.getJSONObject(i).getString("validity")).contains("1"))) {

                            array.add(new Nini(posters.getJSONObject(i)));
                        }
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
                    App.CustomToast("عکسی موجود نیست");

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