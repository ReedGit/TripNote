package com.reed.tripnote.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.reed.tripnote.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人资料详情
 * Created by 伟 on 2016/2/27.
 */
public class InformationActivity extends AppCompatActivity {

    @Bind(R.id.ctl_information)
    public CollapsingToolbarLayout informationCTL;

    @Bind(R.id.toolbar_information)
    public Toolbar informationToolbar;

    @Bind(R.id.civ_information_head)
    public CircleImageView headCIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        informationToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(informationToolbar);
        informationCTL.setTitle("测试");
        informationCTL.setCollapsedTitleTextColor(Color.WHITE);
        informationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
