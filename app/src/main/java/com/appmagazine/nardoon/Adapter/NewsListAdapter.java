package com.appmagazine.nardoon.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import com.appmagazine.nardoon.News;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.EditNini;
import com.appmagazine.nardoon.activities.Login;
import com.appmagazine.nardoon.activities.MyNini;
import com.appmagazine.nardoon.activities.NewsDetails;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.moslem_deris.apps.zarinpal.PaymentBuilder;
import ir.moslem_deris.apps.zarinpal.ZarinPal;
import ir.moslem_deris.apps.zarinpal.enums.ZarinPalError;
import ir.moslem_deris.apps.zarinpal.listeners.OnPaymentListener;
import ir.moslem_deris.apps.zarinpal.models.Payment;

/**
 * Created by nadia on 3/2/2017.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.PosterHolder> {

    List<News> mDataset;
    Context context;

    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;
        TextView time;
        TextView point;
        TextView pointm;
        TextView commnetNumber;
        ImageView image;
        CardView item ;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            time = (TextView) itemView.findViewById(R.id.time);
            point = (TextView) itemView.findViewById(R.id.txt_point);
            pointm = (TextView) itemView.findViewById(R.id.txt_pointm);
            commnetNumber = (TextView) itemView.findViewById(R.id.commnet_number);
            image = (ImageView) itemView.findViewById(R.id.img);
            item = (CardView) itemView.findViewById(R.id.ll_row);

        }
    }

    public NewsListAdapter(Context context, List<News> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    } 

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {

        try {
            holder.title.setText(mDataset.get(position).title);
            holder.content.setText(mDataset.get(position).content);
            holder.time.setText(mDataset.get(position).create_at);
            holder.commnetNumber.setText(mDataset.get(position).commentcount);
            holder.point.setText(mDataset.get(position).point);
            holder.pointm.setText(mDataset.get(position).pointm);


            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(App.urlimages + mDataset.get(position).image)
                    .placeholder(R.mipmap.nopic)
                    .into(holder.image);



            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(App.context, NewsDetails.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("CONTENT", mDataset.get(position).content);
                    intent.putExtra("TITLE", mDataset.get(position).title);
                    intent.putExtra("TIME", mDataset.get(position).create_at);
                    intent.putExtra("ID", mDataset.get(position).id);
                    intent.putExtra("IMAGE", App.urlimages + mDataset.get(position).image);

                    App.context.startActivity(intent);

                }
            });
            lastPosition = position;
        }catch (Exception e){}
    }

    public void update(List<News> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }





}
