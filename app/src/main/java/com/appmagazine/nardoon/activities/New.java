package com.appmagazine.nardoon.activities;

        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;

        import com.appmagazine.nardoon.App;
        import com.appmagazine.nardoon.R;
        import com.loopj.android.http.AsyncHttpClient;
        import com.loopj.android.http.AsyncHttpResponseHandler;
        import com.loopj.android.http.RequestParams;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.URL;

        import cz.msebera.android.httpclient.Header;

        public class New extends AppCompatActivity {

            EditText price,email,phone , title , content;
            String name , id , type;
            RadioGroup radioTypeGroup;
            RadioButton radioTypeButton;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_new);

                Button SelectCat = (Button) findViewById(R.id.btn_cats);
                price = (EditText) findViewById(R.id.edt_price);
                email = (EditText) findViewById(R.id.edt_email);
                phone = (EditText) findViewById(R.id.edt_phone);
                title = (EditText) findViewById(R.id.edt_title);
                content = (EditText) findViewById(R.id.edt_content);
                LinearLayout llForm = (LinearLayout) findViewById(R.id.ll_form);
                LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
                radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);



                Intent intent=getIntent();
                name = intent.getStringExtra("NAME");
                id = intent.getStringExtra("ID");

                if (name!= null){
                    SelectCat.setText(name);
                    llForm.setVisibility(LinearLayout.VISIBLE);

                }

                llErsal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = radioTypeGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        radioTypeButton = (RadioButton) findViewById(selectedId);

                        if(radioTypeButton!=null) {

                            type = radioTypeButton.getText().toString();
                        }
                        Log.i("mytype" , "type  : "+type);

                        webServiceNewAgahi();
                    }
                });

                SelectCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.context , Categorys.class);
                        startActivity(intent);
                    }
                });
            }

            public  void webServiceNewAgahi()
            {

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("title", title.getText()); //  ********** parametr  ersali dar surate niaz
                params.put("content", content.getText());
                params.put("price", price.getText());
                params.put("email", email.getText());
                params.put("mobile", phone.getText());
                params.put("type",type);
                params.put("category_id",id);
                params.put("subcategory_id","1");
                params.put("image","jja");
                params.put("deviceid",App.android_id);
                params.put("devicemodel",App.android_Model);
                params.put("location","ولی عصر");
                Log.i("myurl" , App.urlApi+"agahis/");
                client.post(App.urlApi+"agahis", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
                    @Override
                    public void onStart() {
                        // called before request is started


                        // loginpb1.setVisibility(View.VISIBLE);      *************** inja  progressbar faal mishe
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK" ************** inja vaqti successful shod code 200 daryaft kard mituni json parse koni
                        // loginpb1.setVisibility(View.INVISIBLE);



                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                        // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                        if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                        {
                            App.CustomToast("آگهی با این شماره وجود ندارد !");

                        }else{
                            App.CustomToast("fail "+statusCode);
                            App.CustomToast(" لطفا دوباره سعی کنید ");
                        }
                    }
                });
            }


        }