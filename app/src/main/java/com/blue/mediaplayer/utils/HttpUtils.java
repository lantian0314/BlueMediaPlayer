package com.blue.mediaplayer.utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtils {

    public static void getRequest(String requestUrl, Callback callback) {
        try {
            //得到OkHttpClient对象
            OkHttpClient mOkHttpClient = new OkHttpClient();
            //构造Request对象
            Request.Builder builder = new Request.Builder();
            Request request = builder.get().url(requestUrl).build();
            //将Request封装为Call
            Call mCall = mOkHttpClient.newCall(request);
            //开始执行Call
            //Response mResponse= mCall.execute();//同步方法
            mCall.enqueue(callback);
        } catch (Exception e) {

        }
    }
}
