package com.appmagazine.nardoon.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appmagazine.nardoon.R;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsDetails extends AppCompatActivity {
    Integer[] imageIDs = {
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5
    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);
        LinearLayout llErsal = (LinearLayout) findViewById(R.id.ll_ersal);

        tvBack.setText("جزئیات خبر");

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Gallery gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public HashMap<String, Integer> url_maps;

            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(getBaseContext(),"pic" + (position + 1) + " selected",
                        Toast.LENGTH_SHORT).show();
                // display the images selected
            //    ImageView imageView = (ImageView) findViewById(R.id.image1);
              //  imageView.setImageResource(imageIDs[position]);

                final Dialog dialog = new Dialog(NewsDetails.this,android.R.style.Theme_Black_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_pic);



                SliderLayout sliderShow = (SliderLayout) dialog.findViewById(R.id.slider);
                sliderShow.setCustomIndicator((PagerIndicator) dialog.findViewById(R.id.custom_indicator));
                // sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);

                sliderShow.stopAutoCycle();


                url_maps = new HashMap<String, Integer>();

              //  HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
                url_maps.put("1",R.drawable.p1);
                url_maps.put("2",R.drawable.p2);
                url_maps.put("3",R.drawable.p3);
                url_maps.put("4", R.drawable.p4);
                url_maps.put("5", R.drawable.p5);



                DefaultSliderView SliderView = new DefaultSliderView(NewsDetails.this);
                 /*   if(url1.equals("0") ){
                        SliderView
                                .image(R.drawable.nopic);
                    }
*/
                   // int images[] = {R.drawable.nopic};
                    sliderShow.setPagerTransformer(false, new BaseTransformer() {

                        @Override
                        public void onTransform(View view, float position) {
                        }

                    });
                    for(int i = 0 ; i<(imageIDs.length-1) ; i++){

                        SliderView
                                .image(imageIDs[i]);

                    }
                 SliderView.image(imageIDs[position]);


                sliderShow.addSlider(SliderView);

               /* for (String name : url_maps.keySet()) {


                    DefaultSliderView textSliderView = new DefaultSliderView(NewsDetails.this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    sliderShow.addSlider(textSliderView);
                }
                */
                dialog.show();



                }
        });




    }


    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return imageIDs.length;
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(imageIDs[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }

}
