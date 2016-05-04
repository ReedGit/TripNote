package com.reed.tripnote.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.R;
import com.reed.tripnote.tools.ConstantTool;

import java.util.List;

/**
 * 咨询师聊天底栏GridView的适配器
 */
public class ContentImageAdapter extends BaseAdapter {

    private List<String> imageUrls;

    private Context context;

    public ContentImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
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
        Glide.with(context).load(ConstantTool.imageUrl + imageUrls.get(position)).into(imageView);
        return convertView;
    }
}
