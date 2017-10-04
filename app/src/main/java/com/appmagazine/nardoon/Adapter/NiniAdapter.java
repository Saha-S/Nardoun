package com.appmagazine.nardoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.Login;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.MODE_PRIVATE;

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

    String[] liksarray , showerarray, kissarray , flowearray,icearray;
    Boolean isVotePermited=true;
    private String id_confirmaation;
    public  static    Handler h;
    //  String[] disliksarray;

    public static class PosterHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView likes , txtShower , txtKiss , txtFlower , txtIcecream;
        //   TextView dislikes;
        ImageView imageNini;
        ToggleButton like, shower , kiss , flower, icecream;
        //   ToggleButton dislike;
        LinearLayout llName;
        LinearLayout llFrame;
        LinearLayout lShower, lKiss , lFlower , lIcecream;
        FrameLayout frameLayout;


        public PosterHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_name);
            age = (TextView) itemView.findViewById(R.id.txt_age);
            likes = (TextView) itemView.findViewById(R.id.txt_like);
            txtShower = (TextView) itemView.findViewById(R.id.txt_shower);
            txtKiss = (TextView) itemView.findViewById(R.id.txt_kiss);
            txtFlower = (TextView) itemView.findViewById(R.id.txt_flower);
            txtIcecream = (TextView) itemView.findViewById(R.id.txt_icecream);
            imageNini = (ImageView) itemView.findViewById(R.id.img_nini);
            like = (ToggleButton) itemView.findViewById(R.id.iv_like);
            shower = (ToggleButton) itemView.findViewById(R.id.shower);
            kiss = (ToggleButton) itemView.findViewById(R.id.kiss);
            flower = (ToggleButton) itemView.findViewById(R.id.flower);
            icecream = (ToggleButton) itemView.findViewById(R.id.icecream);
            llName = (LinearLayout) itemView.findViewById(R.id.ll_name);
            llFrame = (LinearLayout) itemView.findViewById(R.id.ll_frame);
            lShower = (LinearLayout) itemView.findViewById(R.id.lShower);
            lKiss = (LinearLayout) itemView.findViewById(R.id.lKiss);
            lFlower = (LinearLayout) itemView.findViewById(R.id.lFlower);
            lIcecream = (LinearLayout) itemView.findViewById(R.id.lIcecream);
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


        holder.like.setEnabled(true);

        String validity = filterPoster.get(position).validity;
        if (validity.equals("10")) {
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.llName.setBackgroundColor(Color.parseColor("#ffb300"));
            holder.llFrame.setBackgroundResource(R.drawable.orangeframe);
            holder.like.setEnabled(false);
//            holder.dislike.setEnabled(false);

            holder.name.setText("برنده این ماه " + filterPoster.get(position).name + "   -   ");
            holder.age.setText(filterPoster.get(position).age);
            holder.likes.setText(filterPoster.get(position).point);
            //    holder.dislikes.setText(filterPoster.get(position).pointm);

            Glide.with(context)
                    .load(App.urlimages + filterPoster.get(position).image)
                    .placeholder(R.mipmap.nopic)
                    .into(holder.imageNini);

        } else {
            holder.like.setEnabled(true);

            holder.frameLayout.setVisibility(View.GONE);
            holder.llName.setBackgroundColor(Color.parseColor("#ff6666"));
            holder.llFrame.setBackgroundResource(R.drawable.nini_border);



            if (validity.equals("1")) {
                holder.frameLayout.setVisibility(View.GONE);
                holder.llFrame.setBackgroundResource(R.drawable.nini_border);

                if(!filterPoster.get(position).shower.equals("-1")){
                    holder.lShower.setVisibility(View.VISIBLE);
                    holder.txtShower.setText(filterPoster.get(position).shower);
                }else {
                    holder.lShower.setVisibility(View.GONE);
                }

                if(!filterPoster.get(position).kiss.equals("-1")){
                    holder.lKiss.setVisibility(View.VISIBLE);
                    holder.txtKiss.setText(filterPoster.get(position).kiss);

                }else {
                    holder.lKiss.setVisibility(View.GONE);
                }

                if(!filterPoster.get(position).flower.equals("-1")){
                    holder.lFlower.setVisibility(View.VISIBLE);
                    holder.txtFlower.setText(filterPoster.get(position).flower);

                }else {
                    holder.lFlower.setVisibility(View.GONE);
                }

                if(!filterPoster.get(position).icecream.equals("-1")){
                    holder.lIcecream.setVisibility(View.VISIBLE);
                    holder.txtIcecream.setText(filterPoster.get(position).icecream);

                }else {
                    holder.lIcecream.setVisibility(View.GONE);
                }


                holder.name.setText(filterPoster.get(position).name + "   -   ");
                holder.age.setText(filterPoster.get(position).age);
                holder.likes.setText(filterPoster.get(position).point);
                //     holder.dislikes.setText(filterPoster.get(position).pointm);

                Glide.with(context)
                        .load(App.urlimages + filterPoster.get(position).image)
                        .placeholder(R.mipmap.nopic)
                        .into(holder.imageNini);

/*
                String likestr = file.read("likess");
                liksarray = likestr.split("-");
                */

                String showerstr = file.read("shower");
                showerarray = showerstr.split("-");

                String kissstr = file.read("kiss");
                kissarray = kissstr.split("-");

                String flowerstr = file.read("flower");
                flowearray = flowerstr.split("-");

                String icestr = file.read("icecream");
                icearray = icestr.split("-");


           if(filterPoster.get(position).like.equals("1")){
               holder.like.setChecked(true);
           }
           if(filterPoster.get(position).like.equals("0")){
               holder.like.setChecked(false);
           }

                if (Arrays.asList(showerarray).contains(filterPoster.get(position).id + "")) {
                    holder.shower.setChecked(true);
                } else {
                    holder.shower.setChecked(false);
                }

                if (Arrays.asList(kissarray).contains(filterPoster.get(position).id + "")) {
                    holder.kiss.setChecked(true);
                } else {
                    holder.kiss.setChecked(false);
                }

                if (Arrays.asList(flowearray).contains(filterPoster.get(position).id + "")) {
                    holder.flower.setChecked(true);
                } else {
                    holder.flower.setChecked(false);
                }

                if (Arrays.asList(icearray).contains(filterPoster.get(position).id + "")) {
                    holder.icecream.setChecked(true);
                } else {
                    holder.icecream.setChecked(false);
                }

                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences prefs = App.context.getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
                        SharedPreferences prefs2 = App.context.getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
                        String status = prefs2.getString("islogin", "0");
                        String id_confirmaationSH = prefs.getString("id_confirmaation", "0");

                        if (status.matches("1") && !id_confirmaationSH.equals("0")) {
                            v.setEnabled(false);

                            id_confirmaation = id_confirmaationSH.replace("[{\"id\":", "").replace("}]", "");
                            if (holder.like.isChecked() == false) {
                                webServiceUnLike(filterPoster.get(position).id, holder, position, v);

                            } else {
                                webServiceLike(filterPoster.get(position).id, holder, position, v);
                            }
                        }else{
                            holder.like.toggle();
                            App.CustomToast(" لطفا برای رای گیری وارد شوید ");

                            Intent intent = new Intent(App.context, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.context.startActivity(intent);

                        }

                        }
                });


                holder.shower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.setEnabled(false);

                        if (holder.shower.isChecked() == false) {
                            webServiceUnShower(filterPoster.get(position).id, holder, position, v);
                        } else {
                            webServiceShower(filterPoster.get(position).id, holder, position, v);
                        }

                    }
                });

                holder.kiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.setEnabled(false);

                        if (holder.kiss.isChecked() == false) {
                            webServiceUnKiss(filterPoster.get(position).id, holder, position, v);
                        } else {
                            webServiceKiss(filterPoster.get(position).id, holder, position, v);
                        }

                    }
                });

                holder.flower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.setEnabled(false);

                        if (holder.flower.isChecked() == false) {
                            webServiceUnFlower(filterPoster.get(position).id, holder, position, v);
                        } else {
                            webServiceFlower(filterPoster.get(position).id, holder, position, v);
                        }

                    }
                });

                holder.icecream.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.setEnabled(false);

                        if (holder.icecream.isChecked() == false) {
                            webServiceUnIce(filterPoster.get(position).id, holder, position, v);
                        } else {
                            webServiceIce(filterPoster.get(position).id, holder, position, v);
                        }

                    }
                });

            }

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


    public void webServiceLike(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/"+id+"/like/" + App.confirm_id  , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                if(value.equals("-1")){
                    App.CustomToast(" لطفا مجددا امتحان کنید ");
                }else {
                    holder.likes.setText(value);
                    holder.like.setChecked(true);
                    filterPoster.get(position).point = value;
                    filterPoster.get(position).like = "1";
                }

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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
    public void webServiceUnLike(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/"+id+"/unlike/" + App.confirm_id , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                if(value.equals("-1")){
                    App.CustomToast(" لطفا مجددا امتحان کنید ");

                }else {
                    holder.likes.setText(value);
                    holder.like.setChecked(false);
                    filterPoster.get(position).point = value;
                    filterPoster.get(position).like = "0";
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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

    public void webServiceShower(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/shower" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                String shower = file.read("shower");
                if (shower.equals("")) {
                    file.write("shower", filterPoster.get(position).id);
                } else {
                    file.write("shower", shower + "-" + filterPoster.get(position).id);
                }

                holder.txtShower.setText(value);

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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
    public void webServiceUnShower(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/unshower" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                //  likes = value;
                holder.txtShower.setText(value);
                String likes = "";
                for (int i = 0; i < showerarray.length; i++) {
                    if (!showerarray[i].equals(filterPoster.get(position).id)) {
                        if (likes.equals(""))
                            likes += showerarray[i];
                        else
                            likes += "-" + showerarray[i];

                    }
                }
                file.write("shower", likes);


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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


    public void webServiceKiss(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/kiss" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                String likes = file.read("kiss");
                if (likes.equals("")) {
                    file.write("kiss", filterPoster.get(position).id);
                } else {
                    file.write("kiss", likes + "-" + filterPoster.get(position).id);
                }

                holder.txtKiss.setText(value);

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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
    public void webServiceUnKiss(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/unkiss" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                //  likes = value;
                holder.txtKiss.setText(value);
                String likes = "";
                for (int i = 0; i < kissarray.length; i++) {
                    if (!kissarray[i].equals(filterPoster.get(position).id)) {
                        if (likes.equals(""))
                            likes += kissarray[i];
                        else
                            likes += "-" + kissarray[i];

                    }
                }
                file.write("kiss", likes);


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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

    public void webServiceFlower(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/flower" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                String likes = file.read("flower");
                if (likes.equals("")) {
                    file.write("flower", filterPoster.get(position).id);
                } else {
                    file.write("flower", likes + "-" + filterPoster.get(position).id);
                }

                holder.txtFlower.setText(value);

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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
    public void webServiceUnFlower(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/unflower" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                //  likes = value;
                holder.txtFlower.setText(value);
                String likes = "";
                for (int i = 0; i < flowearray.length; i++) {
                    if (!flowearray[i].equals(filterPoster.get(position).id)) {
                        if (likes.equals(""))
                            likes += flowearray[i];
                        else
                            likes += "-" + flowearray[i];

                    }
                }
                file.write("flower", likes);


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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

    public void webServiceIce(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/ice" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                String likes = file.read("icecream");
                if (likes.equals("")) {
                    file.write("icecream", filterPoster.get(position).id);
                } else {
                    file.write("icecream", likes + "-" + filterPoster.get(position).id);
                }

                holder.txtIcecream.setText(value);

            }



            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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
    public void webServiceUnIce(String id , final PosterHolder holder, final int position, final View v) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(App.urlApi+"nini/" + id + "/unice" , params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                v.setEnabled(true);
                String value = new String(response);
                //  likes = value;
                holder.txtIcecream.setText(value);
                String likes = "";
                for (int i = 0; i < icearray.length; i++) {
                    if (!icearray[i].equals(filterPoster.get(position).id)) {
                        if (likes.equals(""))
                            likes += icearray[i];
                        else
                            likes += "-" + icearray[i];

                    }
                }
                file.write("icecream", likes);


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                v.setEnabled(true);
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


    public  void webServiceLikeNini(int id)
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("nini",id);

        client.post(App.urlApi+"api/niniwithlike/"+id_confirmaation, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //  String value = new String(response);


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("خطا");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                    Log.i("myerror" , errorResponse.toString());
                }
            }
        });
    }



}