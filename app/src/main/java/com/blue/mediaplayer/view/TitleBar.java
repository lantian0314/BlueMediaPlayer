package com.blue.mediaplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blue.mediaplayer.R;

/**
 * Created by xingyatong on 2018/4/3.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener{
    private Context mContext;

    private View tv_search;
    private View rl_game;
    private View iv_record;
    /**
     * 在代码中实例化该类的时候使用
     *
     * @param context
     */
    public TitleBar(Context context) {
        this(context, null);
    }

    /**
     * 在布局文件中使用该类的时候,Android系统通过这个构造实例
     *
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当需要设置样式的时候，使用此方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * 当布局文件加载完成回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_search=getChildAt(1);
        rl_game=getChildAt(2);
        iv_record=getChildAt(3);
        //设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(mContext, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(mContext, "播放历史", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
