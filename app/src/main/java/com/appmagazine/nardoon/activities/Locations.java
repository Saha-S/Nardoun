package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Locations extends Activity {
    public static ArrayList<String> subs = new ArrayList<>();
    public static ArrayList<Integer> subsid = new ArrayList<>();
    ArrayAdapter<String> adapterSub;
    public static ProgressDialog dialog;
    public static String id_location;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        listView = (ListView) findViewById(R.id.listv2);

        webServiceGetCategory();
        adapterSub = new ArrayAdapter(this, R.layout.item_cats, R.id.txt, subs);
        listView.setAdapter(adapterSub);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
              //  Intent intent = new Intent(App.context, subs.class);
               // intent.putExtra("POSITION", id);
              //  intent.putExtra("NAME", subs.get(position)+"");
              //  startActivity(intent);
                New.SelectLocation.setText(subs.get(position)+"");
                id_location = subsid.get(position).toString();
                finish();
            }
        });





    }
    public  void webServiceGetCategory()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"locations", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Locations.this, null, null,true, false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.setContentView(R.layout.progress_layout_small);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
                String value = new String(response);
                try {
                    subs.clear();

                    JSONArray responcearray = new JSONArray(value);
                    for (int i = 0; i < responcearray.length(); i++) {

                        JSONObject obj = responcearray.getJSONObject(i);
                        String subname = obj.getString("name");
                        int subid = obj.getInt("id");
                        subs.add(subname);
                        subsid.add(subid);

                    }
                    adapterSub = new ArrayAdapter(getApplicationContext(), R.layout.item_cats, R.id.txt, subs);
                    listView.setAdapter(adapterSub);


                } catch (JSONException e1) {

                    e1.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();
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
}
