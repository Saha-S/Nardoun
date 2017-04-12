package com.appmagazine.nardoon.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.NetUtils;
import com.appmagazine.nardoon.NetUtilsCatsAgahi;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.Details;
import com.appmagazine.nardoon.activities.DetailsSms;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class SMS extends Fragment {
    EditText daemi , etebari , irancell;
    public static int Sdaemi,Setebari,Sirancell =0;
    Button price,pay;
    TextView txtPrice,txtWarning;
    int credit , number;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sm, container, false);

        daemi = (EditText) view.findViewById(R.id.edt_daemi);
        etebari = (EditText) view.findViewById(R.id.edt_etebari);
        irancell = (EditText) view.findViewById(R.id.edt_irancell);
        price = (Button) view.findViewById(R.id.btn_price);
        pay = (Button) view.findViewById(R.id.btn_pay);
        txtPrice = (TextView) view.findViewById(R.id.tv_price);
        txtWarning = (TextView) view.findViewById(R.id.txt_warning);
        new GetData().execute();


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , DetailsSms.class);
                intent.putExtra("CNT1" ,  Sdaemi);
                intent.putExtra("CNT11" ,  Setebari);
                intent.putExtra("CNT2" ,  Sirancell);
                Log.i("mylog2" ,Sdaemi + " ,,, "  +Setebari +",,,"+Sirancell );

                startActivity(intent);

            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Sdaemi = Integer.parseInt(daemi.getText().toString());
                } catch (NumberFormatException e) {
                    Sdaemi=0;
                }
                try {
                    Setebari = Integer.parseInt(etebari.getText().toString());
                } catch (NumberFormatException e) {
                    Setebari=0;
                }
                try {
                    Sirancell = Integer.parseInt(irancell.getText().toString());
                } catch (NumberFormatException e) {
                    Sirancell=0;
                }

                     number = Sdaemi+Setebari+Sirancell;
                    int sum = ((Sdaemi * 10) + (Setebari * 10) + (Sirancell * 10));
                if(sum!=0 ){
                    if(number<=credit){
                        txtPrice.setVisibility(View.VISIBLE);
                        txtPrice.setText(Integer.toString(sum) + " تومان ");
                        pay.setVisibility(View.VISIBLE);
                        txtWarning.setVisibility(View.INVISIBLE);
                    }
                    else {
                        txtPrice.setVisibility(View.VISIBLE);
                        txtPrice.setText(Integer.toString(sum) + " تومان ");
                        pay.setVisibility(View.INVISIBLE);
                        txtWarning.setVisibility(View.VISIBLE);
                    }

                }



                if(sum==0){
                    txtPrice.setVisibility(View.VISIBLE);
                    txtPrice.setText(Integer.toString(sum) + " تومان ");
                    pay.setVisibility(View.INVISIBLE);
                    txtWarning.setVisibility(View.INVISIBLE);
                }

            }

    });

        return view;

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
            credit=Integer.parseInt(result);
            super.onPostExecute(result);
        }
    }

}
