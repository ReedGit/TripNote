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

import com.reed.tripnote.R;
import com.reed.tripnote.adapters.TravelAdapter;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.data.TravelData;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.views.DividerItemDecoration;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

public class PersonTravelActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.refresh)
    public SwipeRefreshLayout mSrf;
    @Bind(R.id.recycler_view)
    public RecyclerView mRecycler;

    private TravelAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int visibleLastIndex = 0;
    private List<TravelBean> travels;
    private long userId;
    private String from;
    private String nickName;
    private Call<JSONObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_travel);
        ButterKnife.bind(this);
        userId = getIntent().getLongExtra(ConstantTool.USER_ID, 0);
        from = getIntent().getStringExtra(ConstantTool.FROM);
        nickName = getIntent().getStringExtra(ConstantTool.NICKNAME);
        initView();
        initListener();
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
        travels = TravelData.getInstance().getTravels();
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
                mSrf.setRefreshing(false);
            }
        });
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
}
