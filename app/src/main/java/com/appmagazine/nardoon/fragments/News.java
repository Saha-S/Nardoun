package com.appmagazine.nardoon.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.CatAgahis;
import com.appmagazine.nardoon.activities.NewsList;

public class News extends Fragment {
    LinearLayout llGirl , llPublic , llSport , llSocial , llRSS , llAll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        llGirl = (LinearLayout) view.findViewById(R.id.llGirly);
        llPublic = (LinearLayout) view.findViewById(R.id.llPublic);
        llSport = (LinearLayout) view.findViewById(R.id.llSport);
        llSocial = (LinearLayout) view.findViewById(R.id.llSocial);

        LinearLayout rssVarzeshi = (LinearLayout) view.findViewById(R.id.rssVarzeshi);
        LinearLayout rssPezeshki = (LinearLayout) view.findViewById(R.id.rssPezeshki);
        LinearLayout rssMajazi = (LinearLayout) view.findViewById(R.id.rssMajazi);
        LinearLayout rssEghtesadi = (LinearLayout) view.findViewById(R.id.rssEghtesadi);
        LinearLayout rssSiasi = (LinearLayout) view.findViewById(R.id.rssSiasi);
        LinearLayout rssEjtemaei = (LinearLayout) view.findViewById(R.id.rssEjtemaei);


        //  llRSS = (LinearLayout) view.findViewById(R.id.llRSS);
        llAll = (LinearLayout) view.findViewById(R.id.llAll);


        llPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "سرگرمی و آموزشی");
                startActivity(intent);
            }
        });
        llSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "ورزشی");
                startActivity(intent);
            }
        });
        llSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "اجتماعی");
                startActivity(intent);
            }
        });
        llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "همه ی اخبار");
                startActivity(intent);
            }
        });


        /////////////////// RSS ////////////

        rssVarzeshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
                intent.putExtra("name", "rssVarzeshi");
                startActivity(intent);
            }
        });
        rssPezeshki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
                intent.putExtra("name", "rssPezeshki");
                startActivity(intent);
            }
        });
        rssMajazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
                intent.putExtra("name", "rssMajazi");
                startActivity(intent);
            }
        });
        rssEghtesadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
                intent.putExtra("name", "rssEghtesadi");
                startActivity(intent);
            }
        });
        rssSiasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
                intent.putExtra("name", "rssSiasi");
                startActivity(intent);
            }
        });
        rssEjtemaei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
                intent.putExtra("name", "rssEjtemaei");
                startActivity(intent);
            }
        });


        return view;

    }
}
