package com.blue.mediaplayer.mvp.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import com.blue.mediaplayer.bean.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoModel {
    private List<MediaItem> mediaItemList = null;
    private Context mContext;
    private videoDataInterface mVideoDataInterface;

    public VideoModel(Context context) {
        this.mContext = context;
    }

    public void getVideoList(videoDataInterface mVideoDataInterface) {
        this.mVideoDataInterface=mVideoDataInterface;
        new Thread() {
            @Override
            public void run() {
                super.run();
                mediaItemList = new ArrayList<>();
                ContentResolver contentResolver = mContext.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor = contentResolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MediaItem mediaItem = new MediaItem();
                        mediaItemList.add(mediaItem);
                        mediaItem.setName(cursor.getString(0));
                        mediaItem.setDuration(cursor.getLong(1));
                        mediaItem.setSize(cursor.getLong(2));
                        mediaItem.setData(cursor.getString(3));
                        mediaItem.setArtist(cursor.getString(4));
                    }
                    cursor.close();
                }
                //回调得到的数据
                handler.sendEmptyMessage(10);

            }

        }.start();
    }

    public interface videoDataInterface {
        void getDataList(List<MediaItem> mediaItemList);
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mVideoDataInterface != null) {
                mVideoDataInterface.getDataList(mediaItemList);
            }
        }
    };
}
