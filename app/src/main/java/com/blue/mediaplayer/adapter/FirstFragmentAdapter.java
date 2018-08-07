package com.blue.mediaplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blue.mediaplayer.ui.fragment.first.NetVideoFragment;
import com.blue.mediaplayer.ui.fragment.first.VideoFragment;

/**
 * Created by xingyatong on 2018/8/7 09:18
 * Describe 第一个主页面的适配器
 */
public class FirstFragmentAdapter extends FragmentPagerAdapter {

    private final String[] mTitils;

    public FirstFragmentAdapter(FragmentManager fragmentManager, String... titles) {
        super(fragmentManager);
        mTitils = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return VideoFragment.newInstance();
        } else {
            return NetVideoFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitils.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitils[position];
    }
}
