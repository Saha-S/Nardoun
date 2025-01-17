package com.appmagazine.nardoon;

import com.appmagazine.nardoon.Adapter.NewsListAdapter;
import com.bumptech.glide.Glide;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Nadia on 1/29/2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> articles;

    private int rowLayout;
    private Context mContext;
    WebView articleView;

    public ArticleAdapter(ArrayList<Article> list, int rowLayout, Context context) {

        this.articles = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }


    @Override
    public long getItemId(int item) {
        // TODO Auto-generated method stub
        return item;
    }

    public void clearData() {
        if (articles != null)
            articles.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Article currentArticle = articles.get(position);

        viewHolder.comment.setVisibility(View.GONE);
        viewHolder.emtiaz.setVisibility(View.GONE);
        Locale.setDefault(Locale.getDefault());
        Date date = currentArticle.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String pubDateString = sdf.format(date);

        viewHolder.title.setText(currentArticle.getTitle());
        viewHolder.content.setText(currentArticle.getDescription());

        //load the image. If the parser did not find an image in the article, it set a placeholder.

        Glide.with(mContext)
                .load(currentArticle.getImage())
                .placeholder(R.mipmap.rss)
                .into(viewHolder.image);

        viewHolder.pubDate.setText(pubDateString);



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.getLink()));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return articles == null ? 0 : articles.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        ImageView image;
        TextView content;

        LinearLayout comment , emtiaz;
        public ViewHolder(View itemView) {

            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            pubDate = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.img);
            content = (TextView) itemView.findViewById(R.id.content);
            comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
            emtiaz = (LinearLayout) itemView.findViewById(R.id.ll_emtiaz);
        }
    }
}
