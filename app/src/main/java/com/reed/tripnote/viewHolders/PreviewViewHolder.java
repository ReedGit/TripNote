package com.reed.tripnote.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 添加游记详情时展示添加的图片
 * Created by 伟 on 2016/4/22.
 */
public class PreviewViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.iv_item_add_image)
    public ImageView showImage;
    @Bind(R.id.iv_item_clear)
    public ImageView clearImage;

    public PreviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Context context, String path){
        Glide.with(context).load("file://" + path).into(showImage);
    }
}
