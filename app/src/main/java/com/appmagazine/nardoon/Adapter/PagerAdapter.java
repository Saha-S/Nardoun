package com.appmagazine.nardoon.Adapter;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.appmagazine.nardoon.App;
import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.MyPanel;
import com.appmagazine.nardoon.fragments.Category;
import com.appmagazine.nardoon.fragments.Main;
import com.appmagazine.nardoon.fragments.NiniAx;
import com.appmagazine.nardoon.fragments.SMS;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);

        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NiniAx tab1 = new NiniAx();
                return tab1;

            case 1:
                SMS tab2 = new SMS();
                return tab2;
            case 2:
                Category tab3 = new Category();
                return tab3;
            case 3:
                Main tab4 = new Main();
                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}