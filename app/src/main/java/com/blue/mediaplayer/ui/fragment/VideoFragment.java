package com.blue.mediaplayer.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.adapter.MVideoRecyclerAdapter;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.mvp.persenter.VideoPresenter;
import com.blue.mediaplayer.mvp.view.VideoView;
import com.blue.mediaplayer.ui.activity.VideoPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoFragment extends Fragment implements VideoView {
    private RecyclerView mRecyclerView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;


    private Context mContext;
    private ArrayList<MediaItem> mediaItemList;
    private VideoPresenter videoPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        videoPresenter = new VideoPresenter(mContext);
        videoPresenter.bindView(VideoFragment.this);
        videoPresenter.getVidoList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mRecyclerView = view.findViewById(R.id.video_recycler);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        return view;
    }

    @Override
    public void videoList(ArrayList<MediaItem> mediaItemList) {
        if (mediaItemList != null && mediaItemList.size() > 0) {
            this.mediaItemList = mediaItemList;
            //设置适配器
            MVideoRecyclerAdapter mVideoRecyclerAdapter = new MVideoRecyclerAdapter(mContext, mediaItemList);
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

    class recyclerClickListener implements MVideoRecyclerAdapter.onMyClickListener {
        @Override
        public void onItemClick(View view, int position) {
            //Toast.makeText(mContext, "点击" + position, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(mContext, VideoPlayActivity.class);
//            String dataPath = mediaItemList.get(position).getData();
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.parse(dataPath), "video/*");
//            mContext.startActivity(intent);
            //传递列表数据-序列化
            Intent intent = new Intent(mContext, VideoPlayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItemList);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        if (videoPresenter != null) {
            videoPresenter.detechView();
        }
        super.onDestroy();
    }
}
