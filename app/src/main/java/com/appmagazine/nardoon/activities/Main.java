package com.appmagazine.nardoon.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.PagerAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.fragments.test;
import com.appmagazine.nardoon.fragments.test2;

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

       // forceRTLIfSupported();

        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        llnewagahi=(LinearLayout) findViewById(R.id.ll_new_agahi);
        tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
        tvtitle.setTypeface(tfmorvarid);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("نی نی عکس").setIcon(R.mipmap.baby));
        tabLayout.addTab(tabLayout.newTab().setText("پیامک انبوه").setIcon(R.mipmap.sms));
        tabLayout.addTab(tabLayout.newTab().setText("دسته بندی").setIcon(R.mipmap.list));
        tabLayout.addTab(tabLayout.newTab().setText("صفحه اصلی").setIcon(R.mipmap.home));
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


                Intent intent = new Intent(App.context ,New.class );
                startActivity(intent);
               // App.CustomToast("آگهی جدید");
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new test()).commit();


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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }







}
