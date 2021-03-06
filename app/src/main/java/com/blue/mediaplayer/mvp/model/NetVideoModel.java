package com.blue.mediaplayer.mvp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.bean.MessageEvent;
import com.blue.mediaplayer.bean.NetMediaItem;
import com.blue.mediaplayer.utils.Constants;
import com.blue.mediaplayer.utils.HttpUtils;
import com.blue.model_basic.utils.LogUtil;
import com.blue.model_basic.utils.ShareUtils;
import com.google.gson.Gson;

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
    private netVideoDataInterface mVideoDataInterface = null;

    public void getNetVideoList(final Context context, netVideoDataInterface netVideoDataInterface) {
        this.mVideoDataInterface = netVideoDataInterface;
        String result = ShareUtils.getInstance(context).getString(Constants.NET_URL);
        if (!TextUtils.isEmpty(result)) {
            parserJson(result);
        }
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
                                    ShareUtils.getInstance(context).putString(Constants.NET_URL, result);
                                    parserJson(result);
                                }
                            } catch (Exception e) {
                                LogUtil.e(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    LogUtil.e(e);
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
            if (TextUtils.isEmpty(result)) {
                return;
            }
            JSONObject jsonObject = new JSONObject(result);
            JSONArray tempArray = jsonObject.optJSONArray("trailers");
            if (tempArray != null && tempArray.length() > 0) {
                mediaItemList = new ArrayList<>();
                Gson gson = new Gson();
                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject tempJson = tempArray.optJSONObject(i);
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
            mHandler.sendEmptyMessage(10);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    public interface netVideoDataInterface {
        void getDataList(ArrayList<MediaItem> mediaItemList);
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
