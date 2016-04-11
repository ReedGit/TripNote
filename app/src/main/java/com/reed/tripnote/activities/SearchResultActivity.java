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

import com.reed.tripnote.R;
import com.reed.tripnote.adapters.TravelAdapter;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.data.TravelData;
import com.reed.tripnote.views.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchResultActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar searchToolbar;
    @Bind(R.id.rcv_search_travel)
    public RecyclerView searchRCV;

    private TravelAdapter mAdapter;
    private LinearLayoutManager mManager;
    private int visibleLastIndex = 0;
    private String query;
    private List<TravelBean> travels;

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
        travels = TravelData.getInstance().getTravels();
        mAdapter.setTravelBeans(travels);
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
