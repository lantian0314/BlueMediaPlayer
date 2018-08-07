package com.blue.mediaplayer.ui.fragment.first;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.blue.mediaplayer.bean.MessageEvent;
import com.blue.mediaplayer.mvp.persenter.VideoPresenter;
import com.blue.mediaplayer.mvp.view.VideoView;
import com.blue.mediaplayer.ui.activity.VideoPlayActivity;
import com.blue.model_basic.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class VideoFragment extends BaseMainFragment implements VideoView {
    @BindView(R.id.video_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_nomedia)
    TextView tv_nomedia;
    @BindView(R.id.pb_loading)
    ProgressBar pb_loading;
    boolean isShowlocalView = true;

    private Activity mContext;
    private ArrayList<MediaItem> mediaItemList;
    private VideoPresenter videoPresenter;
    private MVideoRecyclerAdapter mVideoRecyclerAdapter;

    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        videoPresenter = new VideoPresenter(mContext);
        videoPresenter.bindView(VideoFragment.this);
        videoPresenter.getVidoList();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fl_localvideo, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void videoList(ArrayList<MediaItem> mediaList) {
        if (mediaList != null && mediaList.size() > 0) {
            this.mediaItemList = mediaList;
            Collections.sort(mediaItemList);
            getmVideoRecyclerAdapter(mediaItemList);
            mVideoRecyclerAdapter.notifyDataSetChanged();
            //把文本隐藏
            tv_nomedia.setVisibility(View.GONE);
        } else {
            //没有数据文本显示
            tv_nomedia.setVisibility(View.VISIBLE);
        }
        //ProgressBar隐藏
        pb_loading.setVisibility(View.GONE);
    }


    private void getmVideoRecyclerAdapter(ArrayList<MediaItem> mediaItemList) {
        try {
            //设置适配器
            mVideoRecyclerAdapter = new MVideoRecyclerAdapter(mContext, mediaItemList, true);
            //设置监听
            mVideoRecyclerAdapter.setMyClickListener(new recyclerClickListener());
            mRecyclerView.setAdapter(mVideoRecyclerAdapter);
            //布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            //设置布局管理器
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void netvideoList(ArrayList<MediaItem> mediaItemList) {
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
            bundle.putSerializable("videolist", mediaItemList);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            mContext.startActivityForResult(intent, 1);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            openDialog(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            mVideoRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateList(MessageEvent messageEvent) {
        int position = messageEvent.getPosition();
        long duration = messageEvent.getDuration();
        mediaItemList.get(position).setDuration(duration);
        mVideoRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        if (videoPresenter != null) {
            videoPresenter.detechView();
        }
        super.onDestroy();
    }

    public void openDialog(final int position) {
        try {
            final MediaItem mediaItem = mediaItemList.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("删除文件");
            builder.setMessage("你确定要删除文件 :" + mediaItem.getName() + " 吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String path = mediaItem.getData();
                    deleteFile(path);
                    deleteFile(mediaItem.getImageUrl());//删除icon
                    mediaItemList.remove(position);
                    mVideoRecyclerAdapter.notifyDataSetChanged();
                    videoPresenter.deleteDbVideo(path);//删除数据库文件
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);//对话框和返回键都不起作用
            alertDialog.show();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    private void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


}
