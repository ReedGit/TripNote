package com.reed.tripnote.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.reed.tripnote.R;
import com.reed.tripnote.ViewHolders.FootViewHolder;
import com.reed.tripnote.ViewHolders.HomeViewHolder;
import com.reed.tripnote.beans.TravelBean;

import java.util.List;

/**
 * 首页游记Adapter
 * Created by 伟 on 2016/2/14.
 */
public class HomeAdapter extends RecyclerView.Adapter {

    private List<TravelBean> travelBeans;

    private int TYPE_FOOT = 0;
    private int TYPE_NORMAL = 1;

    public void setTravelBeans(List<TravelBean> travelBeans) {
        this.travelBeans = travelBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_NORMAL) {
            return new HomeViewHolder(inflater.inflate(R.layout.home_item, parent, false));
        } else {
            return new FootViewHolder(inflater.inflate(R.layout.more_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == travelBeans.size()) {
            return;
        }
        ((HomeViewHolder) holder).bindData(travelBeans.get(position));
    }

    @Override
    public int getItemCount() {
        return travelBeans == null ? 0 : travelBeans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == travelBeans.size()) {
            return TYPE_FOOT;
        }
        return TYPE_NORMAL;
    }
}
