package com.blue.mediaplayer.mvp.view;

import com.blue.mediaplayer.bean.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyatong on 2018/4/2.
 */

public interface VideoView {
    void videoList(ArrayList<MediaItem> mediaItemList);
}
