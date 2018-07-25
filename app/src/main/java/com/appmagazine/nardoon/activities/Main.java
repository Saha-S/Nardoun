package com.appmagazine.nardoon.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.appmagazine.nardoon.Adapter.PagerAdapter;
import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.BuildConfig;
import com.appmagazine.nardoon.FileOperations;
import com.appmagazine.nardoon.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TabHost tabs;
    ImageButton ibmenu , ib,ib_search;
    TextView tvtitle , txt;
    LinearLayout llnewagahi,ll_search;
    public static Handler h;
    public  Thread t;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        if (Build.VERSION.SDK_INT >= 23) {
//
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//
//                //File write logic here
//                FileOperations file = new FileOperations();
//
//                if (file.read("likes").equalsIgnoreCase("nofile")) {
//
//                    file.write("likes", "");
//                    file.write("dislikes", "");
//                    file.write("favorite", "");
//                }
//
//            }else {
//
//                App.newactivity(Main.this, Request.class);
//                finish();
//            }
//
//
//        }else {
//            FileOperations file = new FileOperations();
//
//            if (file.read("likes").equalsIgnoreCase("nofile")) {
//
//                file.write("likes", "");
//                file.write("dislikes", "");
//                file.write("favorite", "");
//            }
//        }
//
//

        ibmenu=(ImageButton) findViewById(R.id.ib_menu);
        ib=(ImageButton) findViewById(R.id.ib);
        ib_search=(ImageButton) findViewById(R.id.ib_search);
        tvtitle=(TextView) findViewById(R.id.tv_mainpage_title);
        txt=(TextView) findViewById(R.id.txtNew);
        llnewagahi=(LinearLayout) findViewById(R.id.ll_new_agahi);
        ll_search=(LinearLayout) findViewById(R.id.ll_search);
        Typeface tfmorvarid= Typeface.createFromAsset(App.context.getAssets(), "morvarid.ttf");
        tvtitle.setTypeface(tfmorvarid);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("نی نی عکس").setIcon(R.mipmap.baby));
        tabLayout.addTab(tabLayout.newTab().setText("پیامک انبوه").setIcon(R.mipmap.sms));
        tabLayout.addTab(tabLayout.newTab().setText("مجله خبری").setIcon(R.mipmap.news));
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
        viewPager.setCurrentItem(5);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {

                    case 0:
                        ib.setVisibility(View.VISIBLE);
                        ll_search.setVisibility(View.GONE);

                        txt.setText("افزودن نی نی");
                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,NewNini.class );
                                startActivity(intent);
                            }
                        });
                        break;


                    case 1:
                        ib.setVisibility(View.GONE);
                        ll_search.setVisibility(View.GONE);

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
                        ib.setVisibility(View.GONE);
                        ll_search.setVisibility(View.GONE);

                        txt.setText("ایجاد خبر جدید");
                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,NewNews.class );
                                startActivity(intent);
                            }
                        });
                        break;

                    case 4:
                        ib.setVisibility(View.VISIBLE);
                        ll_search.setVisibility(View.GONE);

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
                        ll_search.setVisibility(View.VISIBLE);
                        txt.setText("آگهی جدید");


                        llnewagahi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(App.context ,NewAgahi.class );
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
                final Menu nav_Menu = navigationView.getMenu();

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

                SharedPreferences.Editor editor2 = getSharedPreferences("LOGIN_ID", MODE_PRIVATE).edit();
                editor2.putString("time", "0");
                editor2.commit();

                SharedPreferences.Editor edi_mobile = getSharedPreferences("MOBILE", MODE_PRIVATE).edit();
                edi_mobile.putString("mobile", "0");
                edi_mobile.commit();


                Intent intent = new Intent(App.context , Main.class);
                startActivity(intent);
                finish();

            }
        });
        SharedPreferences prefs = getSharedPreferences("LOGIN_ID", MODE_PRIVATE);
        SharedPreferences prefs2 = getSharedPreferences("IS_LOGIN", MODE_PRIVATE);
        String status = prefs2.getString("islogin", "0");
        SharedPreferences prefsMobile = getSharedPreferences("MOBILE", MODE_PRIVATE);
        final String mobile = prefsMobile.getString("mobile", "0");




        h = new Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch(msg.what) {

                    case 0:
                        SharedPreferences prefsMobile = getSharedPreferences("MOBILE", MODE_PRIVATE);
                        final String mobile = prefsMobile.getString("mobile", "0");

                        Log.i("nnnnnnnn11" ,"???" +mobile);

                        nav_Menu.findItem(R.id.nav_login).setVisible(false);
                        nav_mobile.setVisibility(View.VISIBLE);
                        btnExit.setVisibility(View.VISIBLE);
                        nav_mobile.setText(mobile);
                        break;

                }
            }

        };


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

        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.context ,Search.class );
                startActivity(intent);
            }
        });


      t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int buildconfig = BuildConfig.VERSION_CODE;

                                if(App.appversion > buildconfig){

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main.this);

                                    alertDialogBuilder
                                            .setMessage(App.versiontxt)
                                            .setCancelable(true)
                                            .setPositiveButton("دریافت",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("http://nardoun.ir/nardoun.apk"));
                                                    startActivity(intent);


                                                }
                                            })
                                            .setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    alertDialog.show();

                                    TextView msgtv = (TextView) alertDialog.findViewById(android.R.id.message);
                                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);
                                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(App.getFont());
                                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);
                                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(App.getFont());
                                    msgtv.setTypeface(App.getFont());
                                    msgtv.setTextSize(19);

                                    t.interrupt();

                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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
            Intent intent = new Intent(App.context , AboutUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_favarit) {
            Intent intent = new Intent(App.context , Favorite.class);
            startActivity(intent);

        } else if (id == R.id.nav_my_agahi) {

            Intent intent = new Intent(App.context , MyAgahis.class);
            startActivity(intent);

        }else if (id == R.id.nav_my_news) {

            Intent intent = new Intent(App.context , MyNews.class);
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

        else if (id == R.id.nav_my_agahi) {

        }else if (id == R.id.nav_rules) {
            Intent intent = new Intent(App.context , Rules.class);
            startActivity(intent);

        }else if (id == R.id.nav_support) {
            Intent intent = new Intent(App.context , Poshtibani.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_winner_nini) {
            Intent intent = new Intent(App.context , WinnerNini.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_search) {
            Intent intent = new Intent(App.context , Search.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }






}
