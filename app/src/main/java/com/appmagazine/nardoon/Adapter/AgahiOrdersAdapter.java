package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appmagazine.nardoon.AgahiOrder;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.AgahiOrderDetails;
import com.appmagazine.nardoon.activities.PayDetails;

import java.util.List;

/**
 * Created by nadia on 3/2/2017.
 */

public class AgahiOrdersAdapter extends RecyclerView.Adapter<AgahiOrdersAdapter.PosterHolder> {

    List<AgahiOrder> mDataset;
    Context context;

    public static String order;
    public static String time;
    public static String price;
    public static String isdone;
    public static String id;
    public static String agahiMobile;
    public static String mobile;

    private int lastPosition = -1;
    public static String address;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView time;
        TextView isused;
        TextView mobile;
        LinearLayout llRow;


        public PosterHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            time = (TextView) itemView.findViewById(R.id.time);
            isused = (TextView) itemView.findViewById(R.id.isused);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            llRow = (LinearLayout) itemView.findViewById(R.id.ll_row);

        }
    }

    public AgahiOrdersAdapter(Context context, List<AgahiOrder> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agahi_orders, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    } 

    @Override
    public void onBindViewHolder(PosterHolder holder, final int position) {

        holder.id.setText(mDataset.get(position).id);
        holder.time.setText(mDataset.get(position).created_at);
        holder.mobile.setText(mDataset.get(position).confirmation_mobile);
        if(mDataset.get(position).isdone.equals("0")) {
            holder.isused.setText("تحویل داده نشده");
            holder.isused.setTextColor(Color.parseColor("#ff471a"));
        }
        if(mDataset.get(position).isdone.equals("1")) {
            holder.isused.setText("تحویل داده شده");
            holder.isused.setTextColor(Color.parseColor("#008000"));

        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
        holder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.context , AgahiOrderDetails.class);
                order=mDataset.get(position).order.toString();
                time=mDataset.get(position).created_at.toString();
                price=mDataset.get(position).price.toString();
                address=mDataset.get(position).address.toString();
                intent.putExtra("ORDER" ,mDataset.get(position).order.toString() );
                intent.putExtra("PRICE" ,mDataset.get(position).price.toString() );
                intent.putExtra("AGAHIID" ,mDataset.get(position).agahi_id.toString() );
                intent.putExtra("isdone" ,mDataset.get(position).isdone.toString() );
                intent.putExtra("TIME" ,mDataset.get(position).created_at.toString() );
                intent.putExtra("ID" ,mDataset.get(position).id.toString() );
                isdone = mDataset.get(position).isdone;
                id = mDataset.get(position).id;
                agahiMobile = mDataset.get(position).agahi_mobile;
                mobile = mDataset.get(position).confirmation_mobile;

                // intent.putExtra("DONE" ,mDataset.get(position).done.toString() );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.context.startActivity(intent);

            }
        });
    }

    public void update(List<AgahiOrder> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }


}
