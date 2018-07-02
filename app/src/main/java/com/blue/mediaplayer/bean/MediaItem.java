package com.blue.mediaplayer.bean;


import android.support.annotation.NonNull;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

import java.io.Serializable;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class MediaItem extends SugarRecord implements Serializable, Comparable<MediaItem> {

    private String name;

    private long duration;

    private long size;

    @Column(name = "data", unique = true)
    private String data;

    private String artist;

    private String desc;

    private String imageUrl;

    private long lastModifiedTime;

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int compareTo(@NonNull MediaItem another) {
        if (this.lastModifiedTime > another.lastModifiedTime) {
            return -1;
        } else if (this.lastModifiedTime < another.lastModifiedTime) {
            return 1;
        } else {
            return 0;
        }
    }
}
