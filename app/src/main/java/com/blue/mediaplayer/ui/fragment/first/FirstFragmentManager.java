package com.blue.mediaplayer.ui.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.adapter.FirstFragmentAdapter;
import com.blue.mediaplayer.base.BaseMainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xingyatong on 2018/8/7 09:06
 * Describe 第一页面的管理
 */
public class FirstFragmentManager extends BaseMainFragment {

    private View view;
    private TabLayout mTab;
    private ViewPager mViewPager;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static FirstFragmentManager newInstance() {
        Bundle args = new Bundle();
        FirstFragmentManager fragment = new FirstFragmentManager();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_manager, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tv_status.setVisibility(View.GONE);
        mToolbar.setTitle(getString(R.string.app_name));
        mTab = view.findViewById(R.id.tab);
        mViewPager = view.findViewById(R.id.viewPager);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mViewPager.setAdapter(new FirstFragmentAdapter(getChildFragmentManager(),
                getString(R.string.local_video),
                getString(R.string.net_video)));
        mTab.setupWithViewPager(mViewPager);
    }
}
