package com.blue.mediaplayer.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.adapter.MNetVideoRecyclerAdapter;
import com.blue.mediaplayer.adapter.MVideoRecyclerAdapter;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.mvp.persenter.AudioPresenter;
import com.blue.mediaplayer.mvp.persenter.VideoPresenter;
import com.blue.mediaplayer.mvp.view.VideoView;
import com.blue.mediaplayer.ui.activity.AudioPlayActivity;
import com.blue.mediaplayer.ui.activity.VideoPlayActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class AudioFragment extends Fragment implements VideoView {
    @BindView(R.id.video_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_nomedia)
    TextView tv_nomedia;
    @BindView(R.id.pb_loading)
    ProgressBar pb_loading;
    @BindView(R.id.ll_localvideo)
    FrameLayout ll_localvideo;
    @BindView(R.id.ll_netvideo)
    FrameLayout ll_netvideo;
    @BindView(R.id.btn_localVideo)
    Button btn_localVideo;
    @BindView(R.id.btn_netlocal)
    Button btn_netlocal;
    @BindView(R.id.net_video_recycler)
    RecyclerView mNetRecyclerView;
    @BindView(R.id.tv_nonetmedia)
    TextView tv_netmedia;
    @BindView(R.id.pb_netloading)
    ProgressBar pb_netloading;
    boolean isShowlocalView = true;


    private Context mContext;
    private ArrayList<MediaItem> mediaItemList;
    private AudioPresenter audioPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        audioPresenter = new AudioPresenter(mContext);
        audioPresenter.bindView(AudioFragment.this);
        audioPresenter.getVidoList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        setClickListener();
        return view;
    }

    private void setClickListener() {
        btn_netlocal.setOnClickListener(new MyClickListener());
        btn_localVideo.setOnClickListener(new MyClickListener());
    }

    @Override
    public void videoList(ArrayList<MediaItem> mediaItemList) {
        if (mediaItemList != null && mediaItemList.size() > 0) {
            this.mediaItemList = mediaItemList;
            //设置适配器
            MVideoRecyclerAdapter mVideoRecyclerAdapter = new MVideoRecyclerAdapter(mContext, mediaItemList,false);
            //设置监听
            mVideoRecyclerAdapter.setMyClickListener(new recyclerClickListener());
            mRecyclerView.setAdapter(mVideoRecyclerAdapter);
            //布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            //设置布局管理器
            mRecyclerView.setLayoutManager(linearLayoutManager);
            //把文本隐藏
            tv_nomedia.setVisibility(View.GONE);
        } else {
            //没有数据文本显示
            tv_nomedia.setVisibility(View.VISIBLE);
        }
        //ProgressBar隐藏
        pb_loading.setVisibility(View.GONE);
    }

    @Override
    public void netvideoList(ArrayList<MediaItem> mediaItemList) {
        if (mediaItemList != null && mediaItemList.size() > 0) {
            this.mediaItemList = mediaItemList;
            //设置适配器
            MNetVideoRecyclerAdapter MNetVideoRecyclerAdapter = new MNetVideoRecyclerAdapter(mContext, mediaItemList);
            //设置监听
            MNetVideoRecyclerAdapter.setMyClickListener(new recyclerClickListener());
            mNetRecyclerView.setAdapter(MNetVideoRecyclerAdapter);
            //布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            //设置布局管理器
            mNetRecyclerView.setLayoutManager(linearLayoutManager);
            //把文本隐藏
            tv_netmedia.setVisibility(View.GONE);
        } else {
            //没有数据文本显示
            tv_netmedia.setVisibility(View.VISIBLE);
        }
        //ProgressBar隐藏
        pb_netloading.setVisibility(View.GONE);
    }

    class recyclerClickListener implements MVideoRecyclerAdapter.onMyClickListener
            ,MNetVideoRecyclerAdapter.onMyClickListener {
        @Override
        public void onItemClick(View view, int position) {
            //传递列表数据-序列化
            Intent intent = new Intent(mContext, AudioPlayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }

    @Override
    public void onDestroy() {
        if (audioPresenter != null) {
            audioPresenter.detechView();
        }
        super.onDestroy();
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            isShowlocalView = !isShowlocalView;
            if (isShowlocalView) {
                ll_localvideo.setVisibility(View.VISIBLE);
                ll_netvideo.setVisibility(View.GONE);
                btn_localVideo.setBackgroundColor(Color.parseColor("#ff3097fd"));
                btn_netlocal.setBackgroundColor(Color.parseColor("#11000000"));
            } else {
                ll_localvideo.setVisibility(View.GONE);
                ll_netvideo.setVisibility(View.VISIBLE);
                btn_netlocal.setBackgroundColor(Color.parseColor("#ff3097fd"));
                btn_localVideo.setBackgroundColor(Color.parseColor("#11000000"));
            }
        }
    }
}
