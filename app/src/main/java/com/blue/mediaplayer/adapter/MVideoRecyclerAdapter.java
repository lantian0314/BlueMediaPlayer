package com.blue.mediaplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.mediaplayer.R;
import com.blue.mediaplayer.bean.MediaItem;
import com.blue.mediaplayer.utils.Utils;

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

    private onMyClickListener onMyClickListener;

    public MVideoRecyclerAdapter(Context context, List<MediaItem> mediaItemList) {
        this.mContext = context;
        this.mediaItemList = mediaItemList;
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
                if (onMyClickListener!=null){
                    onMyClickListener.onItemClick(holder.itemView,position);
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
        }
    }

    public interface onMyClickListener {
        void onItemClick(View view,int position);
    }

    public void setMyClickListener(onMyClickListener onMyClickListener) {
        if (onMyClickListener != null) {
            this.onMyClickListener = onMyClickListener;
        }
    }

}
