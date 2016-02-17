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
import com.reed.tripnote.adapters.HomeAdapter;
import com.reed.tripnote.beans.TravelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伟 on 2016/2/12.
 */
public class HomeFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout homeSRL;
    private RecyclerView homeRecycler;
    private HomeAdapter mAdapter;
    private List<TravelBean> travelBeans;
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
            homeSRL = (SwipeRefreshLayout) mView.findViewById(R.id.srl_home);
            homeRecycler = (RecyclerView) mView.findViewById(R.id.recyclerView_home);
            travelBeans = new ArrayList<>();
            mAdapter = new HomeAdapter(travelBeans);
            mManager = new LinearLayoutManager(getActivity());
            homeRecycler.setLayoutManager(mManager);
            homeRecycler.setAdapter(mAdapter);
            homeRecycler.setItemAnimator(new DefaultItemAnimator());
            initListener();
        }
        return mView;
    }

    private void initListener() {
        homeSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        homeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemLastIndex = mAdapter.getItemCount() - 1;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemLastIndex) {
                    FootViewHolder.footTV.setText(R.string.loading);
                    FootViewHolder.footPB.setVisibility(View.VISIBLE);
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
