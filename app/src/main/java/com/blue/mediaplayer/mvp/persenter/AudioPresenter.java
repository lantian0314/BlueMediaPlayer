package com.blue.mediaplayer.mvp.persenter;

import android.content.Context;

import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.mvp.model.AudioModel;
import com.blue.mediaplayer.mvp.view.VideoView;

import java.util.ArrayList;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class AudioPresenter extends BasePresenter<VideoView> {

    private AudioModel AudioModel;
    private VideoView videoView;
    private Context mContext;


    public AudioPresenter(Context context) {
        this.mContext = context;
        AudioModel = new AudioModel(mContext);
    }

    public void getVidoList() {
        videoView = getView();
        AudioModel.getAudioList(new AudioModel.AudioDataInterface() {
            @Override
            public void getDataList(ArrayList<MediaItem> mediaItemList) {
                if (videoView != null) {
                    videoView.videoList(mediaItemList);
                }
            }
        });
    }

}
