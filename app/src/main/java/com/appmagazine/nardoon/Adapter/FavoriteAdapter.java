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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.EditNini;
import com.appmagazine.nardoon.activities.Favorite;
import com.appmagazine.nardoon.activities.MyNini;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Field;
import java.util.Arrays;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.PosterHolder> {

    List<Poster> mDataset;
    Context context;
    String[] favoritearray;
    private int lastPosition = -1;
    private int numbers = Favorite.numbers;


    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        ImageView image;
        LinearLayout remove;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.img);
            remove = (LinearLayout) itemView.findViewById(R.id.ll_remove);

        }
    }

    public FavoriteAdapter(Context context, List<Poster> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public FavoriteAdapter.PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        FavoriteAdapter.PosterHolder dataObjectHolder = new FavoriteAdapter.PosterHolder(view);
        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.PosterHolder holder, final int position) {

        FileOperations file = new FileOperations();
        String favoritestr =file.read("favorite");
        favoritearray = favoritestr.split("-");

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String likes= "";
                for (int i=0;i<favoritearray.length;i++) {
                    if (!favoritearray[i].equals( mDataset.get(position).id )) {
                        if(likes.equals(""))
                            likes+=favoritearray[i];
                        else
                            likes+="-"+favoritearray[i];

                    }

                }
                update(mDataset);

            }
        });

        holder.title.setText(mDataset.get(position).title);

        Log.i("aaaaa2", mDataset.get(position).price.toString());
        holder.time.setText(mDataset.get(position).created_at);



        Glide.with(context)
                .load(App.urlimages + mDataset.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.image);




    /*    Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        */
    }

    public void update(List<Poster> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
            return mDataset.size();

    }


}
