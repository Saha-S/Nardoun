package com.appmagazine.nardoon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.Adapter.AgahiOrdersAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AgahiOrderDetails extends Activity {

    String type ,id;
    private JSONArray menuJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agahi_order_details);

        Intent intent=getIntent();

        TextView txtDone = (TextView) findViewById(R.id.txt_done);
        TextView txtPrice = (TextView) findViewById(R.id.txt_total_price);
        TextView txtTime = (TextView) findViewById(R.id.time);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        Button send = (Button) findViewById(R.id.btnSend);

           // String isused = intent.getStringExtra("ISUSED").toString();
         //   id =  intent.getStringExtra("ID").toString();
//            Log.i("FFFFFFFFFFF" ,id );

        /*    if(isused.equals("0")){
                send.setVisibility(View.VISIBLE);
            }
            if(isused.equals("1")){
                send.setVisibility(View.GONE);
            }
            */
            txtPrice.setText(AgahiOrdersAdapter.price+" تومان");
            txtTime.setText(AgahiOrdersAdapter.time.toString());
            //  txtDone.setText(intent.getStringExtra("DONE").toString);

        try {
            String order = AgahiOrdersAdapter.order;
            menuJsonArray = new JSONArray(order);
            Log.i("aaaaaaa122322",order );
            Log.i("aaaaaaa12234433",menuJsonArray.toString() );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < menuJsonArray.length(); i++) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row_orders, null);
                final TextView txtName = (TextView)addView.findViewById(R.id.txtName);
                final TextView txtFoodPrice = (TextView)addView.findViewById(R.id.txtPrice);
                final TextView txtNumber = (TextView)addView.findViewById(R.id.txtNumber);

                final JSONObject oo = menuJsonArray.getJSONObject(i);
                Log.i("aaaaaaa12233113",oo.getString("name").toString() );

                txtName.setText(oo.getString("name").toString());
                txtFoodPrice.setText(oo.getString("price").toString()+" تومان ");
                txtNumber.setText(oo.getString("number").toString()+" عدد ");


                container.addView(addView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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