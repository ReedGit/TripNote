package com.reed.tripnote.adapters;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_NORMAL) {
            return new TravelViewHolder(inflater.inflate(R.layout.item_travel, parent, false));
        } else if (viewType == TYPE_FOOT){
            return new FootViewHolder(inflater.inflate(R.layout.item_more, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (travelBeans == null || isAll) {
            ((FootViewHolder) holder).bindData(NO_DATA);
            return;
        }
        if (isLoading) {
            ((FootViewHolder) holder).bindData(LOADING_DATA);
            return;
        }
        if (position == travelBeans.size()) {
            ((FootViewHolder) holder).bindData(FINISH_DATA);
            return;
        }
        ((TravelViewHolder) holder).bindData(travelBeans.get(position));
        if (onItemClickListener != null) {
            ((TravelViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(((TravelViewHolder) holder).itemView, position);
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
