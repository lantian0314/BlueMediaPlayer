package com.blue.mediaplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class MFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragmentList;
    public MFragmentPagerAdapter(FragmentManager fm,List<Fragment> mList) {
        super(fm);
        this.fragmentList=mList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
