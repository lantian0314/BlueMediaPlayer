package com.blue.mediaplayer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.model_basic.utils.DeviceInfo;
import com.blue.model_basic.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xingyatong on 2018/4/3.
 */

public class VideoPlayActivity extends AppCompatActivity {
    @BindView(R.id.vv_videoplayer)
    VideoView mVideoView;
    @BindView(R.id.btn_video_start_pause)
    Button btn_video_start_pause;
    @BindView(R.id.btn_exit)
    Button btn_exit;
    @BindView(R.id.btn_video_pre)
    Button btn_video_pre;
    @BindView(R.id.btn_video_next)
    Button btn_video_next;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_system_time)
    TextView tv_system_time;
    @BindView(R.id.iv_battery)
    ImageView iv_battery;
    @BindView(R.id.tv_current_time)
    TextView tv_current_time;
    @BindView(R.id.tv_duration)
    TextView tv_duration;
    @BindView(R.id.seekbar_video)
    SeekBar seekbar_video;
    @BindView(R.id.seekbar_voice)
    SeekBar seekbar_voice;

    private Uri uri;
    private ArrayList<MediaItem> mediaItems;
    private int position;//视频列表的位置
    private DeviceInfo deviceInfo;
    private MyReceiver myReceiver;
    private Utils utils;
    private AudioManager audioManager;
    private int currentVoice;
    private int maxVoice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        setVideoData();
        setClickListener();
        //设置控制面板
        //mVideoView.setMediaController(new MediaController(this));
    }

    private void setClickListener() {
        mVideoView.setOnPreparedListener(new onMyPrepareListener());
        mVideoView.setOnErrorListener(new onMyErrorListener());
        mVideoView.setOnCompletionListener(new onMyCompletionListener());
        seekbar_video.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        seekbar_voice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
    }

    private void setVideoData() {
        utils = new Utils();
        //注册电量变化的广播
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver, intentFilter);

        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);
        deviceInfo = new DeviceInfo(getApplicationContext());
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tv_name.setText(mediaItem.getName());
            mVideoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            tv_name.setText(uri.toString());
            mVideoView.setVideoURI(uri);
        }
        setButtonClickState();
        //得到音量
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekbar_voice.setMax(maxVoice);
        seekbar_voice.setProgress(currentVoice);
    }

    /**
     * 准备的监听事件
     */
    class onMyPrepareListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoView.start();
            //得到视频的总长度
            int duration = mVideoView.getDuration();
            seekbar_video.setMax(duration);
            tv_duration.setText(utils.stringForTime(duration));

            mHandler.sendEmptyMessage(MSG_PROGRESS);
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

    @OnClick({R.id.btn_video_start_pause, R.id.btn_exit, R.id.btn_video_pre, R.id.btn_video_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_video_start_pause:
                startAndPause();
                break;
            case R.id.btn_exit:
                finish();
                break;
            case R.id.btn_video_pre:
                playPreVideo();
                break;
            case R.id.btn_video_next:
                playNextVideo();
                break;
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //播放上一个视频
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                tv_name.setText(mediaItem.getName());
                mVideoView.setVideoPath(mediaItem.getData());
                setButtonClickState();
            }
        } else if (uri != null) {
            setButtonClickState();
        }
    }

    /**
     * 播放上一个视频
     */
    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //播放上一个视频
            position--;
            if (position >= 0) {
                MediaItem mediaItem = mediaItems.get(position);
                tv_name.setText(mediaItem.getName());
                mVideoView.setVideoPath(mediaItem.getData());
                setButtonClickState();
            }
        } else if (uri != null) {
            setButtonClickState();
        }
    }

    /**
     * 设置按钮点击的状态
     */
    private void setButtonClickState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                setEnable(false);
            } else if (mediaItems.size() == 2) {
                if (position == 0) {
                    btn_video_pre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btn_video_pre.setEnabled(false);
                    btn_video_next.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btn_video_next.setEnabled(true);
                } else if (position == mediaItems.size() - 1) {
                    btn_video_pre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btn_video_pre.setEnabled(true);
                    btn_video_next.setBackgroundResource(R.drawable.btn_next_gray);
                    btn_video_next.setEnabled(false);
                }
            } else {
                if (position == 0) {
                    btn_video_pre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btn_video_pre.setEnabled(false);
                } else if (position == mediaItems.size() - 1) {
                    btn_video_next.setBackgroundResource(R.drawable.btn_next_gray);
                    btn_video_next.setEnabled(false);
                } else {
                    setEnable(true);
                }
            }

        }
    }

    /**
     * @param state
     */
    private void setEnable(boolean state) {
        btn_video_pre.setBackgroundResource(R.drawable.btn_video_pre_selector);
        btn_video_pre.setEnabled(state);
        btn_video_next.setBackgroundResource(R.drawable.btn_video_next_selector);
        btn_video_next.setEnabled(state);
    }

    /**
     * 视频开始和暂停
     */
    private void startAndPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            //按钮设置播放状态
            btn_video_start_pause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            mVideoView.start();
            //按钮设置暂停
            btn_video_start_pause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    private final int MSG_PROGRESS = 10;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PROGRESS:
                    tv_system_time.setText(deviceInfo.getSystemTime());//更新系统时间
                    //得到当前的播放进度，更新
                    int currentPosition = mVideoView.getCurrentPosition();
                    seekbar_video.setProgress(currentPosition);
                    tv_current_time.setText(utils.stringForTime(currentPosition));

                    //每秒更新一次
                    mHandler.removeMessages(MSG_PROGRESS);
                    mHandler.sendEmptyMessageDelayed(MSG_PROGRESS, 1000);
                    break;
            }
        }
    };

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            setBattery(level);
        }
    }

    /**
     * 设置电量的变化
     */
    private void setBattery(int level) {
        if (level <= 0) {
            iv_battery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            iv_battery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            iv_battery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            iv_battery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            iv_battery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            iv_battery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            iv_battery.setImageResource(R.drawable.ic_battery_100);
        } else {
            iv_battery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mVideoView.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                seekbar_voice.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
