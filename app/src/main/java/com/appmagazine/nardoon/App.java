package com.appmagazine.nardoon;

import android.Manifest;
import android.app.ActionBar;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.appmagazine.nardoon.activities.Request;
import com.appmagazine.nardoon.activities.cats;
import com.appmagazine.nardoon.activities.subs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by behroozhj on 2/20/17.
 */

public class App extends Application {

    public static Context context;
    public static SharedPreferences preferences;
    public static Typeface appfont;
    public static String urlApi;
    public static String urlimages;
    public static String android_id;
    public static String android_Model;
    public static String confirm_id;
    public static String versiontxt;
    public static int priceSms, priceLink , priceVizhe , priceEstekhdam , priceNini , priceRestaurant, appversion;
    String fileCreated = "0" ;



    public void             onCreate() {

        context=getApplicationContext();
        appfont = getFont();
        overrideFont(context,"SERIF", "Sansfarsi.ttf");
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        urlApi                 ="http://nardoun.ir/api/";
        urlimages              ="http://nardoun.ir/upload/";
        android_id = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        android_Model = Build.MODEL;
        priceSms =10;
        priceLink =10000;
        priceVizhe =15000;
        priceEstekhdam =10000;
        priceNini =5000;
        priceRestaurant =15000;
        appversion = 7;
        versiontxt = "";
        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");
        if(id_confirmaationSH!="0"){
            confirm_id=id_confirmaationSH.toString();
        }


        webServiceGetPrice();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Sansfarsi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



    }

    public static void      overrideFont( Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("TypefaceUtil", "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }

    public static void      setAllTextView(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                setAllTextView((ViewGroup) child);
            } else if (child instanceof TextView) {
                ((TextView) child).setTypeface(getFont());
            }
        }
    }

    public static Typeface  getFont() {
        return Typeface.createFromAsset(context.getAssets(), "Sansfarsi.ttf");
    }

    public static void      CustomToast(String messgae) {
        LinearLayout layout=new LinearLayout(context);
        layout.setBackgroundColor(Color.parseColor("#845381"));
        TextView view=new TextView(context);
        view.setText(messgae);
        view.setTextColor(Color.parseColor("#FFFFFF"));
        view.setTextSize(18);
        view.setShadowLayer(5,2,2,Color.parseColor("#000000"));
        view.setTypeface(appfont);
        view.setPadding(20, 20, 20, 20);
        view.setGravity(Gravity.CENTER);
        layout.addView(view);
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void      newactivity(Context context,Class activity){

        Intent intent =new Intent(context,activity);
        context.startActivity(intent);

    }


    public  void webServiceGetPrice()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"getprice", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {


                String value = new String(response);
                try {
                    JSONObject obj = new JSONObject(value);

                    priceSms =obj.getInt("sms");
                    priceLink =obj.getInt("link");
                    priceVizhe =obj.getInt("vizhe");
                    priceEstekhdam =obj.getInt("estekhdam");
                    priceNini =obj.getInt("nini");
                    priceRestaurant =obj.getInt("restaurant");
                    appversion =obj.getInt("version");
                    versiontxt =obj.getString("versiontxt");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {


            }


            @Override
            public void onRetry(int retryNo) {

            }
        });


    }


}
