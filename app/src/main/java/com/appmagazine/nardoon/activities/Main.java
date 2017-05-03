package com.appmagazine.nardoon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.PagerAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TabHost tabs;
    ImageButton ibmenu , ib;
    TextView tvtitle , txt;
    LinearLayout llnewagahi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        ib=(ImageButton) findViewById(R.id.ib);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        txt=(TextView) findViewById(R.id.txtNew);
        llnewagahi=(LinearLayout) findViewById(R.id.ll_new_agahi);
        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
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
                switch (tab.getPosition()) {
                    case 1:
                        ib.setVisibility(View.GONE);
                        txt.setText("تاریخچه خرید");
                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,MyPanel.class );
                                startActivity(intent);
                            }
                        });
                        break;
                    case 2:
                        ib.setVisibility(View.VISIBLE);
                        txt.setText("آگهی جدید");
                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,NewAgahi.class );
                                startActivity(intent);
                            }
                        });
                        break;
                    case 3:
                        ib.setVisibility(View.VISIBLE);
                        txt.setText("آگهی جدید");
                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,NewAgahi.class );
                                startActivity(intent);
                            }
                        });
                        break;

                    case 0:
                        ib.setVisibility(View.VISIBLE);
                        txt.setText("افزودن نی نی");
                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,NewNini.class );
                                startActivity(intent);
                            }
                        });

                        break;
                }
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
        Menu nav_Menu = navigationView.getMenu();

        View hView =  navigationView.getHeaderView(0);
        final TextView nav_mobile = (TextView)hView.findViewById(R.id.txt_mobile_number);
        final Button btnExit = (Button)hView.findViewById(R.id.btn_exit);
        nav_mobile.setVisibility(View.GONE);
        btnExit.setVisibility(View.GONE);


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("IS_LOGIN", MODE_PRIVATE).edit();
                editor.putString("islogin", "0");
                editor.commit();
                Intent intent = new Intent(App.context , Main.class);
                startActivity(intent);
                finish();

            }
        });


        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        String status = prefs2.getString("islogin", "0");
        String mobile = prefs.getString("mobile", "0");

        if (status.matches("1")) {

            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            nav_mobile.setVisibility(View.VISIBLE);
            btnExit.setVisibility(View.VISIBLE);
            nav_mobile.setText(mobile);

        }


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
                Intent intent = new Intent(App.context ,NewAgahi.class );
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

        if (id == R.id.nav_about) {
            // Handle the camera action
        } else if (id == R.id.nav_favarit) {
            Intent intent = new Intent(App.context , Favorite.class);
            startActivity(intent);


        } else if (id == R.id.nav_my_agahi) {

            Intent intent = new Intent(App.context , MyAgahis.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_my_nini) {

            Intent intent = new Intent(App.context , MyNini.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_login) {

                Intent intent = new Intent(App.context , Login.class);
                startActivity(intent);
        }
        else if (id == R.id.nav_recently) {

        }else if (id == R.id.nav_my_agahi) {

        }else if (id == R.id.nav_rules) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }







}
