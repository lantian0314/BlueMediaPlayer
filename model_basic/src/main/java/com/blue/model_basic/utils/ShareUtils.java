package com.blue.model_basic.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtils {
    private Context mContext;
    private volatile static ShareUtils shareUtils;
    private SharedPreferences sharedPreferences;

    private ShareUtils(Context context) {
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName() + "s", context.MODE_PRIVATE);
    }

    public static ShareUtils getInstance(Context context) {
        if (shareUtils == null) {
            synchronized (ShareUtils.class) {
                if (shareUtils == null) {
                    shareUtils = new ShareUtils(context);
                }
            }
        }
        return shareUtils;
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }
}
