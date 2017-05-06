package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.provider.LoadProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.PipedInputStream;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by nadia on 3/2/2017.
 */

public class NiniAdapter extends RecyclerView.Adapter<NiniAdapter.PosterHolder> {

    List<Nini> mDataset;
    Context context;

    private int lastPosition = -1;
    private String likes;
    private FileOperations file;

    String[] liksarray;
    String[] disliksarray;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView likes;
        ImageView imageNini;
        ToggleButton like;
        ToggleButton dislike;


        public PosterHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_name);
            age = (TextView) itemView.findViewById(R.id.txt_age);
            likes = (TextView) itemView.findViewById(R.id.txt_like);
            imageNini = (ImageView) itemView.findViewById(R.id.img_nini);
            like = (ToggleButton) itemView.findViewById(R.id.iv_like);
            dislike = (ToggleButton) itemView.findViewById(R.id.iv_dislike);

        }
    }

    public NiniAdapter(Context context, List<Nini> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
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

        holder.name.setText(mDataset.get(position).name);
        holder.age.setText(mDataset.get(position).age + " ساله");
        holder.likes.setText(mDataset.get(position).point);

        Glide.with(context)
                .load(App.urlimages + mDataset.get(position).image)
                .placeholder(R.mipmap.nopic)
                .into(holder.imageNini);


        String likestr =file.read("likes");
            liksarray=likestr.split("\n-");


        if(Arrays.asList(liksarray).contains(mDataset.get(position).id+"")){
            Toast.makeText(context, "yes yes yesssss", Toast.LENGTH_SHORT).show();
            holder.like.setChecked(true);
            holder.dislike.setEnabled(false);

        }

        String dislikestr =file.read("dislikes");
            disliksarray=dislikestr.split("\n-");

        if(Arrays.asList(disliksarray).contains(mDataset.get(position).id+"")){
            holder.dislike.setChecked(true);
            holder.like.setEnabled(false);

        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.isChecked()== false) {
                    webServiceDislike(mDataset.get(position).id , holder ,position);

                    String likes= "";
                    for (int i=0;i<liksarray.length;i++) {
                        if (!liksarray[i].equals( mDataset.get(position).id )) {
                            if(likes.equals(""))
                            likes+=liksarray[i];
                         else
                            likes+="\n-"+liksarray[i];

                        }
                    }
                    file.write("likes" , likes);
                    holder.dislike.setEnabled(true);

                }else{
                    webServiceLike(mDataset.get(position).id , holder ,position);
                  //  holder.likes.setText(likes);
                    holder.dislike.setEnabled(false);

                    String likes = file.read("likes");
                    if(likes.equals("")) {
                        file.write("likes", mDataset.get(position).id);
                    }else {
                        file.write("likes",likes +"-"+ mDataset.get(position).id);
                    }
                }
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.dislike.isChecked()== false) {
                    webServiceLike(mDataset.get(position).id , holder ,position);
                    String dislikes= "";
                    for (int i=0;i<disliksarray.length;i++) {
                        if (!disliksarray[i].equals( mDataset.get(position).id )) {
                            if(dislikes.equals(""))
                                dislikes+=disliksarray[i];
                            else
                                dislikes+="\n-"+disliksarray[i];

                        }
                    }
                    file.write("dislikes" , dislikes);
                    holder.like.setEnabled(true);
                }else{
                    webServiceDislike(mDataset.get(position).id , holder ,position);
                    holder.like.setEnabled(false);
                    String dislikes = file.read("dislikes");
                    if(dislikes.equals("")) {
                        file.write("dislikes", mDataset.get(position).id);
                    }else {
                        file.write("dislikes",dislikes +"-"+ mDataset.get(position).id);
                    }

                }
            }
        });


        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? android.R.anim.slide_in_left
                        : android.R.anim.slide_out_right);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    public void update(List<Nini> list) {
        mDataset = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { // از توابع خود اداپتر برای دریافت تعداد داده ها می باشد.
        return mDataset.size();
    }


    public void webServiceLike(String id , final PosterHolder holder, final int position) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/like" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                // called before request is started

                // loginpb1.setVisibility(View.VISIBLE);      *************** inja  progressbar faal mishe
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
              //  likes = value;
                holder.likes.setText(value);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if (statusCode == 404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                } else {
                    App.CustomToast("fail " + statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void webServiceDislike(String id, final PosterHolder holder, final int position) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/dislike" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                // called before request is started

                // loginpb1.setVisibility(View.VISIBLE);      *************** inja  progressbar faal mishe
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                String value = new String(response);
               // likes = value;
                holder.likes.setText(value);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                // loginpb1.setVisibility(View.INVISIBLE); *******************   inja progress bar qeyre faal mishe
                if (statusCode == 404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                } else {
                    App.CustomToast("fail " + statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


}