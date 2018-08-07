package com.blue.mediaplayer.mvp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.database.MediaItemDatabase;
import com.blue.mediaplayer.utils.FileUtils;
import com.blue.mediaplayer.utils.MimeTypes;
import com.blue.model_basic.utils.LogUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoModel {
    private ArrayList<MediaItem> mediaItemList = null;
    private ArrayList<MediaItem> localmediaItemList = null;
    private Context mContext;
    private videoDataInterface mVideoDataInterface;

    private final int SCAN_LEVEL = 8;//扫描文件的深度
    private boolean isScanData = false;


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
                localmediaItemList = new ArrayList<>();
                querlMediaDb();
                File externalDir = Environment.getExternalStorageDirectory();
                if (externalDir != null) {
                    travelPath(externalDir, 0);
                    mHandler.sendEmptyMessage(MSG_CURRENTMEDIA);
                }
                //queryDb();
            }

        }.start();
    }

    private void querlMediaDb() {
        List<MediaItem> mediaItemList = MediaItemDatabase.getAllItem();
        if (mediaItemList != null && mediaItemList.size() > 0) {
            isScanData = true;
            localmediaItemList.addAll(mediaItemList);
            mHandler.sendEmptyMessage(MSG_LOCALDB);
        } else {
            isScanData = false;
        }

    }

    public interface videoDataInterface {
        void getData(MediaItem mediaItem);

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
                    if (MimeTypes.isVideo(file)) {
                        MediaItem mediaItem = new MediaItem();
                        mediaItem.setName(file.getName());
                        mediaItem.setData(file.getPath());
                        mediaItem.setSize(file.length());
                        mediaItem.setLastModifiedTime(file.lastModified());
                        updateVideoIcon(file, mediaItem);
                        MediaItemDatabase.setItemDuration(mediaItem);
                        MediaItem.save(mediaItem);
                        updateMediaItem(mediaItem);
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

    private void updateMediaItem(MediaItem mediaItem) {
        if (!isScanData) {
            Message message = new Message();
            message.what = MSG_CURRENTSINGLEMEDIA;
            message.obj = mediaItem;
            mHandler.sendMessage(message);
        }
    }

    /**
     * 更新视频的ICon
     *
     * @param file
     * @param mediaItem
     */
    private void updateVideoIcon(File file, MediaItem mediaItem) {
        try {
            String path = file.getPath();
            Bitmap bitmap = FileUtils.getVideoThumbnail(mContext, mediaItem.getData(), 60, 60, MediaStore.Images.Thumbnails.MICRO_KIND);
            if (bitmap != null) {
                int index = path.lastIndexOf("/");
                String savePath = FileUtils.getSdcardRootPath() + File.separator + "Image_Icon";
                String saveName = path.substring(index + 1, path.length()) + ".jpg";
                mediaItem.setImageUrl(savePath + File.separator + saveName);
                FileUtils.saveBitmap(savePath, saveName, bitmap);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    private static final int MSG_LOCALDB = 10;
    private static final int MSG_CURRENTMEDIA = 20;//当前扫描出来的
    private static final int MSG_CURRENTSINGLEMEDIA = 30;//当前单个文件
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCALDB:
                    if (mVideoDataInterface != null) {
                        mVideoDataInterface.getDataList(localmediaItemList);
                    }
                    break;
                case MSG_CURRENTMEDIA:
                    if (mVideoDataInterface != null) {
                        mVideoDataInterface.getDataList(mediaItemList);
                    }
                    break;
                case MSG_CURRENTSINGLEMEDIA:
                    if (mVideoDataInterface != null) {
                        MediaItem mediaItem = (MediaItem) msg.obj;
                        mVideoDataInterface.getData(mediaItem);
                    }
                    break;
            }
        }
    };

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
}
