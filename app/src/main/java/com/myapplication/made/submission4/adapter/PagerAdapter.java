package com.myapplication.made.submission4.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.myapplication.made.submission4.fragment.MovieFragment;
import com.myapplication.made.submission4.fragment.TvShowFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numberTabs;
    private boolean isfavActivity = false;

    public PagerAdapter(FragmentManager fm, int numberTabs, boolean isfavActivity) {
        super(fm);
        this.numberTabs = numberTabs;
        this.isfavActivity = isfavActivity;
    }



    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                if (isfavActivity) {
                    return new MovieFragment(true);
                } else {
                    return new MovieFragment(false);
                }
            case 1:
                if (isfavActivity) {
                    return new TvShowFragment(true);
                } else {
                    return new TvShowFragment(false);
                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
