package com.appmagazine.nardoon.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.Adapter.PosterAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.EndlessRecyclerViewScrollListener;
import com.appmagazine.nardoon.NetUtilsCatsAgahi;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.RecyclerItemClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SubCat extends AppCompatActivity {
    RecyclerView recyclerView;
    PosterAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Poster> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    public static String catID ;
    public static ArrayList<String> subs = new ArrayList<>();
    public static ArrayList<Integer> subsid = new ArrayList<>();
    ArrayAdapter<String> adapterSub;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);

        subs.clear();

        Intent intent=getIntent();
        catID = intent.getStringExtra("id");

        TextView txtSub = (TextView) findViewById(R.id.txt_sub);
        txtSub.setText("زیردسته های "+intent.getStringExtra("name"));
        TextView txtAgahi = (TextView) findViewById(R.id.txt_agahi);
        txtAgahi.setText("آگهی های "+intent.getStringExtra("name"));



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new PosterAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


        llFilter=(LinearLayout) findViewById(R.id.ll_Filter);

        webServiceGetCategory();
        ListView listView = (ListView) findViewById(R.id.listv);
        adapterSub = new ArrayAdapter(this, R.layout.item_subcat, R.id.txt, subs);
        listView.setAdapter(adapterSub);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(App.context, SubCatAgahis.class);
              //  intent.putExtra("POSITION", id);
                intent.putExtra("POSITION", subsid.get(position)+"");
                startActivity(intent);
                 }
        });



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
                loadData(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array = new ArrayList<Poster>();
                loadData(0);
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
                startActivity(intent);
            }
      }));



    }




    public void loadData(int page) {
        NetUtilsCatsAgahi.get("http://nardoun.ir/api/agahisbycat/"+catID+"?data=phone&limit=10&page=" + (page+1) , null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArray posters = response;
                try {
                    for (int i = 0; i < posters.length(); i++) {
                        array.add(new Poster(posters.getJSONObject(i)));
                    }
                    adapter.update(array);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(App.context, "http://nardoun.ir/api/categories/"+catID+"/", Toast.LENGTH_LONG).show();
            }

        });
    }

    public  void webServiceGetCategory()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"categories/"+catID, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                loadData(0);
                String value = new String(response);
                try {
                    for (int i = 0; i < value.length(); i++) {

                        JSONObject obj = new JSONArray(value).getJSONObject(i);
                        String subname = obj.getString("name");
                        int subid = obj.getInt("id");
                        subs.add(subname);
                        subsid.add(subid);

                    }
                    adapterSub.notifyDataSetChanged();

                } catch (JSONException e1) {

                    e1.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if(statusCode==404)
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }


            @Override
            public void onRetry(int retryNo) {
                subs.clear();
            }
        });


    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }



}