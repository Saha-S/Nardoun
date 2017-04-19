package com.appmagazine.nardoon.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.DetailsSms;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SMS extends Fragment {
    public static int Sdaemi,Setebari,Sirancell =0;
    Button price,pay;
    TextView txtPrice,txtWarning , txtCharacter;
    int credit , number, operator;
    int countSMS=1;
    RadioGroup radioOperatorGroup;
    RadioButton radioOperatorButton;
    EditText matn , numberSMS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        pay = (Button) view.findViewById(R.id.btn_pay);
        txtCharacter = (TextView) view.findViewById(R.id.txt_character);
        txtWarning = (TextView) view.findViewById(R.id.txt_warning);
        txtPrice = (TextView) view.findViewById(R.id.txt_price);
        matn = (EditText) view.findViewById(R.id.edt_matn);
        numberSMS = (EditText) view.findViewById(R.id.edt_number);
        radioOperatorGroup = (RadioGroup) view.findViewById(R.id.radio_operator);

        int selectedId = radioOperatorGroup.getCheckedRadioButtonId();
        radioOperatorButton = (RadioButton) view.findViewById(selectedId);


        new GetData().execute();


        TextWatcher inputTextWatcherMatn = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length()<=70){
                    txtCharacter.setText("1 پیامک / "+s.length());
                    countSMS=1;
                }
                else if(s.length()<=134 && s.length()> 70) {
                    txtCharacter.setText("2 پیامک / "+s.length());
                    countSMS = 2;
                }
                else if(s.length()<=201 && s.length()> 134) {
                    txtCharacter.setText("3 پیامک / "+s.length());
                    countSMS = 3;
                }
                else if(s.length()<=268 && s.length()> 201) {
                    txtCharacter.setText("4 / "+s.length());
                    countSMS = 4;
                }
                else if(s.length()<=335 && s.length()> 268) {
                    txtCharacter.setText("5 / "+s.length());
                    countSMS = 5;
                }


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        matn.addTextChangedListener(inputTextWatcherMatn);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                try {
                    if (countSMS == 1) {
                        txtPrice.setText(Integer.parseInt(s.toString()) * App.smsPrice + " تومان ");
                        number=Integer.parseInt(s.toString()) * countSMS;
                        Log.i("number" ,"aa"+number );

                    } else {
                        txtPrice.setText(Integer.parseInt(s.toString()) * App.smsPrice * countSMS + " تومان ");
                        number=Integer.parseInt(s.toString()) * countSMS;
                        Log.i("number" ,"aa"+number );


                    }
                }catch (NumberFormatException e){
                    txtPrice.setText( " 0 تومان ");
                }
                try {
                    if (Integer.parseInt(s.toString()) * countSMS >= credit) {
                        pay.setVisibility(View.INVISIBLE);
                        txtWarning.setVisibility(View.VISIBLE);
                    } else {
                        pay.setVisibility(View.VISIBLE);
                        txtWarning.setVisibility(View.INVISIBLE);
                    }
                }catch (NumberFormatException e){}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){


            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        numberSMS.addTextChangedListener(inputTextWatcher);



        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(App.context , DetailsSms.class);
                intent.putExtra("MATN" , matn.getText().toString() );
                Log.i("mylog3" ,radioOperatorButton.getText().toString() );
                Log.i("mylog4" ,getString(R.string.daemi) );

                if(radioOperatorButton.getText().toString()==getString(R.string.daemi)){
                    Sdaemi=number;
                    Setebari=0;
                    Sirancell=0;
                    Log.i("mylog2" ,Sdaemi + " ,,, "  +Setebari +",,,"+Sirancell );
                }else if(radioOperatorButton.getText()==getString(R.string.etebari)){
                    Sdaemi=0;
                    Setebari=number;
                    Sirancell=0;
                }else if(radioOperatorButton.getText()==getString(R.string.daemi)){
                    Sdaemi=0;
                    Setebari=0;
                    Sirancell=number;
                }


                startActivity(intent);

            }
        });

        return view;

    }


    class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL("http://nardoun.ir/api/getcredit");
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                if(code==200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                urlConnection.disconnect();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            credit=Integer.parseInt(result);
            super.onPostExecute(result);
        }
    }

}
