package com.appmagazine.nardoon.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appmagazine.nardoon.fragments.Category;
import com.appmagazine.nardoon.fragments.Main;
import com.appmagazine.nardoon.fragments.News;
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
            News tab3 = new News();
            return tab3;

            case 3:
            Category tab4 = new Category();
            return tab4;

            case 4:
                Main tab5 = new Main();
                return tab5;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}