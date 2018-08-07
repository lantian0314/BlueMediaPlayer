package com.blue.mediaplayer.database;

import com.blue.mediaplayer.bean.MediaItem;

import java.util.List;

/**
 * Created by xingyatong on 2018/8/7 13:48
 * Describe sdcard存储数据的数据库
 */
public class MediaItemDatabase {
    /**
     * 删除本地数据库存储的文件
     *
     * @param path
     */
    public static void deleteDbVideo(String path) {
        List<MediaItem> deleteList = MediaItem.find(MediaItem.class, "data=?", path);
        if (deleteList != null && deleteList.size() > 0) {
            for (int i = 0; i < deleteList.size(); i++) {
                deleteList.get(i).delete();
            }
        }
    }

    /**
     * 更新本地数据库存储的文件
     *
     * @param path
     */
    public static void updateVideDuration(String path, long duration) {
        List<MediaItem> updateList = MediaItem.find(MediaItem.class, "data=?", path);
        if (updateList != null && updateList.size() > 0) {
            for (int i = 0; i < updateList.size(); i++) {
                MediaItem mediaItem = updateList.get(i);
                mediaItem.setDuration(duration);
                mediaItem.save();
            }
        }
    }

    /**
     * 更新视频的播放总时长
     *
     * @param mediaItem
     */
    public static void setItemDuration(MediaItem mediaItem) {
        List<MediaItem> updateList = MediaItem.find(MediaItem.class, "data=?", mediaItem.getData());
        if (updateList != null && updateList.size() > 0) {
            for (int i = 0; i < updateList.size(); i++) {
                MediaItem tempMedia = updateList.get(i);
                mediaItem.setDuration(tempMedia.getDuration());
            }
        }
    }

    public static List<MediaItem> getAllItem() {
        List<MediaItem> mediaItemList = MediaItem.listAll(MediaItem.class);
        if (mediaItemList != null && mediaItemList.size() > 0) {
            return mediaItemList;
        }
        return null;
    }
}
