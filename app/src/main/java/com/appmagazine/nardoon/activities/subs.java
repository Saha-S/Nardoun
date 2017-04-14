package com.appmagazine.nardoon.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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

public class subs extends Activity {
    ArrayAdapter<String> adapterSub;
    public static ProgressDialog dialog;
    ListView listView;
    String CatId ,CatName;
    Boolean ifsubcatavailable = false;
    Typeface sansfarsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorys);

        //    listView = (ListView) findViewById(R.id.listv2);
        Intent intent=getIntent();
        CatId = intent.getStringExtra("CATID");
        CatName = intent.getStringExtra("CATNAME");


        sansfarsi= Typeface.createFromAsset(App.context.getAssets(), "Sansfarsi.ttf");
        staticsetsubcat();
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

    public void staticsetsubcat(){


        String value = "[{\"id\":2,\"category_id\":\"8\",\"name\":\"\\u0645\\u0648\\u062a\\u0648\\u0631\\u0633\\u06cc\\u06a9\\u0644\\u062a \\u0648 \\u0644\\u0648\\u0627\\u0632\\u0645 \\u062c\\u0627\\u0646\\u0628\\u06cc\"},{\"id\":3,\"category_id\":\"8\",\"name\":\"\\u062a\\u0633\\u062a\\u062a\"},{\"id\":4,\"category_id\":\"4\",\"name\":\"\\u0645\\u0648\\u0628\\u0627\\u06cc\\u0644 \\u0648 \\u062a\\u0628\\u0644\\u062a\"},{\"id\":5,\"category_id\":\"4\",\"name\":\"\\u0631\\u0627\\u06cc\\u0627\\u0646\\u0647 \\u0648 \\u0644\\u0648\\u0627\\u0632\\u0645 \\u062c\\u0627\\u0646\\u0628\\u06cc\"},{\"id\":6,\"category_id\":\"2\",\"name\":\"\\u0641\\u0631\\u0648\\u0634 \\u0645\\u0633\\u06a9\\u0648\\u0646\\u06cc (\\u0622\\u067e\\u0627\\u0631\\u062a\\u0645\\u0627\\u0646\\u060c \\u062e\\u0627\\u0646\\u0647\\u060c \\u0632\\u0645\\u06cc\\u0646)\"},{\"id\":7,\"category_id\":\"2\",\"name\":\"\\u0627\\u062c\\u0627\\u0631\\u0647 \\u0645\\u0633\\u06a9\\u0648\\u0646\\u06cc (\\u0622\\u067e\\u0627\\u0631\\u062a\\u0645\\u0627\\u0646\\u060c \\u062e\\u0627\\u0646\\u0647\\u060c \\u0632\\u0645\\u06cc\\u0646)\"},{\"id\":8,\"category_id\":\"2\",\"name\":\"\\u0641\\u0631\\u0648\\u0634 \\u0627\\u062f\\u0627\\u0631\\u06cc \\u062a\\u062c\\u0627\\u0631\\u06cc (\\u0645\\u063a\\u0627\\u0632\\u0647\\u060c \\u062f\\u0641\\u062a\\u0631\\u06a9\\u0627\\u0631\\u060c \\u0635\\u0646\\u0639\\u062a\\u06cc)\"},{\"id\":9,\"category_id\":\"2\",\"name\":\"\\u0627\\u062c\\u0627\\u0631\\u0647 \\u0627\\u062f\\u0627\\u0631\\u06cc \\u062a\\u062c\\u0627\\u0631\\u06cc (\\u0645\\u063a\\u0627\\u0632\\u0647\\u060c \\u062f\\u0641\\u062a\\u0631\\u06a9\\u0627\\u0631\\u060c \\u0635\\u0646\\u0639\\u062a\\u06cc)\"},{\"id\":10,\"category_id\":\"2\",\"name\":\"\\u062e\\u062f\\u0645\\u0627\\u062a \\u0627\\u0645\\u0644\\u0627\\u06a9\"},{\"id\":11,\"category_id\":\"3\",\"name\":\"\\u0648\\u0633\\u0627\\u06cc\\u0644 \\u062a\\u0632\\u0626\\u06cc\\u0646\\u0627\\u062a \\u062e\\u0627\\u0646\\u0647\"},{\"id\":12,\"category_id\":\"3\",\"name\":\"\\u0648\\u0633\\u0627\\u06cc\\u0644 \\u0622\\u0634\\u067e\\u0632\\u062e\\u0627\\u0646\\u0647\"},{\"id\":13,\"category_id\":\"3\",\"name\":\"\\u0627\\u0628\\u0632\\u0627\\u0631\"},{\"id\":14,\"category_id\":\"3\",\"name\":\"\\u0633\\u0627\\u062e\\u062a\\u0645\\u0627\\u0646 \\u0648 \\u062d\\u06cc\\u0627\\u0637\"},{\"id\":15,\"category_id\":\"4\",\"name\":\"\\u06a9\\u0646\\u0633\\u0648\\u0644\\u060c \\u0628\\u0627\\u0632\\u06cc \\u0648\\u06cc\\u062f\\u06cc\\u0648\\u06cc\\u06cc\\u060c \\u0622\\u0646\\u0644\\u0627\\u06cc\\u0646\"},{\"id\":16,\"category_id\":\"4\",\"name\":\"\\u0635\\u0648\\u062a\\u06cc\\u060c \\u062a\\u0635\\u0648\\u06cc\\u0631\\u06cc\"},{\"id\":17,\"category_id\":\"4\",\"name\":\"\\u062a\\u0644\\u0641\\u0646 \\u0631\\u0648\\u0645\\u06cc\\u0632\\u06cc\"},{\"id\":18,\"category_id\":\"5\",\"name\":\"\\u06a9\\u06cc\\u0641\\u060c \\u06a9\\u0641\\u0634\\u060c \\u0644\\u0628\\u0627\\u0633\"},{\"id\":19,\"category_id\":\"5\",\"name\":\"\\u062a\\u0632\\u0626\\u06cc\\u0646\\u06cc\"},{\"id\":20,\"category_id\":\"5\",\"name\":\"\\u0622\\u0631\\u0627\\u06cc\\u0634\\u06cc\\u060c \\u0628\\u0647\\u062f\\u0627\\u0634\\u062a\\u06cc \\u0648 \\u062f\\u0631\\u0645\\u0627\\u0646\\u06cc\"},{\"id\":21,\"category_id\":\"5\",\"name\":\"\\u06a9\\u0641\\u0634 \\u0648 \\u0644\\u0628\\u0627\\u0633 \\u0628\\u0686\\u0647\"},{\"id\":22,\"category_id\":\"5\",\"name\":\"\\u0648\\u0633\\u0627\\u06cc\\u0644 \\u0628\\u0686\\u0647 \\u0648 \\u0627\\u0633\\u0628\\u0627\\u0628 \\u0628\\u0627\\u0632\\u06cc\"},{\"id\":23,\"category_id\":\"8\",\"name\":\"\\u062e\\u0648\\u062f\\u0631\\u0648\"}]";
        try {
            JSONArray responcearray = new JSONArray(value);
            for (int i = 0; i < responcearray.length(); i++) {
                JSONObject obj = new JSONArray(value).getJSONObject(i);

                if (obj.getString("category_id").equalsIgnoreCase(CatId)){

                    ifsubcatavailable =true;
                    final String subname = obj.getString("name");
                    final int subid = obj.getInt("id");

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

                            Intent intent = new Intent(App.context, NewAgahi.class);
                            // intent.putExtra("POSITION", id);
                            intent.putExtra("CATID", CatId+"");
                            intent.putExtra("SUBID", subid);
                            intent.putExtra("NAME", subname);
                            startActivity(intent);
                            finish();
                        }
                    });

                    if(layout.getParent()!=null)
                        ((ViewGroup)layout.getParent()).removeView(layout);

                    ll.addView(layout);
                    lm.addView(ll);
                }
            }

        } catch (JSONException e1) {
            dialog.hide();
            e1.printStackTrace();
        }



        if(!ifsubcatavailable){

            Intent intent = new Intent(App.context, NewAgahi.class);
            // intent.putExtra("POSITION", id);
            intent.putExtra("CATID", CatId+"");
            intent.putExtra("SUBID", "0");
            intent.putExtra("NAME", CatName+"");
            startActivity(intent);
            finish();
        }

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
                        final String subname = obj.getString("name");
                        final int subid = obj.getInt("id");

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
                                intent.putExtra("SUBID", subid);
                                intent.putExtra("NAME", subname);
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
                    Intent intent = new Intent(App.context, NewAgahi.class);
                    // intent.putExtra("POSITION", id);
                    intent.putExtra("CATID", CatId+"");
                    intent.putExtra("SUBID", "0");
                    intent.putExtra("NAME", CatName+"");
                    startActivity(intent);
                    finish();

                }else{
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }


            @Override
            public void onRetry(int retryNo) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        subs.super.onBackPressed();
        Intent intent = new Intent(App.context, cats.class);
        startActivity(intent);

    }
}
