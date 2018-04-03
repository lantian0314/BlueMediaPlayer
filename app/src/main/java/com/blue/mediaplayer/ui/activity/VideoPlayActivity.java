package com.blue.mediaplayer.ui.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.blue.mediaplayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xingyatong on 2018/4/3.
 */

public class VideoPlayActivity extends AppCompatActivity {
    @BindView(R.id.vv_videoplayer)
    VideoView mVideoView;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        uri = getIntent().getData();
        mVideoView.setVideoURI(uri);
        mVideoView.setOnPreparedListener(new onMyPrepareListener());
        mVideoView.setOnErrorListener(new onMyErrorListener());
        mVideoView.setOnCompletionListener(new onMyCompletionListener());
        //设置控制面板
        mVideoView.setMediaController(new MediaController(this));
    }

    /**
     * 准备的监听事件
     */
    class onMyPrepareListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoView.start();
        }
    }

    /**
     * 播放错误的监听事件
     */
    class onMyErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(VideoPlayActivity.this, "播放出错", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    class onMyCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VideoPlayActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
        }
    }
}
