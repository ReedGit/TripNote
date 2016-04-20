package com.reed.tripnote.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.adapters.CommentAdapter;
import com.reed.tripnote.beans.CommentBean;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;
import com.reed.tripnote.views.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = CommentActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    public Toolbar commentToolbar;
    @Bind(R.id.rcv_comment)
    public RecyclerView commentRCV;
    @Bind(R.id.srf_comment)
    public SwipeRefreshLayout commentSRL;

    private UserBean user;
    private long travelId;
    private CommentAdapter mAdapter;
    private List<CommentBean> comments = new ArrayList<>();
    private int visibleLastIndex = 0;
    private LinearLayoutManager mManager;
    private TextInputEditText commentET;

    private ProgressDialog mDlg;

    private int size = 0;
    private int page = 1;
    private Call<JSONObject> listCall;
    private Call<JSONObject> addCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        user = ((App) getApplication()).getUser();
        travelId = getIntent().getLongExtra(ConstantTool.TRAVEL_ID, 0);
        initView();
        initListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listCall != null && listCall.isExecuted()) {
            listCall.cancel();
        }
        if (addCall != null && addCall.isExecuted()) {
            addCall.cancel();
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                View view = LayoutInflater.from(CommentActivity.this).inflate(R.layout.dlg_comment, null, false);
                commentET = (TextInputEditText) view.findViewById(R.id.et_comment_add);
                builder.setCancelable(true);
                builder.setView(view);
                builder.setPositiveButton(R.string.publish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String remark = commentET.getText().toString();
                        if (TextUtils.isEmpty(remark)) {
                            ToastTool.show(CommentActivity.this, "评论不能为空");
                        } else {
                            addComment(remark);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel,null);
                builder.create().show();
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
        commentRCV.addItemDecoration(new DividerItemDecoration(this, (float) 1));
        commentSRL.setRefreshing(true);
        getData(1);
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
                comments.clear();
                mAdapter.setIsLoading(false);
                mAdapter.setIsAll(false);
                page = 1;
                getData(1);
            }
        });
        commentRCV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemLastIndex = mAdapter.getItemCount() - 1;
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemLastIndex) {
                    if (comments.size() < size){
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

    private void addComment(String remark){
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantTool.USER_ID, user.getUserId());
        map.put(ConstantTool.TRAVEL_ID, travelId);
        map.put(ConstantTool.REMARK, remark);
        addCall = RetrofitTool.getService().addComment(map);
        mDlg = ProgressDialog.show(CommentActivity.this, null, "请稍后......", true);
        addCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (mDlg.isShowing()){
                    mDlg.cancel();
                }
                if (response.code() != 200) {
                    ToastTool.show(CommentActivity.this, response.message());
                    LogTool.e(TAG, response.message());
                    return;
                }
                JSONObject result = response.body();
                try {
                    if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                        ToastTool.show(CommentActivity.this, result.getString(ConstantTool.MSG));
                        return;
                    }
                    ToastTool.show(CommentActivity.this, "评论成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (mDlg.isShowing()) {
                    mDlg.cancel();
                }
                if (!call.isCanceled()) {
                    ToastTool.show(CommentActivity.this, "服务器出现问题: " + t.getMessage());
                }
            }
        });
    }

    private void getData(int page){
        listCall = RetrofitTool.getService().getComments(travelId, page);
        listCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (commentSRL.isRefreshing()) {
                    commentSRL.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                if (response.code() != 200) {
                    ToastTool.show(CommentActivity.this, response.message());
                    LogTool.e(TAG, "请求出错：" + response.message());
                    return;
                }
                LogTool.i(TAG, response.body().toString());
                JSONObject result = response.body();
                try {
                    if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                        ToastTool.show(CommentActivity.this, result.getString(ConstantTool.MSG));
                        return;
                    }
                    size = result.getInt(ConstantTool.SIZE);
                    List<CommentBean> commentBeans = FormatTool.gson.fromJson(String.valueOf(result.getJSONArray(ConstantTool.DATA)), new TypeToken<List<CommentBean>>(){}.getType());
                    comments.addAll(commentBeans);
                    mAdapter.setComments(comments);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (commentSRL.isRefreshing()) {
                    commentSRL.setRefreshing(false);
                }
                mAdapter.setIsLoading(false);
                mAdapter.notifyDataSetChanged();
                if (!call.isCanceled()) {
                    ToastTool.show(CommentActivity.this, "服务器出现问题: " + t.getMessage());
                }
            }
        });
    }
}
