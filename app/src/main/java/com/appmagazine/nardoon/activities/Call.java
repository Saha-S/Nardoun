package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.R;

public class Call extends Activity {
    String mobile,email ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        Intent intent=getIntent();

        TextView tvCall = (TextView) findViewById(R.id.tv_call);
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        LinearLayout llEmail = (LinearLayout) findViewById(R.id.ll_email);
        LinearLayout llCall = (LinearLayout) findViewById(R.id.ll_call);
        LinearLayout llMessage = (LinearLayout) findViewById(R.id.ll_message);

        mobile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
        if(email.equals("")){
            llEmail.setVisibility(View.GONE);
        }
        tvCall               .setText("تماس با  "+ mobile);
        tvMessage               .setText("ارسال پیام به "+ mobile);
        tvEmail               .setText("ارسال ایمیل به "+email);

        llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", mobile);
                startActivity(smsIntent);


            }
        });

        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobile));
                startActivity(intent);


            }
        });
        llEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email});
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));


            }
        });


    }
}