package com.appmagazine.nardoon.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

        holder.name.setText(mDataset.get(position).name);
        holder.age.setText(mDataset.get(position).age + " ساله");
        status = mDataset.get(position).validity;
        id_confirm = mDataset.get(position).confirmation_id;

        Glide.with(context)
                .load(App.urlimages + mDataset.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.imageNini);
        String StatusName = null;

        if(status.matches("1")){StatusName =  "منتشر شده";holder.txt.setTextColor(Color.parseColor("#008542")); holder.edit.setVisibility(View.GONE); holder.pay.setVisibility(View.GONE);}
        if(status.matches("0")){StatusName =  "در انتظار تایید ناظر"; holder.txt.setTextColor(Color.parseColor("#ff9900")); holder.pay.setVisibility(View.GONE);}
        if(status.matches("2")){StatusName =  "رد شده";holder.txt.setTextColor(Color.RED);holder.pay.setVisibility(View.GONE);}
        if(status.matches("3")){StatusName =  "درانتظار پرداخت";holder.txt.setTextColor(Color.parseColor("#ff9900"));}
        holder.txt.setText("وضعیت آگهی : "+StatusName.toString());


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
            }
        });

        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    public void update(List<Nini> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }

    private void pay(){
        Payment payment = new PaymentBuilder()
                .setMerchantID("f1bd82da-273d-11e7-9b41-005056a205be")  //  This is an example, put your own merchantID here.
                .setAmount(App.priceNini)                                        //  In Toman
                .setDescription("پرداخت تست پلاگین اندروید")
                .setEmail("moslem.deris@gmail.com")                     //  This field is not necessary.
                .setMobile("09123456789")                               //  This field is not necessary.
                .create();

       // dialog = ProgressDialog.show(App.context, null, null, true, false);
      //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      //  dialog.setContentView(R.layout.progress_layout_small);

        ZarinPal.pay((Activity) App.context, payment, new OnPaymentListener() {
            @Override
            public void onSuccess(String refID ) {
                dialog.hide();
                webServiceEditNini();
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


    public  void webServiceBuylog()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("confirmation_id",id_confirm);
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

                dialog.hide();
                //  Intent intent = new Intent(App.context , Details.class);
                //  intent.putExtra("id", Details.idAgahi+"");
                // startActivity(intent);
                App.CustomToast("اطلاعات خرید ثبت شد");

                //  finish();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast("ثبت اطلاعات خرید با مشکل مواجه شد !");

                }else{
                    dialog.hide();
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast("اطلاعات خرید ثبت نشد ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public  void webServiceEditNini()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("validity","0");
        params.put("image", image);



        client.post(App.urlApi+"updatenini/"+ idNini, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                dialog.hide();
                webServiceBuylog();
                //  Intent intent = new Intent(App.context , Details.class);
                //  intent.putExtra("id", Details.idAgahi+"");
                // startActivity(intent);
                App.CustomToast("آگهی ویرایش شد !");

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    dialog.hide();
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    dialog.hide();
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

}
