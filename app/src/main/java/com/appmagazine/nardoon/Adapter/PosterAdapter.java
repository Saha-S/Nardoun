package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by nadia on 3/2/2017.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterHolder> { // مشتق شده از اداپتر ریسایکلر ویو

    List<Poster> mDataset; // لیست داده هایی از نوع مدل phone که در اداپتر هستند
    Context context; // کانتکست لیست

    private int lastPosition = -1;

    public static class PosterHolder extends RecyclerView.ViewHolder { // تابع viewholder برای تعریف اشاره گر به آیتم های هر سطر
        // به طور مثال در هر سطر یک imageview و یک textview داریم . همان ویو های موجود در فایل item_phone.xml
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

    public PosterAdapter(Context context, List<Poster> myDataset) { // تابع سازنده
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) { // این تابع برای نمایش item_phone اجرا می شود.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poster, parent, false); // توجه کنید در این قسمت اسم item_phone موجود هست. در پروژه های دیگر هم این قسمت نیازمند تغییر هست فقط.
        PosterHolder dataObjectHolder = new PosterHolder(view);
        return dataObjectHolder;

    } 

    @Override
    public void onBindViewHolder(PosterHolder holder, int position) { // تابعی که برای مقدار دهی به ویو های ظاهری فراخوانی میشود.
// در این قسمت holder از نوع PhoneHolder می باشد که در پایین بصورت کلاس داخلی تعریف کرده ایم.
        holder.title.setText(mDataset.get(position).title);  // به ویو title موجود در holder مقدار ست می کنیم.
        holder.price.setText(mDataset.get(position).price+" تومان ");  // به ویو title موجود در holder مقدار ست می کنیم.
        holder.location.setText("در "+mDataset.get(position).location);  // به ویو title موجود در holder مقدار ست می کنیم.

// در صورتی که ویو سطر های شما عکس داشته باشد به کمک کتابخانه پیکاسو به این شکل لود می کنیم.
        Glide.with(context)
                .load("http://nardoun.ir/upload/"+mDataset.get(position).image) // دریافت آدرس از داده ها
                .placeholder(R.mipmap.nopic)
                .into(holder.image); // img از holder





//        Animation animation = AnimationUtils.loadAnimation(context,
//                (position > lastPosition) ? android.R.anim.slide_in_left
//                        : android.R.anim.slide_out_right);
//        holder.itemView.startAnimation(animation);
//        lastPosition = position;
    }

    public void update(List<Poster> list) { // تابعی اضافی که خودمان برای تغییر داده های اداپتر ساخته ایم.
        mDataset = list; // مقدارهای جدید را به اداپتر ست می کند.
        notifyDataSetChanged(); // به اداپتر می گوید داده ها تغییر یافته است.
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }


}
