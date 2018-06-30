package com.blue.mediaplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MusicPlayerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 打开对应位置的音频
     * @param position
     */
    public void openAudio(int position){

    }

    /**
     * 播放
     */
    private void start(){

    }

    /**
     * 暂停
     */
    private void pause(){

    }

    /**
     * 停止
     */
    private void stop(){

    }

    /**
     * 当前播放进度
     * @return
     */
    private int getCurrentPosition(){
        return 0;
    }

    /**
     * 当前播放时长
     * @return
     */
    private int getDuration(){
        return 0;
    }

    /**
     * 当前艺术家名字
     * @return
     */
    private String getArtist(){
        return "";
    }

    /**
     * 得到歌曲名称
     * @return
     */
    private String getName(){
        return "";
    }

    /**
     *得到歌曲播放的路径
     * @return
     */
    private String getAudioPath(){
        return "";
    }

    /**
     * 播放下一个音频
     */
    private void next() {

    }

    /**
     * 播放上一个音频
     */
    private void pre() {

    }

    /**
     * 设置播放模式
     *
     * @param playmode
     */
    private void setPlayMode(int playmode) {

    }

    /**
     * 得到播放模式
     *
     * @return
     */
    private int getPlayMode() {
        return 0;
    }


    /**
     * 是否在播放音频
     * @return
     */
    private boolean isPlaying(){
        return true;
    }
}
