package com.blue.mediaplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.model_basic.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xingyatong on 2018/4/2.
 */

public class MVideoRecyclerAdapter extends RecyclerView.Adapter<MVideoRecyclerAdapter.ListViewHolder> {

    private Context mContext;
    private List<MediaItem> mediaItemList;
    private Utils utils;
    private boolean isVideo = false;

    private onMyClickListener onMyClickListener;

    public MVideoRecyclerAdapter(Context context, List<MediaItem> mediaItemList, boolean isVideo) {
        this.mContext = context;
        this.mediaItemList = mediaItemList;
        this.isVideo = isVideo;
        utils = new Utils();
    }

    @Override
    public MVideoRecyclerAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_video_pager, null);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(final MVideoRecyclerAdapter.ListViewHolder holder, final int position) {
        MediaItem mediaItem = mediaItemList.get(position);
        holder.setData(mediaItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMyClickListener != null) {
                    onMyClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaItemList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView iv_icon;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_size)
        TextView tv_size;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(MediaItem mediaItem) {
            tv_name.setText(mediaItem.getName());
            tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
            tv_size.setText(Formatter.formatFileSize(mContext, mediaItem.getSize()));
            if (!isVideo) {
                iv_icon.setImageResource(R.drawable.music_default_bg);
            }
            if (!TextUtils.isEmpty(mediaItem.getData())) {
                Bitmap bitmap = getVideoThumbnail(mediaItem.getData(), 60, 60, MediaStore.Images.Thumbnails.MICRO_KIND);
                iv_icon.setImageBitmap(bitmap);
            }
        }
    }

    public interface onMyClickListener {
        void onItemClick(View view, int position);
    }

    public void setMyClickListener(onMyClickListener onMyClickListener) {
        if (onMyClickListener != null) {
            this.onMyClickListener = onMyClickListener;
        }
    }


    /**
     * 获取视频文件的缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        try {
            // 获取视频的缩略图
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.video_default_icon);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }
}
