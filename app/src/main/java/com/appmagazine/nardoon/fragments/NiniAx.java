package com.appmagazine.nardoon.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.appmagazine.nardoon.Adapter.NiniAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.AboutUs;
import com.appmagazine.nardoon.activities.Favorite;
import com.appmagazine.nardoon.activities.Login;
import com.appmagazine.nardoon.activities.MyAgahis;
import com.appmagazine.nardoon.activities.MyNini;
import com.appmagazine.nardoon.activities.Poshtibani;
import com.appmagazine.nardoon.activities.Rules;
import com.appmagazine.nardoon.activities.WinnerNini;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NiniAx extends Fragment implements TextWatcher  , NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    NiniAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Nini> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nini_ax, container, false);



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
        SharedPreferences prefs = App.context.getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = App.context.getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        SharedPreferences prefsMobile = App.context.getSharedPreferences("MOBILE", MODE_PRIVATE);
        final String mobile = prefsMobile.getString("mobile", "0");
        Log.i("mmmmmmm" , mobile);

        client.get(App.urlApi+"niniwithlike/"+mobile , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
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
    @Override
    public void onResume()
    {
        super.onResume();
        // Load data and do stuff
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent intent = new Intent(App.context , AboutUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_favarit) {
            Intent intent = new Intent(App.context , Favorite.class);
            startActivity(intent);

        } else if (id == R.id.nav_my_agahi) {

            Intent intent = new Intent(App.context , MyAgahis.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_my_nini) {

            Intent intent = new Intent(App.context , MyNini.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_login) {

            Intent intent = new Intent(App.context , Login.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_my_agahi) {

        }else if (id == R.id.nav_rules) {
            Intent intent = new Intent(App.context , Rules.class);
            startActivity(intent);

        }else if (id == R.id.nav_support) {
            Intent intent = new Intent(App.context , Poshtibani.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_winner_nini) {
            Intent intent = new Intent(App.context , WinnerNini.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}