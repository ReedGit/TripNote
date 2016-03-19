package com.reed.tripnote.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reed.tripnote.R;
import com.reed.tripnote.ViewHolders.FootViewHolder;
import com.reed.tripnote.adapters.TravelAdapter;
import com.reed.tripnote.beans.TravelBean;

import java.util.List;

/**
 * 我收藏的游记
 * Created by 伟 on 2016/3/18.
 */
public class CollectionFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout collectionRefresh;
    private RecyclerView collectRecycler;
    private TravelAdapter mAdapter;
    private List<TravelBean> travelBeans;
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_recycler, container, false);
            collectionRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.refresh);
            collectRecycler = (RecyclerView) mView.findViewById(R.id.recycler_view);
            mAdapter = new TravelAdapter();
            mManager = new LinearLayoutManager(getActivity());
            collectRecycler.setLayoutManager(mManager);
            collectRecycler.setAdapter(mAdapter);
            collectRecycler.setItemAnimator(new DefaultItemAnimator());
            initListener();
        }
        return mView;
    }

    private void initListener() {
        collectionRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        collectRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemLastIndex = mAdapter.getItemCount() - 1;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemLastIndex) {
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleLastIndex = mManager.findLastCompletelyVisibleItemPosition();
            }
        });
    }
}
