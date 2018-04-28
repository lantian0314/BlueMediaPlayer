package com.blue.mediaplayer.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.view.VitamioVideoView;
import com.blue.model_basic.utils.DeviceInfo;
import com.blue.model_basic.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * Created by xingyatong on 2018/4/3.
 */

public class VitamioVideoPlayActivity extends AppCompatActivity {
    @BindView(R.id.vv_videoplayer)
    VitamioVideoView mVideoView;
    @BindView(R.id.btn_video_start_pause)
    Button btn_video_start_pause;
    @BindView(R.id.btn_exit)
    Button btn_exit;
    @BindView(R.id.btn_video_pre)
    Button btn_video_pre;
    @BindView(R.id.btn_video_next)
    Button btn_video_next;
    @BindView(R.id.btn_video_siwch_screen)
    Button btn_video_siwch_screen;
    @BindView(R.id.btn_voice)
    Button btn_voice;
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
    @BindView(R.id.media_controller)
    RelativeLayout media_controller;
    @BindView(R.id.ll_buffer)
    LinearLayout ll_buffer;
    @BindView(R.id.tv_buffernetspeed)
    TextView tv_netspeed;
    @BindView(R.id.tv_loadingNetSpeed)
    TextView tv_loadingNetSpeed;
    @BindView(R.id.ll_loading)
    LinearLayout ll_loading;

    private Uri uri;
    private ArrayList<MediaItem> mediaItems;
    private int position;//视频列表的位置
    private DeviceInfo deviceInfo;
    private MyReceiver myReceiver;
    private Utils utils;
    private AudioManager audioManager;
    private int currentVoice;
    private int maxVoice;
    private GestureDetector gestureDetector;
    private boolean isShowMediaController = false;//是否展示控制面板
    /**
     * 视频的宽高
     */
    private int videoWidth;
    private int videoHeigh;
    /**
     * 屏幕的宽高
     */
    private int screenWidth;
    private int screenHeight;
    private final int FULL_SCREEN = 10;
    private final int DEFAULT_SCREEN = 20;
    private boolean isFullState = false;
    private boolean isMute = false;
    private boolean isNetUri = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.initialize(this);//初始化Vitamio
        setContentView(R.layout.activity_vitamiovideo_player);
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
        gestureDetector = new GestureDetector(this, new MySimpleOnGestureListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mVideoView.setOnInfoListener(new MyOninfoListener());
        }
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
        int[] metrics = deviceInfo.getDisplayMetrics();
        screenWidth = metrics[0];
        screenHeight = metrics[1];
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tv_name.setText(mediaItem.getName());
            isNetUri = utils.isNetUri(mediaItem.getData());
            mVideoView.setVideoPath(mediaItem.getData());
        } else if (uri != null) {
            tv_name.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
            mVideoView.setVideoURI(uri);
        }
        if (isNetUri) {
            ll_loading.setVisibility(View.VISIBLE);
        }
        setButtonClickState();
        //得到音量
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekbar_voice.setMax(maxVoice);
        seekbar_voice.setProgress(currentVoice);
        //发送网速消息
        mHandler.sendEmptyMessage(MSG_NETSPEED);
    }

    /**
     * 准备的监听事件
     */
    class onMyPrepareListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoWidth = mp.getVideoWidth();
            videoHeigh = mp.getVideoHeight();
            ll_loading.setVisibility(View.GONE);
            setDefaultAndFullScreen(DEFAULT_SCREEN);
            mVideoView.start();
            //得到视频的总长度
            int duration = (int) mVideoView.getDuration();
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
            //Toast.makeText(VitamioVideoPlayActivity.this, "播放出错", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    class onMyCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VitamioVideoPlayActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.btn_video_start_pause, R.id.btn_exit, R.id.btn_video_pre, R.id.btn_video_next, R.id.btn_video_siwch_screen, R.id.btn_voice})
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
            case R.id.btn_video_siwch_screen:
                setVideoType();
                break;
            case R.id.btn_voice:
                isMute = !isMute;
                updateVoice(currentVoice, isMute);
                break;
        }
    }

    /**
     * 设置视频的屏幕样式
     */
    private void setVideoType() {
        if (isFullState) {
            setDefaultAndFullScreen(DEFAULT_SCREEN);
        } else {
            setDefaultAndFullScreen(FULL_SCREEN);
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
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tv_name.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
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
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tv_name.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
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

    /**
     * 设置默认和全屏
     *
     * @param type
     */
    private void setDefaultAndFullScreen(int type) {
        switch (type) {
            case FULL_SCREEN:
                //改变全屏状态
                isFullState = true;
                //mVideoView.setVideoSize(screenWidth, screenHeight);
                //默认状态的适配器
                btn_video_siwch_screen.setBackgroundResource(R.drawable.btn_video_siwch_screen_default_selector);
                break;
            case DEFAULT_SCREEN:
                //改变全屏状态
                isFullState = false;
                int height = screenHeight;
                int width = screenWidth;

                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeigh;
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                mVideoView.setVideoSize(width, height);
                //全屏状态的适配器
                btn_video_siwch_screen.setBackgroundResource(R.drawable.btn_video_siwch_screen_full_selector);
                break;
        }
    }

    private final int MSG_PROGRESS = 10;
    private final int MSG_HIDEMEDIACONTROLLER = 20;//隐藏控制栏
    private final int MSG_NETSPEED = 30;//网速
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_PROGRESS:
                    tv_system_time.setText(deviceInfo.getSystemTime());//更新系统时间
                    //得到当前的播放进度，更新
                    int currentPosition = (int) mVideoView.getCurrentPosition();
                    seekbar_video.setProgress(currentPosition);
                    tv_current_time.setText(utils.stringForTime(currentPosition));

                    //更新网络视频缓存进度
                    if (isNetUri) {
                        int bufferPercent = mVideoView.getBufferPercentage();//0~100
                        int seekMax = seekbar_video.getMax();
                        int secondaryProgress = bufferPercent * seekMax / 100;
                        seekbar_video.setSecondaryProgress(secondaryProgress);
                    } else {
                        seekbar_video.setSecondaryProgress(0);
                    }
                    //每秒更新一次
                    mHandler.removeMessages(MSG_PROGRESS);
                    mHandler.sendEmptyMessageDelayed(MSG_PROGRESS, 1000);
                    break;
                case MSG_HIDEMEDIACONTROLLER:
                    setMediaControllerState(false);
                    break;
                case MSG_NETSPEED:
                    String netSpeed = utils.getNetSpeed(VitamioVideoPlayActivity.this);
                    tv_loadingNetSpeed.setText("正在加载中..." + netSpeed);
                    tv_netspeed.setText("缓冲中..." + netSpeed);

                    mHandler.removeMessages(MSG_NETSPEED);
                    mHandler.sendEmptyMessageDelayed(MSG_NETSPEED, 2 * 1000);
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
            mHandler.removeMessages(MSG_HIDEMEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.sendEmptyMessageDelayed(MSG_HIDEMEDIACONTROLLER, 5000);
        }
    }

    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (progress > 0) {
                    isMute = false;
                } else {
                    isMute = true;
                }
            }
            updateVoice(progress, isMute);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeMessages(MSG_HIDEMEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.sendEmptyMessageDelayed(MSG_HIDEMEDIACONTROLLER, 5000);
        }
    }

    private void updateVoice(int progress, boolean isMute) {
        if (isMute) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbar_voice.setProgress(0);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbar_voice.setProgress(progress);
            currentVoice = progress;
        }
    }

    /**
     * 视频卡的监听
     */
    class MyOninfoListener implements MediaPlayer.OnInfoListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START://视频开始卡
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END://结束卡
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return false;
        }
    }

    /**
     * 手势识别器的监听
     */
    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            //长按
            startAndPause();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //双击
            setVideoType();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //单击
            if (isShowMediaController) {
                //隐藏控制面板
                setMediaControllerState(false);
                mHandler.removeMessages(MSG_HIDEMEDIACONTROLLER);
            } else {
                //展示控制面板
                setMediaControllerState(true);
                mHandler.sendEmptyMessageDelayed(MSG_HIDEMEDIACONTROLLER, 5000);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    /**
     * 设置媒体
     *
     * @param ishow
     */
    private void setMediaControllerState(boolean ishow) {
        int visible = View.VISIBLE;
        if (!ishow) {
            visible = View.INVISIBLE;
        }
        media_controller.setVisibility(visible);
        isShowMediaController = ishow;
    }

    private float startY;//起始位置
    private float mVoice;//当前按下的音量
    private float mscreenHeight;//当前屏幕的高

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                startY = event.getY();
                mVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mscreenHeight = Math.max(screenHeight, screenWidth);
                mHandler.removeMessages(MSG_HIDEMEDIACONTROLLER);//移除消息
                break;
            case MotionEvent.ACTION_MOVE://手指移动
                float endY = event.getY();
                float changeValue = startY - endY;
                float changeVoice = (changeValue / mscreenHeight) * maxVoice;
                float nowVoice = Math.max(Math.min((mVoice + changeVoice), 0), maxVoice);
                if (changeVoice != 0) {
                    isMute = false;
                    updateVoice((int) nowVoice, isMute);
                }
                break;
            case MotionEvent.ACTION_UP://手指起来
                mHandler.sendEmptyMessageDelayed(MSG_HIDEMEDIACONTROLLER, 5000);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            updataVoiceByKeyEvent(true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            updataVoiceByKeyEvent(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 根据声音按键来改变声音的大小
     *
     * @param isDown
     */
    private void updataVoiceByKeyEvent(boolean isDown) {
        if (isDown) {
            currentVoice--;
        } else {
            currentVoice++;
        }
        updateVoice(currentVoice, false);
        mHandler.removeMessages(MSG_HIDEMEDIACONTROLLER);
        mHandler.sendEmptyMessageDelayed(MSG_HIDEMEDIACONTROLLER, 5000);
    }
}