package com.reed.tripnote.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reed.tripnote.R;

/**
 * 咨询师聊天底栏GridView的适配器
 */
public class ContentImageAdapter extends BaseAdapter {

    private int[] image = new int[]{R.mipmap.background, R.mipmap.background, R.mipmap.background, R.mipmap.background, R.mipmap.background, R.mipmap.background};

    private Context context;

    public ContentImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return image[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_item_pic);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(image[position]);
        return convertView;
    }
}
