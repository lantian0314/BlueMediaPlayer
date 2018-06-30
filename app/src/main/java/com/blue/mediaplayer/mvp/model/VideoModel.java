package com.blue.mediaplayer.mvp.model;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.utils.MimeTypes;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoModel {
    private ArrayList<MediaItem> mediaItemList = null;
    private Context mContext;
    private videoDataInterface mVideoDataInterface;

    private final int SCAN_LEVEL = 5;//扫描文件的深度


    public VideoModel(Context context) {
        this.mContext = context;
    }

    public void getVideoList(videoDataInterface mVideoDataInterface) {
        this.mVideoDataInterface = mVideoDataInterface;
        new Thread() {
            @Override
            public void run() {
                super.run();
                mediaItemList = new ArrayList<>();
                File externalDir = Environment.getExternalStorageDirectory();
                if (externalDir != null) {
                    travelPath(externalDir, 0);
                }
                //queryDb();
                mHandler.sendEmptyMessage(10);
            }

        }.start();
    }

//    private void queryDb() {
//        ContentResolver contentResolver = mContext.getContentResolver();
//        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        String[] objs = {
//                MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
//                MediaStore.Video.Media.DURATION,//视频总时长
//                MediaStore.Video.Media.SIZE,//视频的文件大小
//                MediaStore.Video.Media.DATA,//视频的绝对地址
//                MediaStore.Video.Media.ARTIST,//歌曲的演唱者
//        };
//        Cursor cursor = contentResolver.query(uri, objs, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                MediaItem mediaItem = new MediaItem();
//                mediaItemList.add(mediaItem);
//                mediaItem.setName(cursor.getString(0));
//                mediaItem.setDuration(cursor.getLong(1));
//                mediaItem.setSize(cursor.getLong(2));
//                mediaItem.setData(cursor.getString(3));
//                mediaItem.setArtist(cursor.getString(4));
//            }
//            cursor.close();
//        }
//    }

    public interface videoDataInterface {
        void getDataList(ArrayList<MediaItem> mediaItemList);
    }

    private void travelPath(File root, int level) {
        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    MediaItem mediaItem = null;
                    if (MimeTypes.isVideo(file)) {
                        mediaItem = new MediaItem();
                        mediaItem.setName(file.getName());
                        mediaItem.setData(file.getPath());
                        mediaItem.setSize(file.length());
                        mediaItemList.add(mediaItem);
                    }
                } else {
                    if (level < SCAN_LEVEL) {
                        travelPath(file, level + 1);
                    }
                }
            }
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mVideoDataInterface != null) {
                mVideoDataInterface.getDataList(mediaItemList);
            }
        }
    };
}
