package com.appmagazine.nardoon.activities;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.fragments.SMS;

public class SuccessSendSms extends AppCompatActivity {

    TextView txtDaemi , txtEtebari , txtIrancell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_send_sms);

        ImageButton ibmenu=(ImageButton) findViewById(R.id.ib_menu);

        ibmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }

            }
        });

        txtDaemi =(TextView) findViewById(R.id.txt_daemi);
        txtEtebari =(TextView) findViewById(R.id.txt_etebari);
        txtIrancell =(TextView) findViewById(R.id.txt_irancell);

        txtDaemi.setText("تعداد پیامک های دائمی همراه اول : "+ SMS.Sdaemi);
        txtEtebari.setText("تعداد پیامک های اعتباری همراه اول : "+ SMS.Setebari);
        txtIrancell.setText("تعداد پیامک های ایرانسل : "+ SMS.Sirancell);

    }
}
