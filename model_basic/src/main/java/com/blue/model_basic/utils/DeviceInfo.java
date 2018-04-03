package com.blue.model_basic.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by xingyatong on 2018/4/3.
 */

public class DeviceInfo {
    private Context mContext;

    public DeviceInfo(Context context) {
        this.mContext = context;
    }

    /**
     * 得到屏幕的分辨率
     *
     * @return
     */
    public int[] getDisplayMetrics() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        // 获取分辨率宽度
        int screenW = metrics.widthPixels;
        int screenH = metrics.heightPixels;
        return new int[]{screenW, screenH};
    }
}
