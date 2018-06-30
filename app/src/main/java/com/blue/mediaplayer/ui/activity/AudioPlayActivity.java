package com.blue.mediaplayer.ui.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blue.mediaplayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AudioPlayActivity extends Activity {
    @BindView(R.id.iv_audioicon)
    ImageView iv_audioicon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        ButterKnife.bind(this);
        iv_audioicon.setBackgroundResource(R.drawable.animation_list);
        //动画
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_audioicon.getBackground();
        animationDrawable.start();
    }
}
