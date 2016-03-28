package com.reed.tripnote.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.reed.tripnote.R;
import com.reed.tripnote.ViewHolders.CommentViewHolder;
import com.reed.tripnote.ViewHolders.FootViewHolder;
import com.reed.tripnote.beans.CommentBean;

import java.util.List;

/**
 * 评论的适配器
 * Created by 伟 on 2016/3/28.
 */
public class CommentAdapter extends RecyclerView.Adapter {

    private List<CommentBean> comments;

    private int TYPE_FOOT = 0;
    private int TYPE_NORMAL = 1;
    public static final int NO_DATA = 0;
    public static final int LOADING_DATA = 1;
    public static final int FINISH_DATA = 2;
    private boolean isAll = false;

    private Context context;

    private boolean isLoading = false;

    public void setIsAll(boolean isAll) {
        this.isAll = isAll;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_NORMAL) {
            return new CommentViewHolder(inflater.inflate(R.layout.item_travel, parent, false));
        } else if (viewType == TYPE_FOOT) {
            return new FootViewHolder(inflater.inflate(R.layout.item_more, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (comments == null || isAll) {
            ((FootViewHolder) holder).bindData(NO_DATA);
            return;
        }
        if (isLoading) {
            ((FootViewHolder) holder).bindData(LOADING_DATA);
            return;
        }
        if (position == comments.size()) {
            ((FootViewHolder) holder).bindData(FINISH_DATA);
            return;
        }
        ((CommentViewHolder) holder).bindData(context, comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 1 : comments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (comments == null || position == comments.size()) {
            return TYPE_FOOT;
        }
        return TYPE_NORMAL;
    }
}
