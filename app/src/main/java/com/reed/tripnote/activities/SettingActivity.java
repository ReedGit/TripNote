package com.reed.tripnote.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
import com.reed.tripnote.tools.PathUtil;
import com.reed.tripnote.tools.ToastTool;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    public Toolbar settingToolbar;
    @Bind(R.id.tv_setting_cache)
    public TextView cacheTV;
    @Bind(R.id.btn_exit)
    public Button exitBtn;
    private UserBean user;

    private static final int CLEAR_OK = 0;

    private static class ClearHandler extends Handler {

        private WeakReference<SettingActivity> mActivity;

        public ClearHandler(SettingActivity settingActivity) {
            mActivity = new WeakReference<>(settingActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SettingActivity settingActivity = mActivity.get();
            if (msg.what == CLEAR_OK) {
                ToastTool.show("清除完成");
                settingActivity.cacheTV.setText(PathUtil.formatFileSize(PathUtil.getFileSize(PathUtil.getAvailableCacheDir(settingActivity))));
                super.handleMessage(msg);
            }
        }
    }

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
                UserManager.exitLogin();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_setting_cache:
                final ClearHandler clearHandler = new ClearHandler(SettingActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(SettingActivity.this).clearDiskCache();
                        clearHandler.sendEmptyMessage(CLEAR_OK);
                    }
                }).start();
                break;
        }

    }

    private void initView() {
        settingToolbar.setTitle(R.string.setting);
        settingToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(settingToolbar);
        cacheTV.setText(PathUtil.formatFileSize(PathUtil.getFileSize(PathUtil.getAvailableCacheDir(this))));
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
