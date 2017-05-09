package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Order extends AppCompatActivity {

    private LinearLayout container;
    private String menuOrder , allPrice , start , end;
    private JSONArray menuJsonArray;
    private JSONArray orderJsonArray = new JSONArray();
    private JSONObject jsnobject;
    private int num2;
    Button btnsabt;
    TextView totalprice;
    int index;
    private ProgressDialog dialog;
    private String id_confirmaation;
    public static  JSONArray finalJson;
    String[] factorarray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TextView tvtitle = (TextView) findViewById(R.id.tv_mainpage_title);
        TextView txtTime = (TextView) findViewById(R.id.txt_time);
        TextView appbarTitle = (TextView) findViewById(R.id.tv_appbar_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        appbarTitle.setText("مشاهده منو و سفارش");
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

        Intent intent = getIntent();
        btnsabt = (Button) findViewById(R.id.btn_sabt);
        totalprice = (TextView) findViewById(R.id.txt_all_price);
        container = (LinearLayout) findViewById(R.id.container);
        menuOrder = intent.getStringExtra("menu");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");

        txtTime.setText("ثبت سفارش از ساعت "+start+   " الی "+end+" امکان پذیر می باشد.");
        try {
            menuJsonArray = new JSONArray(menuOrder);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
        for (int i = 0; i < menuJsonArray.length(); i++) {


            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.order_row, null);
            final TextView txtName = (TextView)addView.findViewById(R.id.txtName);
            final TextView txtPrice = (TextView)addView.findViewById(R.id.txtPrice);
            final TextView txtNumber = (TextView)addView.findViewById(R.id.txt_food_number);
            final TextView txtTotalPrice = (TextView)addView.findViewById(R.id.txt_all_price);
            final ImageView ivPlus = (ImageView)addView.findViewById(R.id.iv_plus);
            final ImageView ivMinus = (ImageView)addView.findViewById(R.id.iv_minus);
            final ToggleButton ivSelect = (ToggleButton)addView.findViewById(R.id.iv_select);


            final JSONObject oo = menuJsonArray.getJSONObject(i);
            Log.i("aaaaaaa12233113",oo.getString("name").toString() );

            txtName.setText(oo.getString("name").toString());
            txtPrice.setText(oo.getString("price").toString()+" تومان ");


            //---

            final JSONObject object = new JSONObject();

            object.put("name", oo.getString("name").toString());
            object.put("price", oo.getString("price").toString());
            object.put("number", "0");

            orderJsonArray.put(object);
            //---

            ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(!ivSelect.isChecked()){
                        ivSelect.setChecked(false);
                        ivMinus.setClickable(false);
                        ivPlus.setClickable(false);
                        try {
                            index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                            orderJsonArray.getJSONObject(index).put("number","0");
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        settotalprice();

                    }else {
                        ivSelect.setChecked(true);
                        ivMinus.setClickable(true);
                        ivPlus.setClickable(true);
                        final int num = Integer.parseInt(txtNumber.getText().toString());
                        num2 = num;
                        try {
                            index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                            orderJsonArray.getJSONObject(index).put("number",num+"");

                        }catch (JSONException e) {e.printStackTrace();}

                        settotalprice();

                        ivPlus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {

                                    index = ((LinearLayout) addView.getParent()).indexOfChild(addView);

                                    num2=orderJsonArray.getJSONObject(index).getInt("number")+1;
                                    txtNumber.setText(String.valueOf(num2));
                                    txtPrice.setText(num2* orderJsonArray.getJSONObject(index).getInt("price")+" تومان ");
                                    orderJsonArray.getJSONObject(index).put("number",num2+"");

                                }catch (JSONException e) {e.printStackTrace();}

                                settotalprice();
                            }
                        });

                        ivMinus.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                try {
                                    index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                                    num2=orderJsonArray.getJSONObject(index).getInt("number");
                                }catch (Exception e){e.printStackTrace();};


                                if(num2>=1) {

                                        try {
                                            index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                                            num2=orderJsonArray.getJSONObject(index).getInt("number")-1;
                                            txtNumber.setText(String.valueOf(num2));

                                            if(num2>=1) {

                                            txtPrice.setText(num2 * orderJsonArray.getJSONObject(index).getInt("price")+" تومان ");
                                            }

                                            orderJsonArray.getJSONObject(index).put("number",num2+"");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    settotalprice();
                                }


                            }
                        });

                    }

                }
            });


            container.addView(addView);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }






        btnsabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final JSONArray finalJsonArray = new JSONArray();
                finalJson= finalJsonArray;

                for (int i=0; i<orderJsonArray.length(); i++){

                    try {

                        if(orderJsonArray.getJSONObject(i).getInt("number")>0){

                            finalJsonArray.put(orderJsonArray.getJSONObject(i));
                        }


                    }catch (JSONException e){e.printStackTrace();}


                }

                SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                String status = prefs2.getString("islogin", "0");
                String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                if (status.matches("1")) {
                    id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Order.this);

//                // set title
//                alertDialogBuilder.setTitle("-");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("سفارش شما قابل حذف یا ویرایش نیست . آیا مطمئن هستید؟")
                            .setCancelable(true)
                            .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    webServiceNewOrder(finalJsonArray);

                                }
                            })
                            .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();

                }else {
                    Intent intent = new Intent(App.context, Login.class);
                    startActivity(intent);
                }

                Log.i("0o0o0o0o0o0o0",finalJsonArray.toString() );
            }
        });

    }

    public void settotalprice(){



        int allprice = 0;

        for (int i=0; i<orderJsonArray.length(); i++){

            try {

                if(orderJsonArray.getJSONObject(i).getInt("number")>0){

                    allprice +=orderJsonArray.getJSONObject(i).getInt("number") * orderJsonArray.getJSONObject(i).getInt("price");
                }



            }catch (JSONException e){e.printStackTrace();}


        }

        totalprice.setText(allprice+"");
        allPrice= String.valueOf(allprice);


    }

    public  void webServiceNewOrder(JSONArray j)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("agahi_id",Details.idAgahi);
        params.put("order", j);
        params.put("price", allPrice);
        params.put("confirmation_id", id_confirmaation);


        client.post(App.urlApi+"factor", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                dialog = ProgressDialog.show(Order.this, null, null, true, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.progress_layout_small);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dialog.hide();
                String value = new String(response);
                Log.i("myyyyyy",value.toString() );
                factorarray = value.split("-");

                Intent intent = new Intent(App.context, MyOrders.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("confirmation_id",id_confirmaation);
                intent.putExtra("order",finalJson.toString());
                intent.putExtra("factor_id",factorarray[1]);
                intent.putExtra("factor_name",factorarray[0]);
                intent.putExtra("price",allPrice);

                startActivity(intent);
                App.CustomToast("سفارش شما ثبت شد");

                finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                dialog.hide();

                if(statusCode==403)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("ثبت سفارش در این زمان امکان پذیر نمی باشد");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                    Log.i("myerror" , errorResponse.toString());
                }
            }
        });
    }

}
