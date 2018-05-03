package com.blue.mediaplayer.mvp.model;

import android.text.TextUtils;

import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.bean.MessageEvent;
import com.blue.mediaplayer.bean.NetMediaItem;
import com.blue.mediaplayer.utils.Constants;
import com.blue.mediaplayer.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NetVideoModel {

    private ArrayList<MediaItem> mediaItemList = null;
    private netVideoDataInterface mVideoDataInterface=null;

    public void getNetVideoList(netVideoDataInterface netVideoDataInterface) {
        this.mVideoDataInterface=netVideoDataInterface;
        EventBus.getDefault().register(this);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HttpUtils.getRequest(Constants.NET_URL, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            try {
                                String result = response.body().string();
                                if (!TextUtils.isEmpty(result)) {
                                    parserJson(result);
                                    //回调得到的数据
                                    EventBus.getDefault().post(new MessageEvent());
                                }
                            }catch (Exception e){

                            }
                        }
                    });
                } catch (Exception e) {

                }
            }
        }.start();
    }

    /**
     * 匹配服务器端的数据
     *
     * @param result
     */
    private void parserJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray tempArray = jsonObject.optJSONArray("trailers");
            if (tempArray != null && tempArray.length() > 0) {
                mediaItemList = new ArrayList<>();
                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject tempJson = tempArray.optJSONObject(i);
                    Gson gson = new Gson();
                    NetMediaItem netMediaItem = gson.fromJson(tempJson.toString(), NetMediaItem.class);
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.setData(netMediaItem.getUrl());
                    mediaItem.setName(netMediaItem.getVideoTitle());
                    mediaItem.setDesc(netMediaItem.getSummary());
                    mediaItem.setImageUrl(netMediaItem.getCoverImg());
                    mediaItem.setSize(netMediaItem.getVideoLength());
                    mediaItemList.add(mediaItem);
                }
            }
        } catch (Exception e) {

        }
    }

    public interface netVideoDataInterface {
        void getDataList(ArrayList<MediaItem> mediaItemList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (mVideoDataInterface != null) {
            mVideoDataInterface.getDataList(mediaItemList);
        }
        EventBus.getDefault().unregister(this);
    }
}
