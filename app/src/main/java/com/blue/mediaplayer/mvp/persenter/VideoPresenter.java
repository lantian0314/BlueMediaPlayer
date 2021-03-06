package com.blue.mediaplayer.mvp.persenter;

import android.content.Context;

import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.database.MediaItemDatabase;
import com.blue.mediaplayer.mvp.model.NetVideoModel;
import com.blue.mediaplayer.mvp.model.VideoModel;
import com.blue.mediaplayer.mvp.view.VideoView;

import java.util.ArrayList;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoPresenter extends BasePresenter<VideoView> {

    private VideoModel videoModel;
    private VideoView videoView;
    private Context mContext;

    private NetVideoModel netVideoModel;

    public VideoPresenter(Context context) {
        this.mContext = context;
        videoModel = new VideoModel(mContext);
        netVideoModel = new NetVideoModel();
    }

    public void getVidoList() {
        videoView = getView();
        videoModel.getVideoList(new VideoModel.videoDataInterface() {
            @Override
            public void getData(MediaItem mediaItem) {
                if (videoView != null) {
                    videoView.videoData(mediaItem);
                }
            }

            @Override
            public void getDataList(ArrayList<MediaItem> mediaItemList) {
                if (videoView != null) {
                    videoView.videoList(mediaItemList);
                }
            }
        });
    }

    public void getNetVideoList() {
        videoView = getView();
        netVideoModel.getNetVideoList(mContext, new NetVideoModel.netVideoDataInterface() {
            @Override
            public void getDataList(ArrayList<MediaItem> mediaItemList) {
                if (videoView != null) {
                    videoView.netvideoList(mediaItemList);
                }
            }
        });
    }

    public void deleteDbVideo(String path) {
        MediaItemDatabase.deleteDbVideo(path);
    }
}
