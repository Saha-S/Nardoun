package com.appmagazine.nardoon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;

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
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CatAgahis extends AppCompatActivity {
    RecyclerView recyclerView;
    PosterAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Poster> array;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayout llFilter;
    public static String catID;
    public static ArrayList<String> subs = new ArrayList<>();
    public static ArrayList<Integer> subsid = new ArrayList<>();
    ArrayAdapter<String> adapterSub;
    public static ProgressDialog dialog;
    public static int subsNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_agahis);

        subs.clear();
        dialog = ProgressDialog.show(CatAgahis.this, null, null, true, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_layout_small);

        ImageButton ibmenu = (ImageButton) findViewById(R.id.ib_menu);

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


        Intent intent = getIntent();
        catID = intent.getStringExtra("id");

        TextView txtSub = (TextView) findViewById(R.id.txt_sub);
        txtSub.setText("زیردسته های " + intent.getStringExtra("name"));
        TextView txtAgahi = (TextView) findViewById(R.id.txt_agahi);
        txtAgahi.setText("آگهی های " + intent.getStringExtra("name"));


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false);
        array = new ArrayList<>();
        adapter = new PosterAdapter(App.context, array);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


        llFilter = (LinearLayout) findViewById(R.id.ll_Filter);

        webServiceGetCategory();


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
                loadData(page);
            }
        };


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
                Intent intent = new Intent(App.context, Details.class);
                intent.putExtra("id", array.get(position).id + "");
                intent.putExtra("title", array.get(position).title);
                intent.putExtra("image", array.get(position).image);
                intent.putExtra("location", array.get(position).location);
                intent.putExtra("price", array.get(position).price);
                startActivity(intent);
            }
        }));


    }


    public void loadData(int page) {
        NetUtilsCatsAgahi.get("http://nardoun.ir/api/agahisbycat/" + catID + "?data=phone&limit=10&page=" + (page + 1), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                JSONArray posters = response;
                try {
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
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialog.hide();
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

                dialog.hide();
                loadData(0);
                String value = new String(response);
                try {
                    JSONArray responcearray = new JSONArray(value);
                    subsNumber = responcearray.length();
                    for (int i = 0; i < responcearray.length(); i++) {

                        JSONObject obj = new JSONArray(value).getJSONObject(i);
                        String subname = obj.getString("name");
                        int subid = obj.getInt("id");
                        subs.add(subname);
                        subsid.add(subid);

                        final LinearLayout lm = (LinearLayout) findViewById(R.id.linearMain);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                        params.setMargins(5, 5, 5, 0);
                        params.height = 70;
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
                                startActivity(intent);
                            }
                        });

                        if (layout.getParent() != null)
                            ((ViewGroup) layout.getParent()).removeView(layout);

                        ll.addView(layout);
                        lm.addView(ll);
                    }

                } catch (JSONException e1) {
                    dialog.hide();
                    e1.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();
                if (statusCode == 404) {
                    dialog.hide();
                    App.CustomToast("آگهی وجود ندارد !");


                } else {
                    dialog.hide();
                    App.CustomToast("fail " + statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }


            @Override
            public void onRetry(int retryNo) {
                subs.clear();
                dialog.hide();
            }
        });


    }


}
