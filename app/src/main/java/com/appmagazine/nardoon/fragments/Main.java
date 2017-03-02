package com.appmagazine.nardoon.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.NetUtils;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Main extends Fragment {
    RecyclerView recyclerView;
    PosterAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Poster> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener; // تعریف اسکرول لیسنر


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe); // اشاره گر سوایپ ریفرش لیوت
        recyclerView = (RecyclerView) view.findViewById(R.id.list); // اشاره گر ریسایکلر ویو
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); // تعریف لینیر لیوت منیجر به صورت عمودی
        array = new ArrayList<>();// ایجاد لیست داده ها
        adapter = new PosterAdapter(getContext(), array); //ساخت اداپتر از لیست داده ها

        recyclerView.setLayoutManager(linearLayoutManager); //  ست کردن لیوت منیجر به ریسایکلر ویو
        recyclerView.setAdapter(adapter); // ست کردن آداپتر به ریسایکلر ویو


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) { // مقدار دهی اسکرول لیسنتر
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadData(page); // کارهایی که باید بعد از اسکرول اتفاق بیافتد. در اینجا لود دادهای دیگر می باشد.
            }
        };
        recyclerView.addOnScrollListener(scrollListener); // ست کردن اسکرول لیسنر به ریسایکلر ویو

        loadData(0);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
// دستورات در هنگامی که ریفرش می شود لیست.
                array = new ArrayList<Poster>(); // لیست خالی می شود.
                loadData(0); // در این مثال دستور لود داده های اول برنامه را اجرا می کنیم.
                scrollListener.resetState(); // اسکرول لیسنر را ریست می کنیم
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
// در این قسمت کارهایی که وقتی کلیک می شود را تعریف می کنیم
                Toast.makeText(getContext(), "آیتم شماره " + array.get(position).id + " را کلیک کردید!", Toast.LENGTH_LONG).show();
            }
        }));


        return view;

    }

    public void loadData(int page) {
        NetUtils.get("?data=phone&limit=10&page=" + (page+1), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArray posters = response;
                try {
                    for (int i = 0; i < posters.length(); i++) { // تمامی داده ها را میگیرم و به لیست اضافه می کنیم
                        array.add(new Poster(posters.getJSONObject(i)));
                    }
                    adapter.update(array); // به اداپتر لیست با داده های جدید را میفرستیم و آپدیت میکنیم.
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), "Error on request", Toast.LENGTH_LONG).show();
            }

        });
    }


}