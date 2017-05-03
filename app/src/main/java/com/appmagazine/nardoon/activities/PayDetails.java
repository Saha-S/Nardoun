package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;

public class PayDetails extends Activity {

    public static int Sdaemi,Setebari,Sirancell =0 , number;
    String type ,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_details);

        Intent intent=getIntent();

        TextView txtType = (TextView) findViewById(R.id.txt_type);
        TextView txtDesc = (TextView) findViewById(R.id.txt_desc);
        TextView txtTest = (TextView) findViewById(R.id.test_number);
        TextView txtSend = (TextView) findViewById(R.id.send_number);
        TextView txtPrice = (TextView) findViewById(R.id.price);
        TextView txtPeygiri = (TextView) findViewById(R.id.peygiri);
        TextView txtTime = (TextView) findViewById(R.id.time);

        Button send = (Button) findViewById(R.id.btnSend);
        try {
            String num = intent.getStringExtra("CREDIT").toString();
            String isused = intent.getStringExtra("ISUSED").toString();
            id =  intent.getStringExtra("ID").toString();
            Log.i("FFFFFFFFFFF" ,id );

            if(isused.equals("0")){
                send.setVisibility(View.VISIBLE);
            }
            if(isused.equals("1")){
                send.setVisibility(View.GONE);
            }


            number= Integer.parseInt(num);

            type= intent.getStringExtra("TYPE").toString();


            if (type.equals("1")) {
                txtType.setText("همراه اول دائمی");
                Sdaemi=number;
                Setebari=0;
                Sirancell=0;

            }
            if (type.equals("2")) {
                txtType.setText("همراه اول اعتباری");
                Sdaemi=0;
                Setebari=number;
                Sirancell=0;

            }
            if (type.equals("1")) {
                txtType.setText("ایرانسل");
                Sdaemi=0;
                Setebari=0;
                Sirancell=number;

            }
            txtDesc.setText(intent.getStringExtra("DESC").toString()+" عدد");
            txtTest.setText(( Integer.parseInt(intent.getStringExtra("DESC"))-number )+" عدد");
            txtSend.setText(intent.getStringExtra("DESC").toString()+" عدد");
            txtPrice.setText(intent.getStringExtra("PRICE").toString()+" تومان");
            txtPeygiri.setText(intent.getStringExtra("PEYGIRI").toString());
            txtTime.setText(intent.getStringExtra("TIME").toString());

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