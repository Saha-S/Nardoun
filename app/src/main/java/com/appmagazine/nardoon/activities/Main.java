package com.appmagazine.nardoon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.PagerAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TabHost tabs;
    ImageButton ibmenu;
    TextView tvtitle;
    Typeface tfmorvarid;
    LinearLayout llnewagahi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        llnewagahi=(LinearLayout) findViewById(R.id.ll_new_agahi);
        tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
        tvtitle.setTypeface(tfmorvarid);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("نی نی عکس").setIcon(R.drawable.baby));
        tabLayout.addTab(tabLayout.newTab().setText("پیامک انبوه").setIcon(R.drawable.sms));
        tabLayout.addTab(tabLayout.newTab().setText("دسته بندی").setIcon(R.drawable.list));
        tabLayout.addTab(tabLayout.newTab().setText("صفحه اصلی").setIcon(R.drawable.home));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(App.appfont);
                }
            }
        }


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(4);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ibmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }

            }
        });

        llnewagahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                App.CustomToast("آگهی جدید");

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


    // dashte bash injaro che khube nadiiii   >  *********

    public  void webServiceActivation()
    {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", "");
        params.put("password", "");

        client.post("http://192.168.0.244/matbakh/public/api/attemptlogin", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
               // loginpb1.setVisibility(View.VISIBLE);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
               // loginpb1.setVisibility(View.INVISIBLE);
                String value = new String(response);
                try {
                    JSONObject obj =new JSONArray(value).getJSONObject(0);



//                    App.loginname=obj.getString("name");
//                    App.complexname=obj.getString("complex_name");
//                    App.setConnectionData();
//                    App.newactivity(ServerLogin.this,Login.class);
//                    finish();


                } catch (JSONException e1) {

                    e1.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                if(statusCode==406)
                {
                    App.CustomToast("حساب کاربری شما تایید نشده است !");
                    //loginpb1.setVisibility(View.INVISIBLE);
                }else if(statusCode==401){
                    App.CustomToast("نام کاربری یا کلمه عبور اشتباه میباشد !");
                    //loginpb1.setVisibility(View.INVISIBLE);

                } else{
                    App.CustomToast("fail "+statusCode);
                    App.CustomToast(" لطفا دوباره سعی کنید ");
                    //loginpb1.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });


    }


}
