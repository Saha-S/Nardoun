package com.appmagazine.nardoon.Adapter;

import android.content.Context;
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
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

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


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            location = (TextView) itemView.findViewById(R.id.location);
            image = (ImageView) itemView.findViewById(R.id.img);

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
    public void onBindViewHolder(PosterHolder holder, int position) {

        holder.title.setText(filterPoster.get(position).title);
        if(!filterPoster.get(position).price.toString().equals("0")) {
            Log.i("aaaaa2" ,filterPoster.get(position).price.toString() );
            holder.price.setText(filterPoster.get(position).price + " تومان ");
        }
        holder.location.setText(filterPoster.get(position).created_at+" در " +filterPoster.get(position).location);


        Glide.with(context)
                .load(App.urlimages+mDataset.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.image);

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
