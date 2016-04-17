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
 * 我的游记列表
 * Created by 伟 on 2016/3/18.
 */
public class TravelFragment extends Fragment {

    private static final String TAG = TravelFragment.class.getSimpleName();

    private View mView;
    @Bind(R.id.refresh)
    public SwipeRefreshLayout travelRefresh;
    @Bind(R.id.recycler_view)
    public RecyclerView travelRecycler;
    private TravelAdapter mAdapter;
    private List<TravelBean> travelBeans = new ArrayList<>();
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;
    private int size = 0;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_recycler, container, false);
            ButterKnife.bind(this, mView);
            mAdapter = new TravelAdapter();
            mManager = new LinearLayoutManager(getActivity());
            travelRecycler.setLayoutManager(mManager);
            travelRecycler.setAdapter(mAdapter);
            travelRecycler.setItemAnimator(new DefaultItemAnimator());
            travelRecycler.addItemDecoration(new DividerItemDecoration(getContext()));
            initListener();
            travelRefresh.setRefreshing(true);
            getData(1);
        }
        return mView;
    }

    private void initListener() {
        travelRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        Call<JSONObject> call = RetrofitTool.getService().getTravels(page);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (travelRefresh.isRefreshing()) {
                    travelRefresh.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                if (response.code() != 200) {
                    ToastTool.show(getActivity(), response.message());
                    LogTool.e(TAG, "请求出错：" + response.message());
                    return;
                }
                LogTool.i(TAG, response.body().toString());
                JSONObject result = response.body();
                try {
                    if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                        ToastTool.show(getActivity(), result.getString(ConstantTool.MSG));
                        return;
                    }
                    size = result.getInt(ConstantTool.SIZE);
                    List<TravelBean> travels = FormatTool.gson.fromJson(String.valueOf(result.getJSONArray(ConstantTool.DATA)), new TypeToken<List<TravelBean>>(){}.getType());
                    travelBeans.addAll(travels);
                    mAdapter.setTravelBeans(travelBeans);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (travelRefresh.isRefreshing()) {
                    travelRefresh.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                ToastTool.show(getActivity().getApplicationContext(), "服务器出现问题: " + t.getMessage());
            }
        });
    }
}
