package com.appmagazine.nardoon;

/**
 * Created by nadia on 4/22/2017.
 */
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DetailsImagePagerAdapter extends PagerAdapter{
    Context context;
    String images[];
    LayoutInflater layoutInflater;

    private int dotsCount;
private ImageView[] dots;
private int[] intdots;



    public DetailsImagePagerAdapter(Context context, String images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

            View itemView = layoutInflater.inflate(R.layout.item_image, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

            Glide.with(App.context)
                    .load(images[position])
                    .placeholder(R.mipmap.nopic)
                    .into(imageView);

        Resources r = App.context.getResources();
        int[] bases = r.getIntArray(R.array.img_id_arr);
        dotsCount = images.length;


        container.addView(itemView);


         return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
