package com.blue.mediaplayer.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.vov.vitamio.widget.VideoView;

public class VitamioVideoView extends VideoView {
    /**
     * 代码实例化对象
     *
     * @param context
     */
    public VitamioVideoView(Context context) {
        this(context, null);
    }

    /**
     * 在布局文件使用该对象
     *
     * @param context
     * @param attrs
     */
    public VitamioVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 定义样式
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public VitamioVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setVideoSize(int videoWidth, int videoHeigtt) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = videoWidth;
        params.height = videoHeigtt;
        setLayoutParams(params);
    }
}
