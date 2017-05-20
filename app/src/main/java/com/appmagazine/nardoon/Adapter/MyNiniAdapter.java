package com.appmagazine.nardoon.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.Details;
import com.appmagazine.nardoon.activities.EditNini;
import com.appmagazine.nardoon.activities.Login;
import com.appmagazine.nardoon.activities.MyNini;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.FileNotFoundException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ir.moslem_deris.apps.zarinpal.PaymentBuilder;
import ir.moslem_deris.apps.zarinpal.ZarinPal;
import ir.moslem_deris.apps.zarinpal.enums.ZarinPalError;
import ir.moslem_deris.apps.zarinpal.listeners.OnPaymentListener;
import ir.moslem_deris.apps.zarinpal.models.Payment;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyNiniAdapter extends RecyclerView.Adapter<MyNiniAdapter.PosterHolder> {

    List<Nini> mDataset;
    Context context;
    public static String id_confirm;
    public static String name;
    public static String age;
    public static String image;
    public static String idNini;
    public static String status;
    public static ProgressDialog dialog;

    private int lastPosition = -1;
    private String peygiri;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView txt;
        ImageView imageNini;
        Button pay;
        Button edit;
        Button delete;


        public PosterHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_name);
            age = (TextView) itemView.findViewById(R.id.txt_age);
            txt = (TextView) itemView.findViewById(R.id.txt);
            imageNini = (ImageView) itemView.findViewById(R.id.img_nini);
            pay = (Button) itemView.findViewById(R.id.btn_pay);
            edit = (Button) itemView.findViewById(R.id.btn_edit);
            delete = (Button) itemView.findViewById(R.id.btn_delete);

        }
    }

    public MyNiniAdapter(Context context, List<Nini> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_nini, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    } 

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {

        try {
            holder.name.setText(mDataset.get(position).name);
            holder.age.setText(mDataset.get(position).age);
            status = mDataset.get(position).validity;

            id_confirm = mDataset.get(position).confirmation_id;
            image = mDataset.get(position).image;
            idNini = mDataset.get(position).id;


            Glide.with(context)
                    .load(App.urlimages + mDataset.get(position).image)
                    .placeholder(R.mipmap.nopic)
                    .into(holder.imageNini);
            String StatusName = null;
            if (status.matches("1")) {
                StatusName = "منتشر شده";
                holder.txt.setTextColor(Color.parseColor("#008542"));
                holder.edit.setVisibility(View.GONE);
                holder.pay.setVisibility(View.GONE);
            }
            if (status.matches("0")) {
                StatusName = "در انتظار تایید ناظر";
                holder.txt.setTextColor(Color.parseColor("#ff9900"));
                holder.pay.setVisibility(View.GONE);
                holder.edit.setVisibility(View.VISIBLE);
            }
            if (status.matches("2")) {
                StatusName = "رد شده - " + mDataset.get(position).comment;
                holder.txt.setTextColor(Color.RED);
                holder.pay.setVisibility(View.GONE);
                holder.edit.setVisibility(View.VISIBLE);
            }
            if (status.matches("3")) {
                StatusName = "درانتظار پرداخت";
                holder.txt.setTextColor(Color.parseColor("#ff9900"));
                holder.edit.setVisibility(View.GONE);
                holder.pay.setVisibility(View.VISIBLE);
            }
            if (status.matches("4")) {
                StatusName = "منقضی شده";
                holder.txt.setTextColor(Color.RED);
                holder.edit.setVisibility(View.VISIBLE);
                holder.edit.setText("ویرایش و تمدید");
                holder.pay.setVisibility(View.GONE);
            }
            if (status.matches("10")) {
                StatusName = "منتشر شده";
                holder.txt.setTextColor(Color.parseColor("#008542"));
                holder.edit.setVisibility(View.GONE);
                holder.pay.setVisibility(View.GONE);
            }
            if (status.matches("20")) {
                StatusName = "منقضی شده(برتر)";
                holder.txt.setTextColor(Color.RED);
                holder.edit.setVisibility(View.GONE);
                holder.pay.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);
            }



            holder.txt.setText("وضعیت آگهی : " + StatusName.toString());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((Activity) v.getContext());

//                // set title
//                alertDialogBuilder.setTitle("-");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("آیا میخواهید این نی نی عکس را حذف کنید؟")
                            .setCancelable(true)
                            .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    webServiceDeleteNini(mDataset.get(position).id.toString() , v);

                                }
                            })
                            .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();

                    TextView msgtv = (TextView) alertDialog.findViewById(android.R.id.message);
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
                    //alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(Font_Far_Koodak);
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);
                    msgtv.setTextSize(19);

                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.context, EditNini.class);
                    name = holder.name.getText().toString();
                    age = mDataset.get(position).age;
                    image = mDataset.get(position).image;
                    idNini = mDataset.get(position).id;
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.context.startActivity(intent);
                    ((Activity)v.getContext()).finish();

                }
            });

            holder.pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = ProgressDialog.show(v.getContext(), null, null, true, false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.progress_layout_small);

                    pay(v, mDataset.get(position).confirmation_id.toString());
                }
            });


            Animation animation = AnimationUtils.loadAnimation(context,
                    (position > lastPosition) ? android.R.anim.slide_in_left
                            : android.R.anim.slide_out_right);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }catch (Exception e){}
    }

    public void update(List<Nini> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }

    private void pay(final View v , final String id ){
        Payment payment = new PaymentBuilder()
                .setMerchantID("f1bd82da-273d-11e7-9b41-005056a205be")  //  This is an example, put your own merchantID here.
                .setAmount(App.priceNini)                                        //  In Toman
                .setDescription("پرداخت به ناردون")
                .setEmail("info@nardoun.ir")                     //  This field is not necessary.
                .setMobile("09175006484")                               //  This field is not necessary.
                .create();
        ZarinPal.pay((Activity) v.getContext(), payment, new OnPaymentListener() {
            @Override
            public void onSuccess(String refID ) {
                dialog.hide();
                webServiceEditNini(id , v);
                Log.wtf("TAG", "::ZarinPal::  RefId: " + refID);
                peygiri  = refID;
             //   holder.pay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(ZarinPalError error) {
                String errorMessage = "";
                dialog.hide();
                switch (error){
                    case INVALID_PAYMENT: errorMessage = "پرداخت تایید نشد";           break;
                    case USER_CANCELED:   errorMessage = "پرداخت توسط کاربر متوقف شد"; break;
                    case NOT_ENOUGH_DATA: errorMessage = "اطلاعات پرداخت کافی نیست";    break;
                    case UNKNOWN:         errorMessage = "خطای ناشناخته";              break;
                }
                Log.wtf("TAG", "::ZarinPal::  ERROR: " + errorMessage);
                //   textView.setText("خطا!!!" + "\n" + errorMessage);
            }
        });
    }


    public  void webServiceBuylog(final String id , final View v)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("confirmation_id",id);
        params.put("type","7");
        params.put("related_id",idNini);
        params.put("description","نی نی عکس");
        params.put("price",App.priceNini);
        params.put("traking_code",peygiri);
        params.put("isused","1");

        client.post(App.urlApi+"buylog", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            //    dialog = ProgressDialog.show(App.context, null, null, true, false);
            //    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //    dialog.setContentView(R.layout.progress_layout_small);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
             //   dialog.hide();
                //  Intent intent = new Intent(App.context , Details.class);
                //  intent.putExtra("id", Details.idAgahi+"");
                // startActivity(intent);
                App.CustomToast("پرداخت شد");
                update(mDataset);
                Intent intent = new Intent(App.context, MyNini.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);
                ((Activity)v.getContext()).finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                //    App.CustomToast("ثبت اطلاعات خرید با مشکل مواجه شد !");

                }else{
                    dialog.hide();
               //     App.CustomToast("fail "+statusCode);
              //      App.CustomToast("اطلاعات خرید ثبت نشد ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public  void webServiceEditNini(final String id , final View v)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(status.equals("4") || status.equals("20")){
            params.put("validity","3");
        }else {

            params.put("validity","0");
        }
        params.put("image", image);

        client.post(App.urlApi+"updatenini/"+ idNini, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                webServiceBuylog(id , v);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                //    dialog.hide();
                    App.CustomToast("مشکلی به وجود آمده است. لطفا دوباره امتحان کنید.");

                }else{
                  //  dialog.hide();
                    App.CustomToast("مشکلی به وجود آمده است. لطفا دوباره امتحان کنید.");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public  void webServiceDeleteNini(final String id , final View v)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.delete(App.urlApi+"nini/"+ id, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {


                App.CustomToast(" نی نی عکس با موفقیت حذف شد. ");

          //  notifyDataSetChanged();
                Intent intent = new Intent(App.context, MyNini.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);
                ((Activity)v.getContext()).finish();



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    //    dialog.hide();
                    App.CustomToast("مشکلی به وجود آمده است. لطفا دوباره امتحان کنید.");

                }else{
                    //  dialog.hide();
                    App.CustomToast("مشکلی به وجود آمده است. لطفا دوباره امتحان کنید.");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}
