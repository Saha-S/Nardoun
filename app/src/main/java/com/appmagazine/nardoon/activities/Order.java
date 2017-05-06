package com.appmagazine.nardoon.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Order extends AppCompatActivity {

    private LinearLayout container;
    private String menuOrder;
    private JSONArray menuJsonArray;
    private JSONArray orderJsonArray;
    private JSONObject jsnobject;
    private int num2;
    int totalPrice=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        container = (LinearLayout) findViewById(R.id.container);
        menuOrder = intent.getStringExtra("menu");
        try {
            menuJsonArray = new JSONArray(menuOrder);
            jsnobject = new JSONObject(menuOrder);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("aaaaaaa122333",menuJsonArray.toString() );
        try {
        for (int i = 0; i < menuJsonArray.length(); i++) {
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(App.context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.order_row, null);
            final TextView txtName = (TextView)addView.findViewById(R.id.txtName);
            final TextView txtPrice = (TextView)addView.findViewById(R.id.txtPrice);
            final TextView txtNumber = (TextView)addView.findViewById(R.id.txt_food_number);
            final TextView txtTotalPrice = (TextView)addView.findViewById(R.id.txt_all_price);
            final ImageView ivPlus = (ImageView)addView.findViewById(R.id.iv_plus);
            final ImageView ivMinus = (ImageView)addView.findViewById(R.id.iv_minus);
            final ToggleButton ivSelect = (ToggleButton)addView.findViewById(R.id.iv_select);


            final JSONObject oo = menuJsonArray.getJSONObject(i);
            Log.i("aaaaaaa12233113",oo.getString("name").toString() );

            txtName.setText(oo.getString("name").toString());
            txtPrice.setText(oo.getString("price").toString()+" تومان ");


            ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ivSelect.isChecked()){
                        ivSelect.setChecked(false);
                        ivMinus.setClickable(false);
                        ivPlus.setClickable(false);
                    }else {
                        ivSelect.setChecked(true);
                        ivMinus.setClickable(true);
                        ivPlus.setClickable(true);
                        final int num =0 ;
                        txtNumber.setText(String.valueOf(num) );

                        num2 = num;
                        final JSONObject object = new JSONObject();

                        ivPlus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    num2=num2+1;

                                txtNumber.setText(String.valueOf(num2));
//                                txtTotalPrice.setText(String.valueOf(totalPrice));
                            }
                        });

                        ivMinus.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                int index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                           //     orderJsonArray.remove(index);
                                if(num2>=1) {
                                    num2 = num2 - 1;

                                }
                                txtNumber.setText(String.valueOf(num2));


                            }
                        });

                        if(num2>0) {
                            try {
                                object.put("name", oo.getString("name").toString());
                                object.put("price", oo.getString("price").toString());
                                object.put("number", oo.getString("price").toString());
                                txtNumber.setText(String.valueOf(num2));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            orderJsonArray.put(object);
                            Log.i("cccccccc2" ,orderJsonArray.toString());

                        }
                    }

                }
            });


            container.addView(addView);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
