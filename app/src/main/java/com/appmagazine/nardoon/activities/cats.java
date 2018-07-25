package com.appmagazine.nardoon.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class cats extends Activity {
    public static ArrayList<String> subs = new ArrayList<>();
    public static ArrayList<Integer> subsid = new ArrayList<>();
    ArrayAdapter<String> adapterSub;
    public static ProgressDialog dialog;
    ListView listView;
    Typeface sansfarsi;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        sansfarsi= Typeface.createFromAsset(App.context.getAssets(), "Sansfarsi.ttf");
        staticsetcategory();

    }

    public void staticsetcategory(){

        String value = "[{\"id\":1,\"name\":\"\\u0645\\u062a\\u0641\\u0631\\u0642\\u0647\"},{\"id\":2,\"name\":\"\\u0627\\u0645\\u0644\\u0627\\u06a9\"},{\"id\":3,\"name\":\"\\u0645\\u0631\\u0628\\u0648\\u0637 \\u0628\\u0647 \\u062e\\u0627\\u0646\\u0647\"},{\"id\":4,\"name\":\"\\u0644\\u0648\\u0627\\u0632\\u0645 \\u0627\\u0644\\u06a9\\u062a\\u0631\\u0648\\u0646\\u06cc\\u06a9\\u06cc\"},{\"id\":5,\"name\":\"\\u0648\\u0633\\u0627\\u06cc\\u0644 \\u0634\\u062e\\u0635\\u06cc\"},{\"id\":6,\"name\":\"\\u062e\\u062f\\u0645\\u0627\\u062a\"},{\"id\":7,\"name\":\"\\u0633\\u0631\\u06af\\u0631\\u0645\\u06cc \\u0648 \\u0641\\u0631\\u0627\\u063a\\u062a\"},{\"id\":8,\"name\":\"\\u0648\\u0633\\u0627\\u06cc\\u0644 \\u0646\\u0642\\u0644\\u06cc\\u0647\"},{\"id\":9,\"name\":\"\\u0627\\u0633\\u062a\\u062e\\u062f\\u0627\\u0645 \\u0648 \\u06a9\\u0627\\u0631\\u06cc\\u0627\\u0628\\u06cc\"},{\"id\":10,\"name\":\"\\u0631\\u0633\\u062a\\u0648\\u0631\\u0627\\u0646\"}]";
        try {
            JSONArray responcearray = new JSONArray(value);
            for (int i = 0; i < responcearray.length(); i++) {

                JSONObject obj = new JSONArray(value).getJSONObject(i);
                String subname = obj.getString("name");
                int subid = obj.getInt("id");
                subs.add(subname);
                subsid.add(subid);

                final LinearLayout lm = (LinearLayout ) findViewById(R.id.linearMain);
                LinearLayout .LayoutParams params = new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
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
                layout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent intent = new Intent(App.context, subs.class);
                        // intent.putExtra("POSITION", id);
                        intent.putExtra("CATID", subsid.get(index)+"");
                        intent.putExtra("CATNAME", subs.get(index)+"");
                        startActivity(intent);
                        finish();
                    }
                });

                if(layout.getParent()!=null)
                    ((ViewGroup)layout.getParent()).removeView(layout);

                ll.addView(layout);
                lm.addView(ll);
            }

        } catch (JSONException e1) {
            dialog.hide();
            e1.printStackTrace();
        }


    }


    public  void webServiceGetCategory()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"categories", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                dialog = ProgressDialog.show(cats.this, null, null,true, false);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

                dialog.setContentView(R.layout.progress_layout_small);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
                String value = new String(response);
                try {
                    JSONArray responcearray = new JSONArray(value);
                    for (int i = 0; i < responcearray.length(); i++) {

                        JSONObject obj = new JSONArray(value).getJSONObject(i);
                        String subname = obj.getString("name");
                        int subid = obj.getInt("id");
                        subs.add(subname);
                        subsid.add(subid);

                        final LinearLayout lm = (LinearLayout ) findViewById(R.id.linearMain);
                        LinearLayout .LayoutParams params = new LinearLayout.LayoutParams(
                                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                        params.setMargins(5,5,5,0);
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
                        layout.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(App.context, subs.class);
                                // intent.putExtra("POSITION", id);
                                intent.putExtra("CATID", subsid.get(index)+"");
                                intent.putExtra("CATNAME", subs.get(index)+"");
                                startActivity(intent);
                                finish();
                            }
                        });

                        if(layout.getParent()!=null)
                            ((ViewGroup)layout.getParent()).removeView(layout);

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
