package com.appmagazine.nardoon.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

public class subs extends Activity {
    public static ArrayList<String> subs = new ArrayList<>();
    public static ArrayList<Integer> subsid = new ArrayList<>();
    ArrayAdapter<String> adapterSub;
    public static ProgressDialog dialog;
    ListView listView;
    String CatId ,CatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

    //    listView = (ListView) findViewById(R.id.listv2);
        Intent intent=getIntent();
        CatId = intent.getStringExtra("CATID");
        CatName = intent.getStringExtra("CATNAME");


        webServiceGetCategory();
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(App.context, NewAgahi.class);
               // intent.putExtra("POSITION", id);
                intent.putExtra("CATID", CatId+"");
                intent.putExtra("SUBID", subsid.get(position)+"");
                intent.putExtra("NAME", subs.get(position)+"");
                startActivity(intent);
            }
        });


*/


    }
    public  void webServiceGetCategory()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"categories/"+CatId, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                dialog = ProgressDialog.show(subs.this, null, null,true, false);
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

                                Intent intent = new Intent(App.context, NewAgahi.class);
                                // intent.putExtra("POSITION", id);
                                intent.putExtra("CATID", CatId+"");
                                intent.putExtra("SUBID", subsid.get(index)+"");
                                intent.putExtra("NAME", subs.get(index)+"");
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

            public void onBackPressed() {
                Intent intent = new Intent(App.context, cats.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();
                if(statusCode==404)
                {
                    Intent intent = new Intent(App.context, NewAgahi.class);
                    // intent.putExtra("POSITION", id);
                    intent.putExtra("CATID", CatId+"");
                   // intent.putExtra("SUBID", "0");
                    intent.putExtra("NAME", CatName+"");
                    startActivity(intent);

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
