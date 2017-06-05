package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nadia on 3/2/2017.
 */

public class WinnerNiniAdapter extends RecyclerView.Adapter<WinnerNiniAdapter.PosterHolder> {

    List<Nini> mDataset;
    static List<Nini> filterPoster;

    Context context;

    private int lastPosition = -1;
    private String likes;
    private FileOperations file;

    String[] liksarray;
   // String[] disliksarray;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView likes;
      //  TextView dislikes;
        TextView txtTime;
        ImageView imageNini;
        ToggleButton like;
//        ToggleButton dislike;
        LinearLayout llName;
        LinearLayout llFrame;
        FrameLayout frameLayout;


        public PosterHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_name);
            age = (TextView) itemView.findViewById(R.id.txt_age);
            likes = (TextView) itemView.findViewById(R.id.txt_like);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            imageNini = (ImageView) itemView.findViewById(R.id.img_nini);
            like = (ToggleButton) itemView.findViewById(R.id.iv_like);
            llName = (LinearLayout) itemView.findViewById(R.id.ll_name);
            llFrame = (LinearLayout) itemView.findViewById(R.id.ll_frame);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_layout);

        }
    }

    public void filter(String searchKeyword){
        searchKeyword=searchKeyword.toLowerCase();
        if (searchKeyword.isEmpty()){
            filterPoster=new ArrayList<>(mDataset);
        }else {
            filterPoster=new ArrayList<>();
            for (int i = 0; i < mDataset.size(); i++) {
                String title =mDataset.get(i).name;

                if (title.toLowerCase().contains(searchKeyword)){
                    filterPoster.add(mDataset.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public WinnerNiniAdapter(Context context, List<Nini> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
        filterPoster=new ArrayList<>(myDataset);

    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nini_bartar, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);


        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {


        String validity = filterPoster.get(position).validity;
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.llName.setBackgroundColor(Color.parseColor("#ffb300"));
            holder.llFrame.setBackgroundResource(R.drawable.orangeframe);
            holder.like.setEnabled(false);
       //     holder.dislike.setEnabled(false);

            holder.name.setText(filterPoster.get(position).name + "   -   ");
            holder.age.setText(filterPoster.get(position).age);
            holder.likes.setText(filterPoster.get(position).point);
        //    holder.dislikes.setText(filterPoster.get(position).pointm);
            holder.txtTime.setText(filterPoster.get(position).created_at);

            Glide.with(context)
                    .load(App.urlimages + filterPoster.get(position).image)
                    .placeholder(R.mipmap.nopic)
                    .into(holder.imageNini);




            Animation animation = AnimationUtils.loadAnimation(context,
                    (position > lastPosition) ? android.R.anim.slide_in_left
                            : android.R.anim.slide_out_right);
            holder.itemView.startAnimation(animation);
            lastPosition = position;

    }

    public void update(List<Nini> list) {
        filterPoster = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterPoster.size();
    }

}