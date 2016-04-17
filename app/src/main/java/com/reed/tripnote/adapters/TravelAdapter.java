package com.reed.tripnote.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reed.tripnote.R;
import com.reed.tripnote.ViewHolders.FootViewHolder;
import com.reed.tripnote.ViewHolders.TravelViewHolder;
import com.reed.tripnote.beans.TravelBean;

import java.util.List;

import butterknife.OnItemClick;

/**
 * 游记Adapter
 * Created by 伟 on 2016/2/14.
 */
public class TravelAdapter extends RecyclerView.Adapter {

    private List<TravelBean> travelBeans;

    private int TYPE_FOOT = 0;
    private int TYPE_NORMAL = 1;
    public static final int NO_DATA = 0;
    public static final int LOADING_DATA = 1;
    public static final int FINISH_DATA = 2;
    private boolean isAll = false;

    private boolean isLoading = false;
    private Context context;

    public void setIsAll(boolean isAll) {
        this.isAll = isAll;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setTravelBeans(List<TravelBean> travelBeans) {
        this.travelBeans = travelBeans;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_NORMAL) {
            return new TravelViewHolder(inflater.inflate(R.layout.item_travel, parent, false));
        } else if (viewType == TYPE_FOOT) {
            return new FootViewHolder(inflater.inflate(R.layout.item_more, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (travelBeans == null || (isAll && position == travelBeans.size())) {
            ((FootViewHolder) holder).bindData(NO_DATA);
            return;
        }
        if (isLoading && position == travelBeans.size()) {
            ((FootViewHolder) holder).bindData(LOADING_DATA);
            return;
        }
        if (position == travelBeans.size()) {
            ((FootViewHolder) holder).bindData(FINISH_DATA);
            return;
        }
        ((TravelViewHolder) holder).bindData(context, travelBeans.get(position));
        if (onItemClickListener != null) {
            ((TravelViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(((TravelViewHolder) holder).itemView, holder.getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            ((TravelViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(((TravelViewHolder) holder).itemView, holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return travelBeans == null ? 1 : travelBeans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (travelBeans == null || position == travelBeans.size()) {
            return TYPE_FOOT;
        }
        return TYPE_NORMAL;
    }
}
