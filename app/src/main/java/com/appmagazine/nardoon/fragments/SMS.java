package com.appmagazine.nardoon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.Details;
import com.appmagazine.nardoon.activities.DetailsSms;
import com.appmagazine.nardoon.activities.Login;
import com.appmagazine.nardoon.activities.MyPanel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import ir.moslem_deris.apps.zarinpal.PaymentBuilder;
import ir.moslem_deris.apps.zarinpal.ZarinPal;
import ir.moslem_deris.apps.zarinpal.enums.ZarinPalError;
import ir.moslem_deris.apps.zarinpal.listeners.OnPaymentListener;
import ir.moslem_deris.apps.zarinpal.models.Payment;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class SMS extends Fragment {
    public static int Sdaemi,Setebari,Sirancell =0;
    Button price,pay;
    TextView txtPrice,txtWarning , txtCharacter;
    int credit , number, operator , priceint;
    int countSMS=1;
    RadioGroup radioOperatorGroup;
    RadioButton radioOperatorButton;
    EditText matn , numberSMS;
    private String id_confirmaation;
    private String peygiri ;
    int typeSMS=0;
    private LinearLayout llnewagahi;

//    public static ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        pay = (Button) view.findViewById(R.id.btn_pay);
        txtCharacter = (TextView) view.findViewById(R.id.txt_character);
        txtWarning = (TextView) view.findViewById(R.id.txt_warning);
        txtPrice = (TextView) view.findViewById(R.id.txt_price);
        matn = (EditText) view.findViewById(R.id.edt_matn);
        numberSMS = (EditText) view.findViewById(R.id.edt_number);
        radioOperatorGroup = (RadioGroup) view.findViewById(R.id.radio_operator);

        int selectedId = radioOperatorGroup.getCheckedRadioButtonId();
        radioOperatorButton = (RadioButton) view.findViewById(selectedId);

        Object connectivityService = App.context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        //     ConnecivityMatnager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() || isMobileDataEnabled()) {
            new GetData().execute();
        }else
            App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");



        final TextWatcher inputTextWatcherMatn = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length()<=70){
                    txtCharacter.setText("1 پیامک / "+s.length());
                    countSMS=1;
                    try {
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number = Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint = Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}

                }
                if(s.length()<=134 && s.length()> 70) {
                    txtCharacter.setText("2 پیامک / "+s.length());
                    countSMS = 2;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}

                }
                if(s.length()<=201 && s.length()> 134) {
                    txtCharacter.setText("3 پیامک / "+s.length());
                    countSMS = 3;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}
                }
                if(s.length()<=268 && s.length()> 201) {
                    txtCharacter.setText("4 پیامک / "+s.length());
                    countSMS = 4;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}

                }
                if(s.length()<=335 && s.length()> 268) {
                    txtCharacter.setText("5 پیامک / "+s.length());
                    countSMS = 5;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}

                }
                if(s.length()<=402 && s.length()> 235) {
                    txtCharacter.setText("6 پیامک / "+s.length());
                    countSMS = 6;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}
                }
                if(s.length()<=469 && s.length()> 402) {
                    txtCharacter.setText("7 پیامک / "+s.length());
                    countSMS = 7;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}

                }
                if(s.length()<=536 && s.length()> 469) {
                    txtCharacter.setText("8 پیامک / "+s.length());
                    countSMS = 8;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}
                }
                if(s.length()<=603 && s.length()> 536) {
                    txtCharacter.setText("9 پیامک / "+s.length());
                    countSMS = 9;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}
                }
                if(s.length()<=670 && s.length()> 603) {
                    txtCharacter.setText("10 پیامک / "+s.length());
                    countSMS = 10;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}
                }
                if(s.length()<=737 && s.length()> 670) {
                    txtCharacter.setText("10 پیامک / "+s.length());
                    countSMS = 11;
                    try{
                        txtPrice.setText(Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(numberSMS.getText().toString()) * countSMS;
                        priceint=Integer.parseInt(numberSMS.getText().toString()) * App.priceSms * countSMS;
                    }catch (NumberFormatException e){}
                }
                if(s.length()>737){
                    txtWarning.setVisibility(View.VISIBLE);
                    pay.setVisibility(View.GONE);
                }


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        matn.addTextChangedListener(inputTextWatcherMatn);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                matn.addTextChangedListener(inputTextWatcherMatn);

                try {
                    if (countSMS == 1) {
                        txtPrice.setText(Integer.parseInt(s.toString()) * App.priceSms + " تومان ");
                        number=Integer.parseInt(s.toString()) * countSMS;
                        priceint = Integer.parseInt(s.toString()) * App.priceSms;
                        Log.i("number" ,"aa"+number );

                    } else {
                        txtPrice.setText(Integer.parseInt(s.toString()) * App.priceSms * countSMS + " تومان ");
                        number=Integer.parseInt(s.toString()) * countSMS;
                        priceint=Integer.parseInt(s.toString()) * App.priceSms * countSMS;
                        Log.i("number" ,"aa"+number );
                    }
                }catch (NumberFormatException e){
                    txtPrice.setText( " 0 تومان ");
                }
                try {
                    if (Integer.parseInt(s.toString()) * countSMS >= credit) {
                        pay.setVisibility(View.INVISIBLE);
                        txtWarning.setVisibility(View.VISIBLE);
                    }else{
                        pay.setVisibility(View.VISIBLE);
                        txtWarning.setVisibility(View.INVISIBLE);
                    }
                }catch (NumberFormatException e){
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){


            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        numberSMS.addTextChangedListener(inputTextWatcher);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences prefs = App.context.getSharedPreferences("LOGIN_ID", 0);
                SharedPreferences prefs2 = App.context.getSharedPreferences("IS_LOGIN", 0);
                String status = prefs2.getString("islogin", "0");
                String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                if (status.matches("1")) {
                    id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");
                    if (radioOperatorButton.getText().toString() == getString(R.string.daemi)) {
                        Sdaemi = number;
                        Setebari = 0;
                        Sirancell = 0;
                        typeSMS = 1;
                        Log.i("mylog2", Sdaemi + " ,,, " + Setebari + ",,," + Sirancell);
                    } else if (radioOperatorButton.getText() == getString(R.string.etebari)) {
                        Sdaemi = 0;
                        Setebari = number;
                        Sirancell = 0;
                        typeSMS = 2;
                    } else if (radioOperatorButton.getText() == getString(R.string.daemi)) {
                        Sdaemi = 0;
                        Setebari = 0;
                        Sirancell = number;
                        typeSMS = 3;
                    }
                    Object connectivityService = App.context.getSystemService(CONNECTIVITY_SERVICE);
                    ConnectivityManager cm = (ConnectivityManager) connectivityService;

               //     ConnecivityMatnager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {
                        pay();
                    }else
                        App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");

                } else {
                    Intent intent = new Intent(App.context, Login.class);
                    startActivity(intent);
                }


            }
        });

        return view;

    }


    private void pay(){
        Payment payment = new PaymentBuilder()
                .setMerchantID("f1bd82da-273d-11e7-9b41-005056a205be")  //  This is an example, put your own merchantID here.
                .setAmount(priceint)                                        //  In Toman
                .setDescription("پرداخت تست پلاگین اندروید")
                .setEmail("moslem.deris@gmail.com")                     //  This field is not necessary.
                .setMobile("09123456789")                               //  This field is not necessary.
                .create();
             //   dialog.hide();

        ZarinPal.pay(getActivity(), payment, new OnPaymentListener() {
            @Override
            public void onSuccess(String refID) {
                peygiri=refID.toString();
                Log.wtf("TAG", "::ZarinPal::  RefId: " + refID);
                webServiceBuylog();

            }
            @Override
            public void onFailure(ZarinPalError error) {
                String errorMessage = "";
                switch (error){
                    case INVALID_PAYMENT: errorMessage = "پرداخت تایید نشد";           break;
                    case USER_CANCELED:   errorMessage = "پرداخت توسط کاربر متوقف شد"; break;
                    case NOT_ENOUGH_DATA: errorMessage = "اطلاعات پرداخت کافی نیست";    break;
                    case UNKNOWN:         errorMessage = "خطای ناشناخته";              break;
                }
                Log.wtf("TAG", "::ZarinPal::  ERROR: " + errorMessage);
             //   textView.setText("خطا!!!" + "\n" + errorMessage);
            }
        });
    }


    class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL("http://nardoun.ir/api/getcredit");
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                if(code==200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                urlConnection.disconnect();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                credit = Integer.parseInt(result);
            }catch (Exception e){}
            super.onPostExecute(result);
        }
    }

    public  void webServiceBuylog()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("confirmation_id",id_confirmaation.toString());
        params.put("type",""+typeSMS);
        params.put("related_id","1");
        params.put("description", Sdaemi+Setebari+Sirancell+"");
        params.put("price",priceint);
        params.put("traking_code","123456");
        params.put("isused","0");
        params.put("credit",Sdaemi+Setebari+Sirancell+"");

        client.post(App.urlApi+"buylog", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                App.CustomToast("اطلاعات خرید ثبت شد");
                App.CustomToast(value.toString().replace("[{\"id\":", "").replace("}]" , ""));
                Intent intent = new Intent(App.context , DetailsSms.class);
                intent.putExtra("MATN" , matn.getText().toString() );
                intent.putExtra("FLAG" , "sms" );
                intent.putExtra("ID" , value.toString().replace("[{\"id\":", "").replace("}]" , "") );
                startActivity(intent);
                getActivity().finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    //dialog.hide();
                    App.CustomToast("ثبت اطلاعات خرید با مشکل مواجه شد !");

                }else{
                   // dialog.hide();
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast("اطلاعات خرید ثبت نشد ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public Boolean isMobileDataEnabled(){
        Object connectivityService = App.context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
