package com.appmagazine.nardoon;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
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

import java.lang.reflect.Field;

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
    public static int priceSms, priceLink , priceVizhe , priceEstekhdam , priceNini , priceRestaurant;
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
        priceNini =10000;
        priceRestaurant =15000;
        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");
        if(id_confirmaationSH!="0"){
            confirm_id=id_confirmaationSH.toString();
        }






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





}
