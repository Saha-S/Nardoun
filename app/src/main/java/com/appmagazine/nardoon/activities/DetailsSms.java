package com.appmagazine.nardoon.activities;

import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.appmagazine.nardoon.fragments.SMS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DetailsSms extends AppCompatActivity {

    EditText edtContent , edtMobile ;
    String cnt1 , cnt2 , cnt11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_sms);


        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
        TextView tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        tvtitle.setTypeface(tfmorvarid);

        edtContent = (EditText) findViewById(R.id.edt_content);
        edtMobile = (EditText) findViewById(R.id.edt_mobile);
        Intent intent=getIntent();
        cnt11 = intent.getStringExtra("CNT11");
        cnt1 = intent.getStringExtra("CNT1");
        cnt2 = intent.getStringExtra("CNT2");
        Log.i("mylog" ,cnt1 + " ,,, "  +cnt11 +",,,"+cnt2 );
        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);

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

    }
    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("ارسال انبوه پیامک ، آیا مطمئن هستید؟");
                alertDialogBuilder.setPositiveButton("بلی",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                webServiceSendSMS();

                            }
                        });

        alertDialogBuilder.setNegativeButton("خیر",new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public  void webServiceSendSMS()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // File myFile = new File("/path/to/file.png");

        params.put("content", edtContent.getText()); //  ********** parametr  ersali dar surate niaz
      //  params.put("count", intent.getStringExtra("COUNT"));
        params.put("cnt11", SMS.Setebari);
        params.put("cnt1", SMS.Sdaemi);
        params.put("cnt2", SMS.Sirancell);



        client.post("http://nardoun.ir/api/sendsms", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //dialog.hide();
                String value = new String(response);
              //  App.CustomToast("ارسال شد");
              //  App.CustomToast(value);
               // Log.i("mylog3" ,"ersal"+value );

                Intent intent = new Intent(App.context, SuccessSendSms.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                //dialog.hide();

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                //    Log.i("myerror" , errorResponse.toString());
                }
            }
        });
    }

    public  void webServiceTestSMS(View view)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // File myFile = new File("/path/to/file.png");

        Intent intent=getIntent();
        params.put("content", edtContent.getText()); //  ********** parametr  ersali dar surate niaz
        params.put("number", edtMobile.getText());



        client.post("http://nardoun.ir/api/testsms", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //dialog.hide();
                App.CustomToast("پیامک با موفقیت ارسال شد");

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                //dialog.hide();

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                    Log.i("myerror" , errorResponse.toString());
                }
            }
        });
    }

}
