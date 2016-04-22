package com.reed.tripnote.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.adapters.AddImageAdapter;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.ToastTool;
import com.reed.tripnote.views.FullyGridLayoutManager;
import com.wq.photo.widget.PickConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateContentActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar createToolbar;
    @Bind(R.id.tv_create_location)
    public TextView locationTV;
    @Bind(R.id.et_create_article)
    public EditText articleET;
    @Bind(R.id.rcv_create_image)
    public RecyclerView imageRCV;
    private FullyGridLayoutManager mManager;
    private AddImageAdapter mAdapter;
    private String coordinate;
    private String location;
    private String travelName;
    private List<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_content);
        ButterKnife.bind(this);
        travelName = getIntent().getStringExtra(ConstantTool.TRAVEL_NAME);
        coordinate = getIntent().getStringExtra(ConstantTool.COORDINATE);
        location = getIntent().getStringExtra(ConstantTool.LOCATION);
        initView();
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            //在data中返回 选择的图片列表
            paths.addAll(data.getStringArrayListExtra("data"));
            mAdapter.setImagePath(paths);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_publish:
                /*ToastTool.show("发布成功");*/

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        if (TextUtils.isEmpty(travelName)) {
            createToolbar.setTitle("");
        } else {
            createToolbar.setTitle(travelName);
        }
        locationTV.setText(location);
        createToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(createToolbar);
        mAdapter = new AddImageAdapter();
        mManager = new FullyGridLayoutManager(this, 3);
        imageRCV.setLayoutManager(mManager);
        imageRCV.setAdapter(mAdapter);
        imageRCV.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener() {
        createToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter.setOnAddClickListener(new AddImageAdapter.OnAddClickListener() {
            @Override
            public void onAddClick(View view, int position) {
                new PickConfig.Builder(CreateContentActivity.this)
                        .maxPickSize(6 - paths.size())//最多选择几张
                        .isneedcamera(true)//是否需要第一项是相机
                        .spanCount(3)//一行显示几张照片
                        .isneedcrop(false)//受否需要剪裁
                        .pickMode(PickConfig.MODE_MULTIP_PICK)//单选还是多选
                        .build();
            }
        });
        mAdapter.setOnClearClickListener(new AddImageAdapter.OnClearClickListener() {
            @Override
            public void onClearClick(View view, int position) {
                paths.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });

    }
}
