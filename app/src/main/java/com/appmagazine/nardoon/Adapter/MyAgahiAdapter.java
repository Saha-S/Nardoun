package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmagazine.nardoon.MyAgahi;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyAgahiAdapter extends RecyclerView.Adapter<MyAgahiAdapter.PosterHolder> {

    List<MyAgahi> mDataset;
    Context context;

    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView status;
        TextView location;
        ImageView image;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            status = (TextView) itemView.findViewById(R.id.status);
            location = (TextView) itemView.findViewById(R.id.location);
            image = (ImageView) itemView.findViewById(R.id.img);

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
    public void onBindViewHolder(PosterHolder holder, int position) {

        holder.title.setText(mDataset.get(position).title);
        String status = mDataset.get(position).validity;
        Log.i("lllll", status);
        String StatusName = null;
        if(status.matches("1")){StatusName =  "منتشر شده";holder.status.setTextColor(Color.parseColor("#008542"));}
        if(status.matches("0")){StatusName =  "در انتظار"; holder.status.setTextColor(Color.parseColor("#ff9900"));}
        if(status.matches("2")){StatusName =  "رد شده";holder.status.setTextColor(Color.RED);}
        holder.status.setText("وضعیت آگهی : "+StatusName.toString());
        holder.location.setText(mDataset.get(position).created_at+" در " +mDataset.get(position).location);


        Glide.with(context)
                .load("http://nardoun.ir/upload/"+mDataset.get(position).image)
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
