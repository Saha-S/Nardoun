package com.appmagazine.nardoon.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    private JSONArray orderJsonArray = new JSONArray();
    private JSONObject jsnobject;
    private int num2;
    int totalPrice=0;
    Button btnsabt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        btnsabt = (Button) findViewById(R.id.btn_sabt);
        container = (LinearLayout) findViewById(R.id.container);
        menuOrder = intent.getStringExtra("menu");
        try {
            menuJsonArray = new JSONArray(menuOrder);

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
            final TextView txtindex = (TextView)addView.findViewById(R.id.txt_index);
            final TextView txtTotalPrice = (TextView)addView.findViewById(R.id.txt_all_price);
            final ImageView ivPlus = (ImageView)addView.findViewById(R.id.iv_plus);
            final ImageView ivMinus = (ImageView)addView.findViewById(R.id.iv_minus);
            final ToggleButton ivSelect = (ToggleButton)addView.findViewById(R.id.iv_select);


            final JSONObject oo = menuJsonArray.getJSONObject(i);
            Log.i("aaaaaaa12233113",oo.getString("name").toString() );

            txtindex.setText(i+"");
            txtName.setText(oo.getString("name").toString());
            txtPrice.setText(oo.getString("price").toString()+" تومان ");


            //---

            final JSONObject object = new JSONObject();

            object.put("name", oo.getString("name").toString());
            object.put("price", oo.getString("price").toString());
            object.put("number", "0");

            orderJsonArray.put(object);
            //---

            ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int index = Integer.parseInt(txtindex.getText().toString());
                    if(!ivSelect.isChecked()){
                        ivSelect.setChecked(false);
                        ivMinus.setClickable(false);
                        ivPlus.setClickable(false);

                        try {
                            orderJsonArray.getJSONObject(index).put("number","0");
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {
                        ivSelect.setChecked(true);
                        ivMinus.setClickable(true);
                        ivPlus.setClickable(true);

                        final int num = Integer.parseInt(txtNumber.getText().toString());
                        num2 = num;
                        try {

                            orderJsonArray.getJSONObject(index).put("number",num+"");

                        }catch (JSONException e) {e.printStackTrace();}



                        ivPlus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    num2=num2+1;

                                txtNumber.setText(String.valueOf(num2));
                                try {

                                    txtPrice.setText(num2* orderJsonArray.getJSONObject(index).getInt("price")+" تومان ");
                                    orderJsonArray.getJSONObject(index).put("number",num2+"");


                                }catch (JSONException e) {e.printStackTrace();}
                            }
                        });

                        ivMinus.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                int index = ((LinearLayout) addView.getParent()).indexOfChild(addView);
                           //     orderJsonArray.remove(index);
                                if(num2>=1) {

                                    num2=num2-1;

                                    txtNumber.setText(String.valueOf(num2));

                                        try {

                                            if(num2>=1) {
                                            txtPrice.setText(num2 * orderJsonArray.getJSONObject(index).getInt("price")+" تومان ");
                                            }

                                            orderJsonArray.getJSONObject(index).put("number",num2+"");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                }


                            }
                        });

                    }

                }
            });


            container.addView(addView);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }






        btnsabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONArray finalJsonArray = new JSONArray();

                for (int i=0; i<orderJsonArray.length(); i++){

                    try {

                        if(orderJsonArray.getJSONObject(i).getInt("number")>0){

                            finalJsonArray.put(orderJsonArray.getJSONObject(i));
                        }

                    }catch (JSONException e){e.printStackTrace();}


                }


                Log.i("0o0o0o0o0o0o0",finalJsonArray.toString() );
            }
        });

    }
}
