package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.Details;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nadia on 3/2/2017.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterHolder> {

    static List<Poster> mDataset;
    static List<Poster> filterPoster;
    Context context;

    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView price;
        TextView location;
        ImageView image;
        CardView row;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            location = (TextView) itemView.findViewById(R.id.location);
            image = (ImageView) itemView.findViewById(R.id.img);
            row = (CardView) itemView.findViewById(R.id.ll_row);

        }
    }

    public PosterAdapter(Context context, List<Poster> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
        filterPoster=new ArrayList<>(myDataset);

    }

    public void filter(String searchKeyword){
        searchKeyword=searchKeyword.toLowerCase();
        if (searchKeyword.isEmpty()){
            filterPoster=new ArrayList<>(mDataset);
        }else {
            filterPoster=new ArrayList<>();
            for (int i = 0; i < mDataset.size(); i++) {
                String title =mDataset.get(i).title;

                if (title.toLowerCase().contains(searchKeyword)){
                    filterPoster.add(mDataset.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poster, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;
    } 

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {
try {
    holder.title.setText(filterPoster.get(position).title);

    holder.title.post(new Runnable() {
        @Override
        public void run() {
            int lineCount = holder.title.getLineCount();

            if(lineCount>=2){
                holder.title.setLines(2);
            }
        }
    });

    if (!filterPoster.get(position).price.toString().equals("0")) {
        if(filterPoster.get(position).price.toString().equals("-1")){
            holder.price.setText("توافقی");
        }else {
            String s = filterPoster.get(position).price;
            Locale farsi = new Locale("fa", "IR");
            NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

            String c = numberFormatDutch.format(new BigDecimal(s.toString()));
            String cc = c.replace("ریال", " تومان " + "\u200e");
            holder.price.setText(cc);
        }
    }
    holder.location.setText(filterPoster.get(position).created_at + " در " + filterPoster.get(position).location);


    Glide.with(context)
            .load(App.urlimages + filterPoster.get(position).image)
            .placeholder(R.mipmap.nopic)
            .into(holder.image);
}catch(Exception e){}

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , Details.class);
                intent.putExtra("id", filterPoster.get(position).id+"");
                intent.putExtra("title", filterPoster.get(position).title);
                intent.putExtra("image", filterPoster.get(position).image);
                intent.putExtra("location", filterPoster.get(position).location);
                intent.putExtra("price", filterPoster.get(position).price);
                intent.putExtra("time", filterPoster.get(position).created_at);
                intent.putExtra("statusbox", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);

            }
        });
    }

    public void update(List<Poster> list) {
        filterPoster = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterPoster.size();
    }


}
