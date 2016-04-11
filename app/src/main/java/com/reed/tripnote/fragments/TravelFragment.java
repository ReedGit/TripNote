package com.reed.tripnote.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reed.tripnote.R;
import com.reed.tripnote.ViewHolders.FootViewHolder;
import com.reed.tripnote.activities.ContentActivity;
import com.reed.tripnote.adapters.TravelAdapter;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.data.TravelData;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.views.DividerItemDecoration;

import java.util.List;

/**
 * 我的游记列表
 * Created by 伟 on 2016/3/18.
 */
public class TravelFragment extends Fragment {

    private View mView;
    private SwipeRefreshLayout travelRefresh;
    private RecyclerView travelRecycler;
    private TravelAdapter mAdapter;
    private List<TravelBean> travelBeans;
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_recycler, container, false);
            travelRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.refresh);
            travelRecycler = (RecyclerView) mView.findViewById(R.id.recycler_view);
            mAdapter = new TravelAdapter();
            mManager = new LinearLayoutManager(getActivity());
            travelRecycler.setLayoutManager(mManager);
            travelRecycler.setAdapter(mAdapter);
            travelRecycler.setItemAnimator(new DefaultItemAnimator());
            travelRecycler.addItemDecoration(new DividerItemDecoration(getContext()));
            travelBeans = TravelData.getInstance().getTravels();
            mAdapter.setTravelBeans(travelBeans);
            mAdapter.notifyDataSetChanged();
            initListener();
        }
        return mView;
    }

    private void initListener() {
        travelRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                travelBeans.clear();
                travelBeans = TravelData.getInstance().getTravels();
                mAdapter.setTravelBeans(travelBeans);
                mAdapter.notifyDataSetChanged();
                travelRefresh.setRefreshing(false);
            }
        });
        mAdapter.setOnItemClickListener(new TravelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantTool.TRAVEL, travelBeans.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new TravelAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认删除“" + travelBeans.get(position).getTitle() + "”？")
                        .setCancelable(true)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });
        travelRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
