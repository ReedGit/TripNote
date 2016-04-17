package com.reed.tripnote.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.reed.tripnote.R;
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

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = SearchResultActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    public Toolbar searchToolbar;
    @Bind(R.id.rcv_search_travel)
    public RecyclerView searchRCV;

    private TravelAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int visibleLastIndex = 0;
    private String query;
    private List<TravelBean> travels = new ArrayList<>();

    private int size = 0;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initView();
        initListener();
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            query = getIntent().getStringExtra(SearchManager.QUERY);
            setTitle(getString(R.string.search) + "：" + query);
        }
        getData(1);
    }

    private void initView() {
        searchToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(searchToolbar);
        mAdapter = new TravelAdapter();
        mManager = new LinearLayoutManager(this);
        searchRCV.setLayoutManager(mManager);
        searchRCV.setAdapter(mAdapter);
        searchRCV.setItemAnimator(new DefaultItemAnimator());
        searchRCV.addItemDecoration(new DividerItemDecoration(this));
        mAdapter.setIsLoading(true);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchRCV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemLastIndex = mAdapter.getItemCount() - 1;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemLastIndex) {

                    if (travels.size() < size) {
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
        Call<JSONObject> call = RetrofitTool.getService().searchTravel(query, page);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                if (response.code() != 200) {
                    ToastTool.show(SearchResultActivity.this, response.message());
                    LogTool.e(TAG, "请求出错：" + response.message());
                    return;
                }
                LogTool.i(TAG, response.body().toString());
                JSONObject result = response.body();
                try {
                    if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                        ToastTool.show(SearchResultActivity.this, result.getString(ConstantTool.MSG));
                        return;
                    }
                    size = result.getInt(ConstantTool.SIZE);
                    List<TravelBean> travelBeans = FormatTool.gson.fromJson(String.valueOf(result.getJSONArray(ConstantTool.DATA)), new TypeToken<List<TravelBean>>() {
                    }.getType());
                    travels.addAll(travelBeans);
                    mAdapter.setTravelBeans(travelBeans);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                ToastTool.show(SearchResultActivity.this, "服务器出现问题: " + t.getMessage());
            }
        });
    }
}
