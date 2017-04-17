package com.appmagazine.nardoon;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

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


    public void             onCreate() {

        context=getApplicationContext();
        appfont = getFont();
        overrideFont(context,"SERIF", "Sansfarsi.ttf");
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        urlApi                 ="http://192.168.0.244/nardoon/public/api/";
        urlimages              ="http://192.168.0.244/nardoon/public/upload/";
        android_id = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        android_Model = Build.MODEL;



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
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void      newactivity(Context context,Class activity){

        Intent intent =new Intent(context,activity);
        context.startActivity(intent);

    }





}
