package com.reed.tripnote.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class PersonTravelActivity extends AppCompatActivity {

    private static final String TAG = PersonTravelActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.refresh)
    public SwipeRefreshLayout mSrf;
    @Bind(R.id.recycler_view)
    public RecyclerView mRecycler;

    private TravelAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int visibleLastIndex = 0;
    private List<TravelBean> travels = new ArrayList<>();
    private long userId;
    private String from;
    private String nickName;
    private Call<JSONObject> call;

    private int size = 0;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_travel);
        ButterKnife.bind(this);
        userId = getIntent().getLongExtra(ConstantTool.USER_ID, -1);
        from = getIntent().getStringExtra(ConstantTool.FROM);
        nickName = getIntent().getStringExtra(ConstantTool.NICKNAME);
        initView();
        initListener();
        getData(1);
    }

    private void initView(){
        mToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        switch (from) {
            case ConstantTool.COLLECTION:
                mToolbar.setTitle(nickName + "收藏的游记");
                break;
            case ConstantTool.LIKE:
                mToolbar.setTitle(nickName + "赞过的游记");
                break;
            case ConstantTool.TRAVEL:
                mToolbar.setTitle(nickName + "的游记");
                break;
        }
        setSupportActionBar(mToolbar);
        mAdapter = new TravelAdapter();
        mManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.addItemDecoration(new DividerItemDecoration(this));
        mAdapter.setTravelBeans(travels);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSrf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                travels.clear();
                mAdapter.setIsLoading(false);
                mAdapter.setIsAll(false);
                page = 1;
                getData(1);
            }
        });
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        mAdapter.setOnItemClickListener(new TravelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PersonTravelActivity.this, ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantTool.TRAVEL, travels.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getData(int page){
        switch (from) {
            case ConstantTool.COLLECTION:
                call = RetrofitTool.getService().userCollection(userId, page);
                break;
            case ConstantTool.LIKE:
                call = RetrofitTool.getService().userLike(userId, page);
                break;
            case ConstantTool.TRAVEL:
                call = RetrofitTool.getService().getUserTravel(userId, page);
                break;
        }
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (mSrf.isRefreshing()) {
                    mSrf.setRefreshing(false);
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
                    List<TravelBean> travelBeans = FormatTool.gson.fromJson(String.valueOf(result.getJSONArray(ConstantTool.DATA)), new TypeToken<List<TravelBean>>() {
                    }.getType());
                    travels.addAll(travelBeans);
                    mAdapter.setTravelBeans(travels);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (mSrf.isRefreshing()) {
                    mSrf.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                if (!call.isCanceled()) {
                    ToastTool.show("服务器出现问题: " + t.getMessage());
                }
            }
        });
    }
}
