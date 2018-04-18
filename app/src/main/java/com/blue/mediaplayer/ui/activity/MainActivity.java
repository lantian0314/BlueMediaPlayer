package com.blue.mediaplayer.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.adapter.MFragmentPagerAdapter;
import com.blue.mediaplayer.ui.fragment.AudioFragment;
import com.blue.mediaplayer.ui.fragment.DynamicFragment;
import com.blue.mediaplayer.ui.fragment.VideoFragment;
import com.blue.model_basic.utils.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rb_video)
    RadioButton tv_video;
    @BindView(R.id.rb_audio)
    RadioButton tv_audo;
    @BindView(R.id.rb_dynamic)
    RadioButton tv_dynamic;

    @BindView(R.id.cursor)
    ImageView cursor;

    @BindView(R.id.vPager)
    ViewPager mViewPager;

    private List<Fragment> fragmentList = null;
    private FragmentManager fragmentManager;

    //当前页卡编号
    private int currIndex = 0;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        ButterKnife.bind(this);
        InitImageView();
        initFragment();
        initViewPager();

    }

    private void initViewPager() {
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentList));
        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(2);
        //设置默认打开第一页
        mViewPager.setCurrentItem(0);
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new VideoFragment());
        fragmentList.add(new AudioFragment());
        fragmentList.add(new DynamicFragment());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        DeviceInfo deviceInfo = new DeviceInfo(this);
        // 获取分辨率宽度
        int[] displayMetrics = deviceInfo.getDisplayMetrics();
        int screenW = displayMetrics[0];
        int imageWidth = (screenW / 3);
        //设置动画图片宽度
        setImageWidth(cursor, imageWidth);

        offset = 0;
        //动画图片偏移量赋值
        position_one = (int) (screenW / 3.0);
        position_two = position_one * 2;
    }

    /**
     * 设置动画图片宽度
     *
     * @param mWidth
     */
    private void setImageWidth(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = mWidth;
        imageView.setLayoutParams(params);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(@IntRange(from = 0, to = 10) int position) {
            Animation animation = null;
            switch (position) {
                //当前为页卡1
                case 0:
                    //从页卡2跳转转到页卡1
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                    } else if (currIndex == 2) {//从页卡3跳转转到页卡1
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                    }
                    setVideoDrawable(true);
                    setAutioDrawable(false);
                    setDynamicDrawable(false);
                    break;
                //当前为页卡2
                case 1:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                    } else if (currIndex == 2) { //从页卡3跳转转到页卡2
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    }
                    setAutioDrawable(true);
                    setVideoDrawable(false);
                    setDynamicDrawable(false);
                    break;
                //当前为页卡3
                case 2:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                    } else if (currIndex == 1) {//从页卡2跳转转到页卡3
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    }
                    setDynamicDrawable(true);
                    setVideoDrawable(false);
                    setAutioDrawable(false);
                    break;
            }
            currIndex = position;
            animation.setFillAfter(true);// true:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setDynamicDrawable(boolean isPressed) {
        int drawable = R.drawable.ic_tab_dynamic;
        if (isPressed) {
            drawable = R.drawable.ic_tab_dynamic_press;
        }
        Drawable dynamicTop = getResources().getDrawable(drawable);
        dynamicTop.setBounds(0, 0, dynamicTop.getMinimumHeight(), dynamicTop.getMinimumHeight());
        tv_dynamic.setCompoundDrawables(null, dynamicTop, null, null);
    }

    private void setAutioDrawable(boolean isPressed) {
        int drawable = R.drawable.ic_tab_audio;
        if (isPressed) {
            drawable = R.drawable.ic_tab_audio_press;
        }
        Drawable autioTop = getResources().getDrawable(drawable);
        autioTop.setBounds(0, 0, autioTop.getMinimumHeight(), autioTop.getMinimumHeight());
        tv_audo.setCompoundDrawables(null, autioTop, null, null);
    }

    private void setVideoDrawable(boolean isPressed) {
        int drawable = R.drawable.ic_tab_video;
        if (isPressed) {
            drawable = R.drawable.ic_tab_video_press;
        }
        Drawable videoTop = getResources().getDrawable(drawable);
        videoTop.setBounds(0, 0, videoTop.getMinimumHeight(), videoTop.getMinimumHeight());
        tv_video.setCompoundDrawables(null, videoTop, null, null);
    }

    @OnClick({R.id.rb_video, R.id.rb_audio, R.id.rb_dynamic})
    public void txtClick(TextView txtView) {
        switch (txtView.getId()) {
            case R.id.rb_video:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rb_audio:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rb_dynamic:
                mViewPager.setCurrentItem(2);
                break;
        }
    }
}