package com.appmagazine.nardoon.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.MyNiniAdapter;
import com.appmagazine.nardoon.Adapter.NewsListAdapter;
import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.ArticleAdapter;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.News;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewsList extends AppCompatActivity {
    RecyclerView recyclerView;
    NewsListAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<News> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            appbarTitle.setText(extras.getString("name"));
            cat = extras.getString("name");
        }
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

        recyclerView = (RecyclerView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);


        final HorizontalScrollView view = (HorizontalScrollView) findViewById(R.id.scroll);
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                view.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 10);

        Button siasi = (Button)
                findViewById(R.id.siasi);
        Button ejtemaei = (Button) findViewById(R.id.ejtemaei);
        Button eghtesadi = (Button) findViewById(R.id.eghtesadi);
        Button majazi = (Button) findViewById(R.id.majazi);
        Button pezeshki = (Button) findViewById(R.id.pezeshki);
        Button varzeshi = (Button) findViewById(R.id.varzeshi);
        Button all = (Button) findViewById(R.id.all);

        LinearLayout llTabs = (LinearLayout) findViewById(R.id.llTabs);

        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new NewsListAdapter(App.context, array);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {

                if (cat.equals("rssVarzeshi")){
                    llTabs.setVisibility(View.VISIBLE);
                    appbarTitle.setText("ورزشی");
                    swipeRefreshLayout.setRefreshing(true);
                    setRss("https://www.yjc.ir/fa/rss/8");
                }
                else if (cat.equals("rssPezeshki")){
                    appbarTitle.setText("پزشکی");
                    llTabs.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    setRss("https://www.yjc.ir/fa/rss/7");

                }

                else if (cat.equals("rssMajazi")){
                    appbarTitle.setText("فضای مجازی");
                    llTabs.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    setRss("https://www.yjc.ir/fa/rss/14");
                }
                else if (cat.equals("rssEghtesadi")){
                    appbarTitle.setText("اقتصادی");
                    llTabs.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    setRss("https://www.yjc.ir/fa/rss/6");
                }
                else if (cat.equals("rssSiasi")){
                    appbarTitle.setText("سیاسی");
                    llTabs.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    setRss("https://www.yjc.ir/fa/rss/3");
                }
                else if (cat.equals("rssEjtemaei")){
                    appbarTitle.setText("اجتماعی");
                    llTabs.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    setRss("https://www.yjc.ir/fa/rss/5");
                }


            else {
                llTabs.setVisibility(View.GONE);
                webServiceGetNini();
            }
            swipeRefreshLayout.setRefreshing(true);

        }else {
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
            swipeRefreshLayout.setRefreshing(false);
        }


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected() || isMobileDataEnabled()) {

                     if (cat.equals("rssVarzeshi")){
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/8");
                    }
                    else if (cat.equals("rssPezeshki")){
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/7");
                    }

                    else if (cat.equals("rssMajazi")){
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/14");
                    }
                    else if (cat.equals("rssEghtesadi")){
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/6");
                    }
                    else if (cat.equals("rssSiasi")){
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/3");
                    }
                    else if (cat.equals("rssEjtemaei")){
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/5");
                    }

                    else {
                         swipeRefreshLayout.setRefreshing(true);
                         webServiceGetNini();
                    }

                }else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        };


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array = new ArrayList<News>();

                if (mWifi.isConnected() || isMobileDataEnabled()) {
                    if (cat.equals("rssVarzeshi")) {
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/8");
                    } else if (cat.equals("rssPezeshki")) {
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/7");
                    } else if (cat.equals("rssMajazi")) {
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/14");
                    } else if (cat.equals("rssEghtesadi")) {
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/6");
                    } else if (cat.equals("rssSiasi")) {
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/3");
                    } else if (cat.equals("rssEjtemaei")) {
                        swipeRefreshLayout.setRefreshing(true);
                        setRss("https://www.yjc.ir/fa/rss/5");
                    } else {
                        swipeRefreshLayout.setRefreshing(true);
                        webServiceGetNini();
                    }

                }
                else {
                    App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                    swipeRefreshLayout.setRefreshing(false);
                }
                scrollListener.resetState();
            }
        });

        siasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                setRss("https://www.yjc.ir/fa/rss/3");
            }
        });
        ejtemaei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                setRss("https://www.yjc.ir/fa/rss/5");
            }
        });
        eghtesadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                setRss("https://www.yjc.ir/fa/rss/6");
            }
        });
        majazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                setRss("https://www.yjc.ir/fa/rss/14");
            }
        });
        pezeshki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                setRss("https://www.yjc.ir/fa/rss/7");
            }
        });
        varzeshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                setRss("https://www.yjc.ir/fa/rss/8");
            }
        });


    }

    public  void webServiceGetNini()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"newsbycat/"+cat+"/"+App.android_id , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
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
                        array.add(new News(posters.getJSONObject(i)));
                    }
                    adapter.update(array);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e1) {

                    e1.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    swipeRefreshLayout.setRefreshing(false);
                    App.CustomToast(" خبری موجود نیست");

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

    public void setRss(String url){
        Parser parser = new Parser();
        parser.execute(url);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {

                ArticleAdapter adapter = new ArticleAdapter(list, R.layout.item_news, NewsList.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onError() {

            }
        });

    }

}