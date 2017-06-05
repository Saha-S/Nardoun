package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.Poster;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.provider.LoadProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nadia on 3/2/2017.
 */

public class NiniAdapter extends RecyclerView.Adapter<NiniAdapter.PosterHolder> {

    List<Nini> mDataset;
    static List<Nini> filterPoster;

    Context context;

    private int lastPosition = -1;
    private String likes;
    private FileOperations file;

    String[] liksarray;
  //  String[] disliksarray;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView likes;
     //   TextView dislikes;
        ImageView imageNini;
        ToggleButton like;
     //   ToggleButton dislike;
        LinearLayout llName;
        LinearLayout llFrame;
        FrameLayout frameLayout;


        public PosterHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_name);
            age = (TextView) itemView.findViewById(R.id.txt_age);
            likes = (TextView) itemView.findViewById(R.id.txt_like);
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

    public NiniAdapter(Context context, List<Nini> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
        filterPoster=new ArrayList<>(myDataset);

    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nini, parent, false);
        PosterHolder dataObjectHolder = new PosterHolder(view);


        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {

        file = new FileOperations();


        String validity = filterPoster.get(position).validity;
        if (validity.equals("10")){
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.llName.setBackgroundColor(Color.parseColor("#ffb300"));
            holder.llFrame.setBackgroundResource(R.drawable.orangeframe);
            holder.like.setEnabled(false);
//            holder.dislike.setEnabled(false);

            holder.name.setText("برنده این ماه "+filterPoster.get(position).name + "   -   ");
            holder.age.setText(filterPoster.get(position).age);
            holder.likes.setText(filterPoster.get(position).point);
        //    holder.dislikes.setText(filterPoster.get(position).pointm);

            Glide.with(context)
                    .load(App.urlimages + filterPoster.get(position).image)
                    .placeholder(R.mipmap.nopic)
                    .into(holder.imageNini);

        }
        if(validity.equals("1")) {
            holder.frameLayout.setVisibility(View.GONE);

            holder.name.setText(filterPoster.get(position).name + "   -   ");
            holder.age.setText(filterPoster.get(position).age);
            holder.likes.setText(filterPoster.get(position).point);
       //     holder.dislikes.setText(filterPoster.get(position).pointm);

            Glide.with(context)
                    .load(App.urlimages + filterPoster.get(position).image)
                    .placeholder(R.mipmap.nopic)
                    .into(holder.imageNini);


            String likestr = file.read("likes");
            liksarray = likestr.split("-");


            if (Arrays.asList(liksarray).contains(filterPoster.get(position).id + "")) {
                holder.like.setChecked(true);
        //        holder.dislike.setEnabled(false);
            } else {
                holder.like.setChecked(false);
        //        holder.dislike.setEnabled(true);
            }

            String dislikestr = file.read("dislikes");
        //    disliksarray = dislikestr.split("-");

     /*       if (Arrays.asList(disliksarray).contains(filterPoster.get(position).id + "")) {
                holder.dislike.setChecked(true);
                holder.like.setEnabled(false);
            } else {
                holder.dislike.setChecked(false);
                holder.like.setEnabled(true);
            }
*/
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.like.isChecked() == false) {
                        webServiceUnLike(filterPoster.get(position).id, holder, position);

                        String likes = "";
                        for (int i = 0; i < liksarray.length; i++) {
                            if (!liksarray[i].equals(filterPoster.get(position).id)) {
                                if (likes.equals(""))
                                    likes += liksarray[i];
                                else
                                    likes += "-" + liksarray[i];

                            }
                        }
                        file.write("likes", likes);
                     //   holder.dislike.setEnabled(true);

                    } else {
                        webServiceLike(filterPoster.get(position).id, holder, position);
                        //  holder.likes.setText(likes);
                   //     holder.dislike.setEnabled(false);

                        String likes = file.read("likes");
                        if (likes.equals("")) {
                            file.write("likes", filterPoster.get(position).id);
                        } else {
                            file.write("likes", likes + "-" + filterPoster.get(position).id);
                        }
                    }
                }
            });

         /*   holder.dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.dislike.isChecked() == false) {
                        webServiceUnDislike(filterPoster.get(position).id, holder, position);
                        String dislikes = "";
                        for (int i = 0; i < disliksarray.length; i++) {
                            if (!disliksarray[i].equals(filterPoster.get(position).id)) {
                                if (dislikes.equals(""))
                                    dislikes += disliksarray[i];
                                else
                                    dislikes += "-" + disliksarray[i];

                            }
                        }
                        file.write("dislikes", dislikes);
                        holder.like.setEnabled(true);
                    } else {
                        webServiceDislike(filterPoster.get(position).id, holder, position);
                        holder.like.setEnabled(false);
                        String dislikes = file.read("dislikes");
                        if (dislikes.equals("")) {
                            file.write("dislikes", filterPoster.get(position).id);
                        } else {
                            file.write("dislikes", dislikes + "-" + filterPoster.get(position).id);
                        }

                    }
                }
            });
*/

            Animation animation = AnimationUtils.loadAnimation(context,
                    (position > lastPosition) ? android.R.anim.slide_in_left
                            : android.R.anim.slide_out_right);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void update(List<Nini> list) {
        filterPoster = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return filterPoster.size();
    }


    public void webServiceLike(String id , final PosterHolder holder, final int position) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/like" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
                holder.likes.setText(value);

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (statusCode == 403)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast(" در حال حاضر امکان رای دادن وجود ندارد. ");

                } else {
                    App.CustomToast(" لطفا مجددا امتحان کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    ///////////////////////////////////// unLIKE //////////////////////////////
    public void webServiceUnLike(String id , final PosterHolder holder, final int position) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/unlike" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
                //  likes = value;
                holder.likes.setText(value);

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (statusCode == 403)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast(" در حال حاضر امکان رای دادن وجود ندارد. ");

                } else {
                    App.CustomToast(" لطفا مجددا امتحان کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }

    ///////////////////////////////////// unLIKE //////////////////////////////

  /*  public void webServiceDislike(String id, final PosterHolder holder, final int position) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/dislike" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
                holder.dislikes.setText(value);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (statusCode == 403)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast(" در حال حاضر امکان رای دادن وجود ندارد. ");

                } else {
                    App.CustomToast(" لطفا مجددا امتحان کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
*/
    ///////////////////////////////////////////// UNDISLIKE /////////////////////////////////
  /*  public void webServiceUnDislike(String id, final PosterHolder holder, final int position) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/undislike" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
                holder.dislikes.setText(value);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (statusCode == 403)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast(" در حال حاضر امکان رای دادن وجود ندارد. ");

                } else {
                    App.CustomToast(" لطفا مجددا امتحان کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
*/
    ///////////////////////////////////////////// UNDISLIKE /////////////////////////////////


}