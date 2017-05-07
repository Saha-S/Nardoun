package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.AgahiOrders;
import com.appmagazine.nardoon.activities.Details;
import com.bumptech.glide.Glide;

import java.util.List;

import static android.R.attr.order;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyAgahiAdapter extends RecyclerView.Adapter<MyAgahiAdapter.PosterHolder> {

    List<MyAgahi> mDataset;
    Context context;

    private int lastPosition = -1;
    public static int idAgahi;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView status;
        TextView location;
        TextView orders;
        ImageView image;
        LinearLayout llRow;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            status = (TextView) itemView.findViewById(R.id.status);
            location = (TextView) itemView.findViewById(R.id.location);
            orders = (TextView) itemView.findViewById(R.id.txt_orders);
            image = (ImageView) itemView.findViewById(R.id.img);
            llRow = (LinearLayout) itemView.findViewById(R.id.ll_row);

        }
    }

    public MyAgahiAdapter(Context context, List<MyAgahi> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_agahi, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    } 

    @Override
    public void onBindViewHolder(PosterHolder holder, final int position) {

        holder.title.setText(mDataset.get(position).title);
        String status = mDataset.get(position).validity;
        Log.i("lllll", status);
      //  Log.i("lllll", status+mDataset.get(position).factors.toString());

        try {
            String a = mDataset.get(position).factors.toString();
            Log.i("lllll2", a);
            holder.orders.setVisibility(View.VISIBLE);

        }catch (Exception e){}
        holder.orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , AgahiOrders.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                idAgahi = mDataset.get(position).id;

                App.context.startActivity(intent);
            }
        });

        String StatusName = null;
        if(status.matches("1")){StatusName =  "منتشر شده";holder.status.setBackgroundColor(Color.parseColor("#008542"));}
        if(status.matches("0")){StatusName =  "در انتظار"; holder.status.setBackgroundColor(Color.parseColor("#ff9900"));}
        if(status.matches("2")){StatusName =  "رد شده";holder.status.setBackgroundColor(Color.RED);}
        if(status.matches("3")){StatusName =  "درانتظار پرداخت";holder.status.setBackgroundColor(Color.parseColor("#ff9900"));}
        holder.status.setText("وضعیت آگهی : "+StatusName.toString());
        holder.location.setText(mDataset.get(position).created_at+" در " +mDataset.get(position).location);
        holder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , Details.class);
                intent.putExtra("id", mDataset.get(position).id+"");
                intent.putExtra("title", mDataset.get(position).title);
                intent.putExtra("image", mDataset.get(position).image);
                intent.putExtra("location", mDataset.get(position).location);
                intent.putExtra("price", mDataset.get(position).price);
                intent.putExtra("time", mDataset.get(position).created_at);
                intent.putExtra("comment", mDataset.get(position).comment);
                intent.putExtra("permission", mDataset.get(position).permission);
                intent.putExtra("statusbox", "1");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                App.context.startActivity(intent);

            }
        });

        Glide.with(context)
                .load(App.urlimages+mDataset.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.image);





//        Animation animation = AnimationUtils.loadAnimation(context,
//                (position > lastPosition) ? android.R.anim.slide_in_left
//                        : android.R.anim.slide_out_right);
//        holder.itemView.startAnimation(animation);
//        lastPosition = position;
    }

    public void update(List<MyAgahi> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }


}
