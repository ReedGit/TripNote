package com.reed.tripnote.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
import com.reed.tripnote.tools.ToastTool;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.toolbar)
    public Toolbar settingToolbar;
    @Bind(R.id.btn_exit)
    public Button exitBtn;
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        user = ((App) getApplication()).getUser();
        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                UserManager.exitLogin(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_setting_cache:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(SettingActivity.this).clearDiskCache();
                    }
                }).start();
                ToastTool.show(SettingActivity.this, "清除成功");
                break;
        }

    }

    private void initView() {
        settingToolbar.setTitle(R.string.setting);
        settingToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(settingToolbar);
        if (user == null) {
            exitBtn.setVisibility(View.GONE);
        } else {
            exitBtn.setVisibility(View.VISIBLE);
        }
        settingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
