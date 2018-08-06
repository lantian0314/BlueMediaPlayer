package com.blue.mediaplayer.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.adapter.MFragmentPagerAdapter;
import com.blue.mediaplayer.ui.fragment.AudioFragment;
import com.blue.mediaplayer.ui.fragment.DynamicFragment;
import com.blue.mediaplayer.ui.fragment.MainFragment;
import com.blue.mediaplayer.ui.fragment.VideoFragment;
import com.blue.model_basic.utils.DeviceInfo;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
        ButterKnife.bind(this);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
//        InitImageView();
//        initFragment();
//        initViewPager();

    }
}
