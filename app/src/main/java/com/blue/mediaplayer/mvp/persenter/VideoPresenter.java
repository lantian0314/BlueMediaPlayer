package com.blue.mediaplayer.mvp.persenter;

import android.content.Context;

import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.mvp.model.VideoModel;
import com.blue.mediaplayer.mvp.view.VideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoPresenter extends BasePresenter<VideoView> {

    private VideoModel videoModel;
    private VideoView videoView;
    private Context mContext;

    public VideoPresenter(Context context) {
        this.mContext = context;
        videoModel = new VideoModel(mContext);
    }

    public void getVidoList() {
        videoView=getView();
        videoModel.getVideoList(new VideoModel.videoDataInterface() {
            @Override
            public void getDataList(ArrayList<MediaItem> mediaItemList) {
                if (videoView!=null){
                    videoView.videoList(mediaItemList);
                }
            }
        });
    }
}
