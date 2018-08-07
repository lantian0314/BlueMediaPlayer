package com.blue.mediaplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.ui.fragment.first.FirstFragmentManager;
import com.blue.mediaplayer.ui.fragment.first.VideoFragment;
import com.blue.mediaplayer.view.BottomBar;
import com.blue.mediaplayer.view.BottomBarTab;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by xingyatong on 2018/8/6 17:05
 * Describe xxxxx
 */
public class MainFragment extends SupportFragment {

    private SupportFragment[] mFragments = new SupportFragment[3];

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;

    private BottomBar mBottomBar;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(FirstFragmentManager.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstFragmentManager.newInstance();
            mFragments[SECOND] = AudioFragment.newInstance();
            mFragments[THIRD] = DynamicFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(AudioFragment.class);
            mFragments[THIRD] = findChildFragment(DynamicFragment.class);
        }
    }

    private void initView(View view) {
        mBottomBar = view.findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_video, getString(R.string.video)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_audio, getString(R.string.audio)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.ic_tab_dynamic, getString(R.string.dynamic)));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
}
