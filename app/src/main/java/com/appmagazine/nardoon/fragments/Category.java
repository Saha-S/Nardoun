package com.appmagazine.nardoon.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.SubCat;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Category extends Fragment {
    LinearLayout llCar , llHouse , llLamp , llPersonal, llWork,llGame, llOther,llPc;
    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        url=App.urlApi+"categories";

        llCar = (LinearLayout) view.findViewById(R.id.llCar);
        llHouse = (LinearLayout) view.findViewById(R.id.llHouse);
        llLamp = (LinearLayout) view.findViewById(R.id.llLamp);
        llPersonal = (LinearLayout) view.findViewById(R.id.llPersonal);
        llWork = (LinearLayout) view.findViewById(R.id.llWork);
        llGame = (LinearLayout) view.findViewById(R.id.llGame);
        llPc = (LinearLayout) view.findViewById(R.id.llPc);
        llOther = (LinearLayout) view.findViewById(R.id.llOther);

        llCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "1");
                intent.putExtra("name", "وسایل نقلیه");
                startActivity(intent);
            }
        });
        llHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "2");
                intent.putExtra("name", "املاک");
                startActivity(intent);
            }
        });
        llLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "3");
                intent.putExtra("name", "مربوط به خانه");
                startActivity(intent);
            }
        });
        llPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "4");
                intent.putExtra("name", "لوازم الکترونیکی");
                startActivity(intent);
            }
        });
        llPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "5");
                intent.putExtra("name", "وسایل شخصی");
                startActivity(intent);
            }
        });
        llWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "6");
                intent.putExtra("name", "خدمات");
                startActivity(intent);
            }
        });
        llGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "7");
                intent.putExtra("name", "سرگرمی و فراغت");
                startActivity(intent);
            }
        });
        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , SubCat.class);
                intent.putExtra("id", "8");
                intent.putExtra("name", "متفرقه");
                startActivity(intent);
            }
        });


        return view;

    }
}