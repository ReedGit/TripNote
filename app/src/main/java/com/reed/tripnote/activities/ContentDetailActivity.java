package com.reed.tripnote.activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.R;
import com.reed.tripnote.adapters.ContentImageAdapter;
import com.reed.tripnote.beans.ContentBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentDetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar detailToolbar;

    @Bind(R.id.tv_detail_content)
    public TextView contentTV;

    @Bind(R.id.tv_detail_location)
    public TextView locationTV;

    @Bind(R.id.tv_detail_time)
    public TextView timeTV;

    @Bind(R.id.gv_detail_image)
    public GridView imageGV;

    private String travelName;
    private ContentBean content;
    private ContentImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        ButterKnife.bind(this);
        travelName = getIntent().getStringExtra(ConstantTool.TRAVEL_NAME);
        content = (ContentBean) getIntent().getSerializableExtra(ConstantTool.CONTENT);
        mAdapter = new ContentImageAdapter(this, content.getImageurl());
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
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentTV.setText(content.getArticle());
        locationTV.setText(content.getLocation());
        String time = FormatTool.transformToDateString(content.getTime()) + " / 第" + content.getDay() + "天";
        timeTV.setText(time);
        imageGV.setAdapter(mAdapter);
        imageGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(ContentDetailActivity.this, R.style.Dialog_Fullscreen);
                View dlgView = LayoutInflater.from(ContentDetailActivity.this).inflate(R.layout.image_browser, null, false);
                ImageView imageView = (ImageView) dlgView.findViewById(R.id.iv_message_browser);
                LinearLayout linearLayout = (LinearLayout) dlgView.findViewById(R.id.ll_message_browser);
                Glide.with(ContentDetailActivity.this).load(ConstantTool.imageUrl + mAdapter.getItem(position)).into(imageView);
                dialog.setContentView(dlgView);
                dialog.show();
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

}
