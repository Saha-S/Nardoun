package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.Cat;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by nadia on 3/2/2017.
 */

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.PosterHolder> {

    List<Cat> mDataset;
    Context context;
    String special;
    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView price;
        TextView location;
        ImageView image;
        LinearLayout ll_vizhe;


        public PosterHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            price = (TextView) itemView.findViewById(R.id.price);
            location = (TextView) itemView.findViewById(R.id.location);
            image = (ImageView) itemView.findViewById(R.id.img);
            ll_vizhe = (LinearLayout) itemView.findViewById(R.id.ll_vizhe);

        }
    }

    public CatAdapter(Context context, List<Cat> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
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

        holder.title.setText(mDataset.get(position).title);
        if(!mDataset.get(position).price.toString().equals("0")) {
            Log.i("aaaaa2" ,mDataset.get(position).price.toString() );
            holder.price.setText(mDataset.get(position).price + " تومان ");
        }
        holder.location.setText(mDataset.get(position).created_at+" در " +mDataset.get(position).location);

        special=mDataset.get(position).special;
        if(special.equals("1")){
            holder.ll_vizhe.setVisibility(View.VISIBLE);
        }else{
            holder.ll_vizhe.setVisibility(View.GONE);
        }



        Glide.with(context)
                .load(App.urlimages+mDataset.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.image);





    /*    Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        */
    }

    public void update(List<Cat> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }


}
