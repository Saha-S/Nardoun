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
    LinearLayout llGirl , llPublic , llSport , llSocial , llRSS;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        llGirl = (LinearLayout) view.findViewById(R.id.llGirly);
        llPublic = (LinearLayout) view.findViewById(R.id.llPublic);
        llSport = (LinearLayout) view.findViewById(R.id.llSport);
        llSocial = (LinearLayout) view.findViewById(R.id.llSocial);
        llRSS = (LinearLayout) view.findViewById(R.id.llRSS);

//        llGirl.setBackgroundColor(Color.parseColor("#003399"));
//        llPublic.setBackgroundColor(Color.parseColor("#9a00ff"));
//        llSport.setBackgroundColor(Color.parseColor("#ff6501"));
//        llSocial.setBackgroundColor(Color.parseColor("#cd9933"));
//        llRSS.setBackgroundColor(Color.parseColor("#04b9e6"));

        llGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "دخترانه");
                startActivity(intent);
            }
        });
        llPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "عمومی");
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
        llRSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context , NewsList.class);
//                intent.putExtra("id", "8");
                intent.putExtra("name", "اخبار روزانه");
                startActivity(intent);
            }
        });
        return view;

    }
}
