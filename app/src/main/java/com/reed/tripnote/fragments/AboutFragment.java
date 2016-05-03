package com.reed.tripnote.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reed.tripnote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于我
 * Created by 伟 on 2016/3/18.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private View mView;
    @Bind(R.id.tv_about_wei_bo)
    public TextView weiBoTV;
    @Bind(R.id.tv_about_wei_bo_logo)
    public TextView logoTV;
    @Bind(R.id.tv_about_wei_bo_guide)
    public TextView guideTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_about, container, false);
            ButterKnife.bind(this, mView);
            weiBoTV.setOnClickListener(this);
            logoTV.setOnClickListener(this);
            guideTV.setOnClickListener(this);
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        String url = null;
        switch (v.getId()) {
            case R.id.tv_about_wei_bo:
                url = "http://m.weibo.cn/u/2944149551";
                break;
            case R.id.tv_about_wei_bo_logo:
                url = "http://m.weibo.cn/u/1800495203";
                break;
            case R.id.tv_about_wei_bo_guide:
                url = "http://m.weibo.cn/u/1782051632";
                break;
        }
        if (url != null) {
            openUrl(url);
        }
    }

    private void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
