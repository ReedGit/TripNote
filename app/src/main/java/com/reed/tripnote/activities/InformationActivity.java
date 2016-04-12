package com.reed.tripnote.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人资料详情
 * Created by 伟 on 2016/2/27.
 */
public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.ctl_information)
    public CollapsingToolbarLayout informationCTL;

    @Bind(R.id.toolbar_information)
    public Toolbar informationToolbar;

    @Bind(R.id.civ_information_head)
    public CircleImageView headCIV;

    @Bind(R.id.tv_information_introduction)
    public TextView introductionTV;

    @Bind(R.id.tv_information_travel)
    public TextView travelTV;

    @Bind(R.id.tv_information_like)
    public TextView likeTV;

    @Bind(R.id.tv_information_collection)
    public TextView collectionTV;

    private UserBean user;
    private long userId;
    private UserBean author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        user = ((App) getApplication()).getUser();
        userId = getIntent().getLongExtra(ConstantTool.USER_ID, 0);
        ButterKnife.bind(this);
        initView();
        travelTV.setText("0");
        likeTV.setText("0");
        collectionTV.setText("0");
        if (user != null && user.getUserId() == userId) {
            author = user;
        } else {
            author = new UserBean();
            author.setUserId(userId);
        }
    }

    @Override
    protected void onResume() {
        if (!TextUtils.isEmpty(author.getHeadImage())) {
            Glide.with(this).load(ConstantTool.imageUrl + author.getHeadImage())
                    .placeholder(R.mipmap.default_head)
                    .into(headCIV);
        }
        informationCTL.setTitle(TextUtils.isEmpty(author.getNickName()) ? "" : author.getNickName());
        informationCTL.setCollapsedTitleTextColor(Color.WHITE);
        introductionTV.setText(TextUtils.isEmpty(author.getIntroduction()) ? "你还没有给自己写点介绍呢" : author.getIntroduction());
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null && user.getUserId() == userId){
            getMenuInflater().inflate(R.menu.personal_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify_information:
                Intent intent = new Intent(InformationActivity.this, PersonalActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(InformationActivity.this, PersonTravelActivity.class);
        intent.putExtra(ConstantTool.USER_ID, author.getUserId());
        intent.putExtra(ConstantTool.NICKNAME, author.getNickName());
        switch (v.getId()) {
            case R.id.ll_information_travel:
                intent.putExtra(ConstantTool.FROM, ConstantTool.TRAVEL);
                break;
            case R.id.ll_information_collection:
                intent.putExtra(ConstantTool.FROM, ConstantTool.COLLECTION);
                break;
            case R.id.ll_information_like:
                intent.putExtra(ConstantTool.FROM, ConstantTool.LIKE);
                break;
            default:
                intent = null;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void initView() {
        informationToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(informationToolbar);
        informationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
