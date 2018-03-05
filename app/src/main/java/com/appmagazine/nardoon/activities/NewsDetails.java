package com.appmagazine.nardoon.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.ImageLoader;
import com.appmagazine.nardoon.Nini;
import com.appmagazine.nardoon.R;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewsDetails extends AppCompatActivity {
    String pid ;
    private ImageLoader imageLoader;
    private ArrayList<String> strings;
    private EditText cName,cContent;
    private ListView listView;
    ArrayList<HashMap<String, String>> listComment;
    private SimpleAdapter simpleAdapter;
    private View addView;
    private LinearLayout list;
    private HashMap<String, String> b;
    private TextView txtName ,txtComment , noComment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String status;
    private String id_confirmaationSH;
    private String id_confirmaation;
    LinearLayout ll;
    LinearLayout like , unlike;
    TextView emtiaz , txtLike , txtUnlike;
    private String likes;
    private FileOperations file;

    String[] liksarray;
    String[] disliksarray;
    private LinearLayout llErsal;
    private TextView content;
    private TextView title;

    String img1 = "", img2= "", img3= "", img4= "",  img5= "", img6= "", img7= "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        listComment = new ArrayList<HashMap<String, String>>();


        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        LinearLayout llSendComment = (LinearLayout) findViewById(R.id.ll_send_comment);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvBack = (TextView) findViewById(R.id.tv_back);
        llErsal = (LinearLayout) findViewById(R.id.ll_ersal);
        TextView txt = (TextView) findViewById(R.id.txt_ersal);
        ImageView imgErsal = (ImageView) findViewById(R.id.img_ersal);
        ll = (LinearLayout) findViewById(R.id.ll);

        Bundle extrasV = getIntent().getExtras();
        if(extrasV != null) {

             if(extrasV.getString("VIRAYESH").equals("1")){
                 llErsal.setVisibility(View.VISIBLE);
                 imgErsal.setVisibility(View.GONE);
                 txt.setText("ویرایش");
             }else{
                 llErsal.setVisibility(View.GONE);
             }
        }



        like = (LinearLayout) findViewById(R.id.like);
        unlike = (LinearLayout) findViewById(R.id.unlike);
        emtiaz = (TextView) findViewById(R.id.txt_emtiaz);
        txtLike = (TextView) findViewById(R.id.txt_like);
        txtUnlike = (TextView) findViewById(R.id.txt_unlike);

        TextView time = (TextView) findViewById(R.id.time);
        TextView id = (TextView) findViewById(R.id.id);
        title = (TextView) findViewById(R.id.txt_title);
        content = (TextView) findViewById(R.id.content);
        noComment = (TextView) findViewById(R.id.no_commnet);
        ImageView img = (ImageView) findViewById(R.id.img);

        list = (LinearLayout) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        status = prefs2.getString("islogin", "0");
        id_confirmaationSH = prefs.getString("id_confirmaation", "0");

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webServicelike();
            }
        });
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webServiceunlike();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected() || isMobileDataEnabled()) {
                        webServiceGetNewsDetails();
                        swipeRefreshLayout.setRefreshing(true);

                    }else {
                        App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                        swipeRefreshLayout.setRefreshing(false);
                    }

            }
        });



//        if(addView.getParent()!=null)
//            ((ViewGroup)addView.getParent()).removeView(addView); // <- fix
//        list.addView(addView); //  <==========  ERROR IN THIS LINE DURING 2ND RUN


//        listView.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            time.setText(extras.getString("TIME"));
            title.setText(extras.getString("TITLE"));
            id.setText("کد خبر: "+extras.getString("ID"));
            content.setText(extras.getString("CONTENT"));

            pid= extras.getString("ID").toString();
            Glide.with(App.context)
                    .load(extras.getString("IMAGE"))
                    .placeholder(R.mipmap.nopic)
                    .into(img);
        }
            ConnectivityManager connManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected() || isMobileDataEnabled()) {
                webServiceGetNewsDetails();
                swipeRefreshLayout.setRefreshing(true);

            }else {
                App.CustomToast("خطا: ارتباط اینترنت را چک نمایید");
                swipeRefreshLayout.setRefreshing(false);
            }

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

            llSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (status.matches("1") && !id_confirmaationSH.equals("0")) {
                        id_confirmaation=id_confirmaationSH.replace("[{\"id\":", "").replace("}]" , "");

                        final Dialog dialog = new Dialog(NewsDetails.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_comment);

                    // set the custom dialog components - text, image and button
                    cName = (EditText) dialog.findViewById(R.id.c_name);
                    cContent = (EditText) dialog.findViewById(R.id.c_content);
                    Button cSend = (Button) dialog.findViewById(R.id.c_send);

                    // if button is clicked, close the custom dialog
                    cSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cName.getText().toString().equals("")) {
                                cName.setError("نام خود را وارد کنید");
                            }
                            if (cContent.getText().toString().equals("")) {
                                cContent.setError("نظر خود را وارد کنید");
                            }
                            if (!cContent.getText().toString().equals("") && !cName.getText().toString().equals("")) {
                                webServiceNewComment();
                                dialog.dismiss();

                            }
                        }
                    });

                    dialog.show();
                    }else {
                        Intent intent = new Intent(App.context, Login.class);
                        startActivity(intent);
                    }

                }
            });



    }


    public class AddImgAdp extends BaseAdapter {
        int GalItemBg;
        private Context cont;
        ArrayList<String> strings;
        private  int itemBackground;
        public AddImgAdp(Context c,ArrayList<String> strings) {
            cont = c;
            this.strings=strings;
            TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();

        }

        public int getCount() {
            return strings.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imgView = new ImageView(cont);

            imgView.setLayoutParams(new Gallery.LayoutParams(250, 250));
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imgView.setBackgroundResource(itemBackground);
            imageLoader.DisplayImage(strings.get(position), imgView);
            return imgView;
        }
    }


    public void webServiceGetNewsDetails()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(App.urlApi+"news/"+pid, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
                swipeRefreshLayout.setRefreshing(false);

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                JSONObject obj = null;  //********* chon ye json array ba 1 json objecte injur migirimesh

                isLike();
                try {

                    obj = new JSONArray(value).getJSONObject(0);
                    strings = new ArrayList<String>();

                    txtLike.setText(obj.getString("point"));
                    txtUnlike.setText(  obj.getString("pointm") );

                    strings.clear();
                    if(!obj.getString("imagei").equals("0")){
                        strings.add(App.urlimages+obj.getString("imagei").toString());
                        img1=App.urlimages+obj.getString("imagei").toString();

                    }
                    if(!obj.getString("imageii").equals("0")){
                        strings.add(App.urlimages+obj.getString("imageii").toString());
                        img2=App.urlimages+obj.getString("imageii").toString();


                    }
                    if(!obj.getString("imageiii").equals("0")){
                        strings.add(App.urlimages+obj.getString("imageiii").toString());
                        img3=App.urlimages+obj.getString("imageiii").toString();

                    }
                    if(!obj.getString("imageiv").equals("0")){
                        strings.add(App.urlimages+obj.getString("imageiv").toString());
                        img4=App.urlimages+obj.getString("imageiv").toString();

                    }
                    if(!obj.getString("imagev").equals("0")){
                        strings.add(App.urlimages+obj.getString("imagev").toString());
                        img5=App.urlimages+obj.getString("imagev").toString();

                    }
                    if(!obj.getString("imagevi").equals("0")){
                        strings.add(App.urlimages+obj.getString("imagevi").toString());
                        img6=App.urlimages+obj.getString("imagevi").toString();

                    }
                    if(!obj.getString("imagevii").equals("0")){
                        strings.add(App.urlimages+obj.getString("imagevii").toString());
                        img7=App.urlimages+obj.getString("imagevii").toString();

                    }

                    list.removeAllViews();

                    JSONArray comments = obj.getJSONArray("comments");
                    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

                    if(comments.length()==0){
                        noComment.setVisibility(View.VISIBLE);
                    }else{

                    for (int i = 0; i < comments.length(); i++) {
                        noComment.setVisibility(View.GONE);

                        b = new HashMap<String, String>();
                        String name = comments.getJSONObject(i).getString("name");
                        String comment = comments.getJSONObject(i).getString("content");
                        b.put("name" , name);
                        b.put("comment" , comment);
                        arrayList.add(b);
                        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(NewsDetails.this.LAYOUT_INFLATER_SERVICE);
                        addView = layoutInflater.inflate(R.layout.item_comment, null);
                        txtName = (TextView)addView.findViewById(R.id.c_name);
                        txtComment = (TextView)addView.findViewById(R.id.c_content);

                        txtName.setText("نام: "+name);
                        txtComment.setText(comment);
                        list.addView(addView);


                    }
                    }

                    final Bundle extras = getIntent().getExtras();
                    final JSONObject finalObj = obj;
                    llErsal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewsDetails.this, EditNews.class);

                            intent.putExtra("TITLE" ,extras.getString("TITLE") );
                            intent.putExtra("ID" ,extras.getString("ID") );
                            intent.putExtra("CONTENT" ,extras.getString("CONTENT") );
                            intent.putExtra("IMAGE" ,extras.getString("IMAGE") );
                            intent.putExtra("TYPE" ,extras.getString("TYPE") );
                            intent.putExtra("IMG1" , img1 );
                            intent.putExtra("IMG2" , img2 );
                            intent.putExtra("IMG3" , img3 );
                            intent.putExtra("IMG4" , img4 );
                            intent.putExtra("IMG5" , img5 );
                            intent.putExtra("IMG6" , img6 );
                            intent.putExtra("IMG7" , img7 );
                            startActivity(intent);
                        }
                    });

//                    int[] to = { R.id.c_name,R.id.c_content};
//                    String[] from={"name","comment"};//string array
//                    simpleAdapter=new SimpleAdapter(NewsDetails.this,arrayList,R.layout.item_comment,from,to);//Create object and set the parameters for simpleAdapter
//                    listView.setAdapter(simpleAdapter);//sets the adapter for listView
//                    for(int i = 0 ; i<b.size() ; i++){
//                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageLoader=new ImageLoader(NewsDetails.this);

                Gallery gallery = (Gallery) findViewById(R.id.gallery1);
                try {
                    gallery.setAdapter(new AddImgAdp(NewsDetails.this , strings));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public HashMap<String, String> url_maps;

                    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                    {
//                        Toast.makeText(getBaseContext(),"pic" + (position + 1) + " selected",
//                                Toast.LENGTH_SHORT).show();
                        // display the images selected
                        //    ImageView imageView = (ImageView) findViewById(R.id.image1);
                        //  imageView.setImageResource(imageIDs[position]);

                        final Dialog dialog = new Dialog(NewsDetails.this,android.R.style.Theme_Black_NoTitleBar);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#CA303030")));
                        dialog.setContentView(R.layout.dialog_pic);



                        SliderLayout sliderShow = (SliderLayout) dialog.findViewById(R.id.slider);
                        sliderShow.setCustomIndicator((PagerIndicator) dialog.findViewById(R.id.custom_indicator));
                        // sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);

                        sliderShow.stopAutoCycle();


                        url_maps = new HashMap<String, String>();

                        //  HashMap<String,Integer> file_maps = new HashMap<String, Integer>();


                        for(int i = 0 ; i<strings.size() ; i++){
                            url_maps.put(i+"",strings.get(i));

                        }
                        url_maps.remove(position+1);



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
//                        for(int i = 0 ; i<((strings.size())-1) ; i++){
//
//                            SliderView
//                                    .image(strings.get(i));
//
//                        }
//                        SliderView.image(strings.get(position));
//


//                        sliderShow.addSlider(SliderView);

                for (String name : url_maps.keySet()) {


                    DefaultSliderView textSliderView = new DefaultSliderView(NewsDetails.this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    sliderShow.addSlider(textSliderView);

                }

                        dialog.show();



                    }
                });


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("آگهی با این شماره وجود ندارد !");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }


    public  void webServiceNewComment()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("validity","1");
        params.put("name",cName.getText().toString());
        params.put("news_id",pid);
        params.put("content",cContent.getText().toString());
        params.put("confirmation_id",id_confirmaation);


        client.post(App.urlApi+"addcomment", params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //  String value = new String(response);
                noComment.setVisibility(View.GONE);
                App.CustomToast("نظر شما با موفقیت ثبت شد");
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(NewsDetails.this.LAYOUT_INFLATER_SERVICE);
                addView = layoutInflater.inflate(R.layout.item_comment, null);
                txtName = (TextView)addView.findViewById(R.id.c_name);
                txtComment = (TextView)addView.findViewById(R.id.c_content);

                txtName.setText("نام: "+cName.getText().toString());
                txtComment.setText(cContent.getText().toString());
                list.addView(addView);

               // webServiceGetNewsDetails();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("خطا");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }
    public  void webServicelike()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.get(App.urlApi+"news/"+pid+"/like/"+App.android_id, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                if(!value.equals("-1")){
                    txtLike.setText(value);
                    txtLike.setTextColor(Color.parseColor("#ffffff"));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        like.setBackgroundDrawable( getResources().getDrawable(R.drawable.likebtnpressed) );
                    } else {
                        like.setBackground( getResources().getDrawable(R.drawable.likebtnpressed));
                    }

                    like.setClickable(false);
                    unlike.setClickable(false);


                    String likes = file.read("likes");
                    if (likes.equals("")) {
                        file.write("likes", pid);
                    } else {
                        file.write("likes", likes + "-" + pid);
                    }
                   // unlike.setVisibility(View.GONE);
                   // emtiaz.setText(value);
                }else {
                    App.CustomToast("امتیاز شما قبلا ثبت شده است");
                }

               // webServiceGetNewsDetails();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("خطا");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }
    public  void isLike()
    {

//        if(!value.equals("-1")){
//        }else {
//            final int sdk = android.os.Build.VERSION.SDK_INT;
//            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                like.setBackgroundDrawable( getResources().getDrawable(R.drawable.likebtnpressed) );
//            } else {
//                like.setBackground( getResources().getDrawable(R.drawable.likebtnpressed));
//            }
//            txtLike.setTextColor(Color.parseColor("#ffffff"));
//            like.setClickable(false);
//            unlike.setClickable(false);
//
//        }
        file = new FileOperations();

        String likestr =file.read("likes");
        liksarray=likestr.split("-");


        if(Arrays.asList(liksarray).contains(pid+"")){
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                like.setBackgroundDrawable( getResources().getDrawable(R.drawable.likebtnpressed) );
            } else {
                like.setBackground( getResources().getDrawable(R.drawable.likebtnpressed));
            }
            txtLike.setTextColor(Color.parseColor("#ffffff"));
            like.setClickable(false);
            unlike.setClickable(false);
        }

        String dislikestr =file.read("dislikes");
        disliksarray=dislikestr.split("-");

        if(Arrays.asList(disliksarray).contains(pid+"")){
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                unlike.setBackgroundDrawable( getResources().getDrawable(R.drawable.likebtnpressed) );
            } else {
                unlike.setBackground( getResources().getDrawable(R.drawable.likebtnpressed));
            }
            txtUnlike.setTextColor(Color.parseColor("#ffffff"));
            unlike.setClickable(false);
            like.setClickable(false);


        }


    }
    public  void webServiceunlike()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        client.get(App.urlApi+"news/"+pid+"/unlike/"+App.android_id, params, new AsyncHttpResponseHandler() {   // **************   get request  vase post: clinet.post qarar midim
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String value = new String(response);
                if(!value.equals("-1")){
                    txtUnlike.setText(  value);
                    txtUnlike.setTextColor(Color.parseColor("#ffffff"));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        unlike.setBackgroundDrawable( getResources().getDrawable(R.drawable.unlikebtnpressed) );
                    } else {
                        unlike.setBackground( getResources().getDrawable(R.drawable.unlikebtnpressed));
                    }

                    unlike.setClickable(false);
                    like.setClickable(false);

                    String dislikes = file.read("dislikes");
                    if (dislikes.equals("")) {
                        file.write("dislikes", pid);
                    } else {
                        file.write("dislikes", dislikes + "-" + pid);
                    }
                    //like.setVisibility(View.GONE);
                  //  emtiaz.setText(value);
                }else {
                    App.CustomToast("امتیاز شما قبلا ثبت شده است");
                }

               // webServiceGetNewsDetails();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                if(statusCode==404)  //**************   agar agahi vojud nadashte bashe man code 404 mifrestam
                {
                    App.CustomToast("خطا");

                }else{
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                }
            }
        });
    }
    public Boolean isMobileDataEnabled(){
        Object connectivityService = App.context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean)m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
