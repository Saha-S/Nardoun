package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.Cat;
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

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.PosterHolder> {

    List<Cat> mDataset;
    static List<Cat> filterCat;
    Context context;
    String special;
    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView price;
        TextView location;
        ImageView image;
        LinearLayout ll_vizhe;
        FrameLayout ll_border;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            location = (TextView) itemView.findViewById(R.id.location);
            image = (ImageView) itemView.findViewById(R.id.img);
            ll_vizhe = (LinearLayout) itemView.findViewById(R.id.ll_vizhe);
            ll_border = (FrameLayout) itemView.findViewById(R.id.ll_border);

        }
    }

    public CatAdapter(Context context, List<Cat> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
        filterCat =new ArrayList<>(myDataset);

    }

    public void filter(String searchKeyword){
        searchKeyword=searchKeyword.toLowerCase();
        if (searchKeyword.isEmpty()){
            filterCat =new ArrayList<>(mDataset);
        }else {
            filterCat =new ArrayList<>();
            for (int i = 0; i < mDataset.size(); i++) {
                String title =mDataset.get(i).title;

                if (title.toLowerCase().contains(searchKeyword)){
                    filterCat.add(mDataset.get(i));
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
    public void onBindViewHolder(PosterHolder holder, final int position) {

        holder.title.setText(filterCat.get(position).title);
        if(!mDataset.get(position).price.toString().equals("0")) {
            String s =filterCat.get(position).price;
            Locale farsi = new Locale("fa", "IR");
            NumberFormat numberFormatDutch = NumberFormat.getCurrencyInstance(farsi);

            String c = numberFormatDutch.format(new BigDecimal(s.toString()));
            String cc = c.replace("ریال",   " تومان " + "\u200e") ;
            holder.price.setText(  cc );
        }
        holder.location.setText(filterCat.get(position).created_at+" در " + filterCat.get(position).location);

        special= filterCat.get(position).special;
        if(special.equals("1")){
            holder.ll_vizhe.setVisibility(View.VISIBLE);
            holder.ll_border.setVisibility(View.VISIBLE);
        }else{
            holder.ll_vizhe.setVisibility(View.GONE);
            holder.ll_border.setVisibility(View.GONE);
        }



        Glide.with(context)
                .load(App.urlimages+ filterCat.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context, Details.class);
                intent.putExtra("id", filterCat.get(position).id + "");
                intent.putExtra("title", filterCat.get(position).title);
                intent.putExtra("image", filterCat.get(position).image);
                intent.putExtra("location", filterCat.get(position).location);
                intent.putExtra("price", filterCat.get(position).price);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);

            }
        });





    /*    Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        */
    }

    public void update(List<Cat> list) {
        filterCat = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterCat.size();
    }


}
