package com.blue.mediaplayer.ui.fragment.first;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.adapter.MNetVideoRecyclerAdapter;
import com.blue.mediaplayer.adapter.MVideoRecyclerAdapter;
import com.blue.mediaplayer.base.BaseMainFragment;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.mvp.persenter.VideoPresenter;
import com.blue.mediaplayer.mvp.view.VideoView;
import com.blue.mediaplayer.ui.activity.VideoPlayActivity;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class NetVideoFragment extends BaseMainFragment implements VideoView {
    @BindView(R.id.net_video_recycler)
    RecyclerView mNetRecyclerView;
    @BindView(R.id.tv_nonetmedia)
    TextView tv_netmedia;
    @BindView(R.id.pb_netloading)
    ProgressBar pb_netloading;

    private Activity mContext;
    private ArrayList<MediaItem> NetmediaItemList;
    private VideoPresenter videoPresenter;
    private MVideoRecyclerAdapter mVideoRecyclerAdapter;

    public static NetVideoFragment newInstance() {
        Bundle args = new Bundle();
        NetVideoFragment fragment = new NetVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        videoPresenter = new VideoPresenter(mContext);
        videoPresenter.bindView(NetVideoFragment.this);
        videoPresenter.getNetVideoList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fl_netvideo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void videoData(MediaItem mediaItem) {

    }

    @Override
    public void videoList(ArrayList<MediaItem> mediaList) {
    }

    @Override
    public void netvideoList(ArrayList<MediaItem> mediaItemList) {
        if (mediaItemList != null && mediaItemList.size() > 0) {
            this.NetmediaItemList = mediaItemList;
            //设置适配器
            MNetVideoRecyclerAdapter MNetVideoRecyclerAdapter = new MNetVideoRecyclerAdapter(mContext, NetmediaItemList);
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
            , MNetVideoRecyclerAdapter.onMyClickListener {
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
                bundle.putSerializable("videolist", NetmediaItemList);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            mContext.startActivityForResult(intent, 1);
        }

        @Override
        public void onItemLongClick(View view, int position) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mVideoRecyclerAdapter.notifyDataSetChanged();
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
