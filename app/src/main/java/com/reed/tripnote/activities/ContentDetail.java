package com.reed.tripnote.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.tools.ConstantTool;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentDetail extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar detailToolbar;
    @Bind(R.id.tv_detail_content)
    public TextView contentTV;
    @Bind(R.id.tv_detail_location)
    public TextView locationTV;
    @Bind(R.id.gv_detail_image)
    public GridView imageGV;
    private String travelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        ButterKnife.bind(this);
        travelName = getIntent().getStringExtra(ConstantTool.TRAVEL_NAME);
        initView();
    }

    private void initView() {
        if (TextUtils.isEmpty(travelName)) {
            detailToolbar.setTitle("");
        } else {
            detailToolbar.setTitle(travelName);
        }
        detailToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(detailToolbar);
    }
}
