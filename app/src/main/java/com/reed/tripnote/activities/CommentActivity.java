package com.reed.tripnote.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.adapters.CommentAdapter;
import com.reed.tripnote.beans.CommentBean;
import com.reed.tripnote.beans.UserBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar commentToolbar;
    @Bind(R.id.rcv_comment)
    public RecyclerView commentRCV;
    @Bind(R.id.srl_comment)
    public SwipeRefreshLayout commentSRL;

    private UserBean user;
    private CommentAdapter mAdapter;
    private List<CommentBean> comments;
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        user = ((App) getApplication()).getUser();
        initView();
        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null) {
            getMenuInflater().inflate(R.menu.add_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        commentToolbar.setTitle(R.string.comment);
        commentToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(commentToolbar);
        mManager = new LinearLayoutManager(this);
        mAdapter = new CommentAdapter();
        commentRCV.setLayoutManager(mManager);
        commentRCV.setAdapter(mAdapter);
        commentRCV.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener(){
        commentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commentSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        commentRCV.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
