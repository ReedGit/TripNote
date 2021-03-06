package com.reed.tripnote.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.reed.tripnote.R;
import com.reed.tripnote.activities.ContentActivity;
import com.reed.tripnote.adapters.TravelAdapter;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;
import com.reed.tripnote.views.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页fragment
 * Created by 伟 on 2016/2/12.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private View mView;
    @Bind(R.id.refresh)
    public SwipeRefreshLayout homeSRL;
    @Bind(R.id.recycler_view)
    public RecyclerView homeRecycler;
    private TravelAdapter mAdapter;
    private List<TravelBean> travelBeans = new ArrayList<>();
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;
    private int size = 0;
    private int page = 1;
    private Call<JSONObject> call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_recycler, container, false);
            ButterKnife.bind(this, mView);
            mAdapter = new TravelAdapter();
            mManager = new LinearLayoutManager(getActivity());
            mAdapter.setTravelBeans(travelBeans);
            homeRecycler.setLayoutManager(mManager);
            homeRecycler.setAdapter(mAdapter);
            homeRecycler.setItemAnimator(new DefaultItemAnimator());
            homeRecycler.addItemDecoration(new DividerItemDecoration(getActivity()));
            homeSRL.setRefreshing(true);
            getData(1);
            initListener();
        }
        return mView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }

    private void initListener() {
        homeSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                travelBeans.clear();
                mAdapter.setIsLoading(false);
                mAdapter.setIsAll(false);
                page = 1;
                getData(1);
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
        homeRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemLastIndex = mAdapter.getItemCount() - 1;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemLastIndex) {
                    if (travelBeans.size() < size){
                        page++;
                        mAdapter.setIsLoading(true);
                        mAdapter.notifyDataSetChanged();
                        getData(page);
                    } else {
                        mAdapter.setIsAll(true);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleLastIndex = mManager.findLastCompletelyVisibleItemPosition();
            }
        });
    }

    private void getData(int page) {
        call = RetrofitTool.getService().getTravels(page);
        if (call.isExecuted()) {
            call.cancel();
        }
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (homeSRL.isRefreshing()) {
                    homeSRL.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                if (response.code() != 200) {
                    ToastTool.show(response.message());
                    LogTool.e(TAG, "请求出错：" + response.message());
                    return;
                }
                LogTool.i(TAG, response.body().toString());
                JSONObject result = response.body();
                try {
                    if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                        ToastTool.show(result.getString(ConstantTool.MSG));
                        return;
                    }
                    size = result.getInt(ConstantTool.SIZE);
                    List<TravelBean> travels = FormatTool.gson.fromJson(String.valueOf(result.getJSONArray(ConstantTool.DATA)), new TypeToken<List<TravelBean>>(){}.getType());
                    travelBeans.addAll(travelBeans.size(), travels);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (homeSRL.isRefreshing()) {
                    homeSRL.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                LogTool.i(TAG, "服务器出现问题: " + t.getMessage());
                if (!call.isCanceled()) {
                    ToastTool.show("服务器出现问题: " + t.getMessage());
                }
            }
        });
    }
}
