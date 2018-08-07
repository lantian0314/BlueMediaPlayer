package com.blue.mediaplayer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blue.mediaplayer.R;
import com.blue.mediaplayer.ui.fragment.MainFragment;
import butterknife.ButterKnife;
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
