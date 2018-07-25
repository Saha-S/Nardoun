package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PayDetails extends Activity {

    public static int Sdaemi,Setebari,Sirancell =0 , number;
    String type ,id;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_details);


        Intent intent=getIntent();

        TextView txtDaemi = (TextView) findViewById(R.id.daemi);
        TextView txtEtebari = (TextView) findViewById(R.id.etebari);
        TextView txtIrancell = (TextView) findViewById(R.id.irancell);
        TextView txtDesc = (TextView) findViewById(R.id.txt_desc);
        TextView txtTest = (TextView) findViewById(R.id.test_number);
        TextView txtSend = (TextView) findViewById(R.id.send_number);
        TextView txtPrice = (TextView) findViewById(R.id.price);
        TextView txtPeygiri = (TextView) findViewById(R.id.peygiri);
        TextView txtTime = (TextView) findViewById(R.id.time);

        Button send = (Button) findViewById(R.id.btnSend);

        Sdaemi= Integer.parseInt(intent.getStringExtra("DAEMI").toString());
        Setebari= Integer.parseInt(intent.getStringExtra("ETEBARI").toString());
        Sirancell= Integer.parseInt(intent.getStringExtra("IRANCELL").toString());
        int all = Integer.parseInt(intent.getStringExtra("IRANCELL").toString()) + Integer.parseInt(intent.getStringExtra("ETEBARI").toString())+Integer.parseInt(intent.getStringExtra("DAEMI").toString());

        try {
            String num = intent.getStringExtra("CREDIT").toString();
            String isused = intent.getStringExtra("ISUSED").toString();
            id =  intent.getStringExtra("ID").toString();
            number= Integer.parseInt(num);

            txtDesc.setText(all+" عدد");
            txtDaemi.setText(Sdaemi+" عدد");
            txtEtebari.setText(Setebari+" عدد");
            txtIrancell.setText(Sirancell+" عدد");
            txtTest.setText(( all-number )+" عدد");
            txtPrice.setText(intent.getStringExtra("PRICE").toString()+" تومان");
            txtPeygiri.setText(intent.getStringExtra("PEYGIRI").toString());
            txtTime.setText(intent.getStringExtra("TIME").toString());


            if(isused.equals("0")){
                send.setVisibility(View.VISIBLE);
                txtSend.setText("0 عدد");
            }
            if(isused.equals("1")){
                send.setVisibility(View.GONE);
                txtSend.setText(number + " عدد");
            }






        }catch (Exception e){}


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , DetailsSms.class);
                intent.putExtra("ID" ,id );
                intent.putExtra("FLAG" ,"panel" );

                startActivity(intent);
            }
        });

    }
}