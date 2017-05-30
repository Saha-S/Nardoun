package com.appmagazine.nardoon.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

import com.appmagazine.nardoon.Adapter.CatAdapter;
import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.Cat;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.NetUtilsCatsAgahi;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CatAgahis extends AppCompatActivity implements TextWatcher {
    RecyclerView recyclerView;
    CatAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Cat> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    public static String catID;
    public static ArrayList<String> subs = new ArrayList<>();
    public static ArrayList<Integer> subsid = new ArrayList<>();
    ArrayAdapter<String> adapterSub;
    public static int subsNumber;
    TextView txtSub;
    Boolean isSubcatAvailable = false;
    Typeface sansfarsi;
    LinearLayout lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_agahis);
        Intent intent = getIntent();
        sansfarsi= Typeface.createFromAsset(App.context.getAssets(), "Sansfarsi.ttf");
        subs.clear();
        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText(intent.getStringExtra("name").toString());
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

        EditText editText=(EditText)findViewById(R.id.editText_main_search);
        editText.addTextChangedListener(this);

        catID = intent.getStringExtra("id");
        lm = (LinearLayout) findViewById(R.id.linearMain);

        txtSub = (TextView) findViewById(R.id.txt_sub);
        TextView txtAgahi = (TextView) findViewById(R.id.txt_agahi);
        txtAgahi.setText("آگهی های " + intent.getStringExtra("name"));
        txtSub.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new CatAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


        llFilter = (LinearLayout) findViewById(R.id.ll_Filter);

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            swipeRefreshLayout.setRefreshing(true);
            if(Integer.parseInt(catID) == 1){

                webServiceGetCatAgahi();
            }else{

                webServiceGetCategory();

            }
        }else {
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            swipeRefreshLayout.setRefreshing(false);

        }



        llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(App.context, Filter.class);
                startActivity(intent);

            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    webServiceGetCatAgahi();
                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }


            }
        };


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array = new ArrayList<Cat>();
                webServiceGetCatAgahi();
                scrollListener.resetState();
            }
        });

     /*   recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(App.context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getContext(), "آیتم شماره " + array.get(position).id + " را کلیک کردید!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(App.context, Details.class);
                intent.putExtra("id", array.get(position).id + "");
                intent.putExtra("title", array.get(position).title);
                intent.putExtra("image", array.get(position).image);
                intent.putExtra("location", array.get(position).location);
                intent.putExtra("price", array.get(position).price);
                startActivity(intent);
            }
        }));
*/

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.filter(charSequence.toString());
        if(charSequence.toString().isEmpty()){
            lm.setVisibility(View.VISIBLE);
            txtSub.setVisibility(View.VISIBLE);
        }else {
            lm.setVisibility(View.GONE);
            txtSub.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public  void webServiceGetCatAgahi()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"agahisbycat/"+catID, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {


                String value = new String(response);

                try {
                    JSONArray posters = new JSONArray(value);

                    for (int i = 0; i < posters.length(); i++) {
                        array.add(new Cat(posters.getJSONObject(i)));
                    }
                    adapter.update(array);
                    swipeRefreshLayout.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    swipeRefreshLayout.setRefreshing(false);
                    App.CustomToast("آگهی موجود نیست");

                }else{
                    swipeRefreshLayout.setRefreshing(false);
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











    public void webServiceGetCategory() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi + "categories/" + catID, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {


                webServiceGetCatAgahi();
                String value = new String(response);
                try {
                    JSONArray responcearray = new JSONArray(value);
                    subsNumber = responcearray.length();
                    Intent intent = getIntent();
                    txtSub.setVisibility(View.VISIBLE);
                    txtSub.setText("زیردسته های " + intent.getStringExtra("name"));
                    for (int i = 0; i < responcearray.length(); i++) {

                        JSONObject obj = new JSONArray(value).getJSONObject(i);

                        isSubcatAvailable = true;

                        String subname = obj.getString("name");
                        int subid = obj.getInt("id");
                        subs.add(subname);
                        subsid.add(subid);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                            params.setMargins(5,5,5,5);
                            params.height = ActionBar.LayoutParams.WRAP_CONTENT;
                        params.gravity = Gravity.CENTER_VERTICAL;


                        LinearLayout ll = new LinearLayout(App.context);
                        ll.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout layout = new LinearLayout(App.context);
                        layout.setBackgroundColor(Color.parseColor("#ffffff"));
                        ll.addView(layout);

                        TextView tv = new TextView(App.context);
                        // ImageView iv = new ImageView(App.context);
                        tv.setLayoutParams(params);
                        tv.setText(subname);
                            tv.setTextSize(17);
                            tv.setPadding(5,5,5,5);
                            tv.setTypeface(sansfarsi);
                        tv.setTextColor(Color.parseColor("#4f4f4f"));
                        // iv.setBackgroundResource(R.mipmap.left);


                        layout.addView(tv);
                        //   layout.addView(iv);


                        layout.setLayoutParams(params);

                        final int index = i;
                        layout.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {

                                Log.i("TAG", "index :" + index);
                                Intent intent = new Intent(App.context, SubCatAgahis.class);
                                intent.putExtra("POSITION", subsid.get(index) + "");
                                intent.putExtra("subname", subs.get(index) + "");
                                startActivity(intent);
                            }
                        });

                        if (layout.getParent() != null)
                            ((ViewGroup) layout.getParent()).removeView(layout);

                        ll.addView(layout);
                        lm.addView(ll);

                    }

                } catch (JSONException e1) {

                    txtSub.setVisibility(View.GONE);
                    e1.printStackTrace();

                }


                if (!isSubcatAvailable){

                    txtSub.setVisibility(View.GONE);

                    webServiceGetCatAgahi();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                if (statusCode == 404) {
                    txtSub.setVisibility(View.GONE);
                    webServiceGetCatAgahi();


                } else {
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }


            @Override
            public void onRetry(int retryNo) {
                subs.clear();
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
