package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.MyPay;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.PayDetails;

import java.util.List;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyPanelAdapter extends RecyclerView.Adapter<MyPanelAdapter.PosterHolder> {

    List<MyPay> mDataset;
    Context context;


    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView number;
        TextView isused;
        TextView peygiri;
        LinearLayout llRow;


        public PosterHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            number = (TextView) itemView.findViewById(R.id.number);
            isused = (TextView) itemView.findViewById(R.id.isused);
            llRow = (LinearLayout) itemView.findViewById(R.id.ll_row);

        }
    }

    public MyPanelAdapter(Context context, List<MyPay> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_panel, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    } 

    @Override
    public void onBindViewHolder(PosterHolder holder, final int position) {

        holder.id.setText(mDataset.get(position).id);
        holder.number.setText(mDataset.get(position).credit +" عدد");
        if(mDataset.get(position).isused.equals("0")) {
            holder.isused.setText("استفاده نشده");
            holder.isused.setBackgroundColor(Color.parseColor("#ff471a"));
        }
        if(mDataset.get(position).isused.equals("1")) {
            holder.isused.setText("استفاده شده");
            holder.isused.setBackgroundColor(Color.parseColor("#008000"));

        }


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

        holder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , PayDetails.class);
                intent.putExtra("TYPE" ,mDataset.get(position).type.toString() );
                intent.putExtra("CREDIT" ,mDataset.get(position).credit.toString() );
                intent.putExtra("DESC" ,mDataset.get(position).description.toString() );
                intent.putExtra("PEYGIRI" ,mDataset.get(position).traking_code.toString() );
                intent.putExtra("PRICE" ,mDataset.get(position).price.toString() );
                intent.putExtra("TIME" ,mDataset.get(position).created_at.toString() );
                intent.putExtra("ID" ,mDataset.get(position).id.toString() );
                intent.putExtra("ISUSED" ,mDataset.get(position).isused.toString() );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);

            }
        });
    }

    public void update(List<MyPay> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }


}
