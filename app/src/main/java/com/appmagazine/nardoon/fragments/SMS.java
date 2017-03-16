package com.appmagazine.nardoon.fragments;

import android.content.Context;
import android.net.Uri;
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

import com.appmagazine.nardoon.R;

import java.util.Set;

public class SMS extends Fragment {
    EditText daemi , etebari , irancell;
    int Sdaemi,Setebari,Sirancell =0;
    Button price,pay;
    TextView txtPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sm, container, false);

        daemi = (EditText) view.findViewById(R.id.edt_daemi);
        etebari = (EditText) view.findViewById(R.id.edt_etebari);
        irancell = (EditText) view.findViewById(R.id.edt_irancell);
        price = (Button) view.findViewById(R.id.btn_price);
        pay = (Button) view.findViewById(R.id.btn_pay);
        txtPrice = (TextView) view.findViewById(R.id.tv_price);



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


                    int sum = ((Sdaemi * 10) + (Setebari * 10) + (Sirancell * 10));
                if(sum!=0){
                    txtPrice.setVisibility(View.VISIBLE);
                    txtPrice.setText(Integer.toString(sum) + " تومان ");
                    pay.setVisibility(View.VISIBLE);}

                if(sum==0){
                    txtPrice.setVisibility(View.VISIBLE);
                    txtPrice.setText(Integer.toString(sum) + " تومان ");
                    pay.setVisibility(View.INVISIBLE);}

            }

    });

        return view;

    }
}
