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

/**
 * 关于我
 * Created by 伟 on 2016/3/18.
 */
public class AboutFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_about, container, false);
            TextView weiBoTV = (TextView) mView.findViewById(R.id.tv_about_wei_bo);
            weiBoTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://m.weibo.cn/u/2944149551";
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        return mView;
    }
}
