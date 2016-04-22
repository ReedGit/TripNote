package com.reed.tripnote.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reed.tripnote.R;
import com.reed.tripnote.viewHolders.PreviewViewHolder;
import com.reed.tripnote.viewHolders.AddImageViewHolder;

import java.util.List;

/**
 * 添加新游记详情时增加图片的adapter
 * Created by 伟 on 2016/4/22.
 */
public class AddImageAdapter extends RecyclerView.Adapter {

    private final int ADD_IMAGE = 0;
    private final int PREVIEW_IMAGE = 1;
    private Context context;

    private List<String> imagePath;

    public void setImagePath(List<String> imagePath) {
        this.imagePath = imagePath;
    }

    public interface OnAddClickListener {
        void onAddClick(View view, int position);
    }

    private OnAddClickListener onAddClickListener;

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public interface OnClearClickListener {
        void onClearClick(View view, int position);
    }

    private OnClearClickListener onClearClickListener;

    public void setOnClearClickListener(OnClearClickListener onClearClickListener) {
        this.onClearClickListener = onClearClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        switch (viewType) {
            case PREVIEW_IMAGE:
                return new PreviewViewHolder(inflater.inflate(R.layout.item_add_image, parent, false));
            case ADD_IMAGE:
                return new AddImageViewHolder(inflater.inflate(R.layout.item_add_image_end, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (imagePath == null || (imagePath.size() < 6 && imagePath.size() == position)) {
            if (onAddClickListener != null) {
                ((AddImageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddClickListener.onAddClick(v, holder.getAdapterPosition());
                    }
                });
            }
            return;
        }
        ((PreviewViewHolder) holder).bindData(context, imagePath.get(position));
        if (onClearClickListener != null) {
            ((PreviewViewHolder) holder).clearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClearClickListener.onClearClick(v, holder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return imagePath == null ? 1 : (imagePath.size() >= 6 ? 6 : imagePath.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (imagePath == null) {
            return ADD_IMAGE;
        }
        if (imagePath.size() >= 6) {
            return PREVIEW_IMAGE;
        }
        if (imagePath.size() == position) {
            return ADD_IMAGE;
        }
        return PREVIEW_IMAGE;
    }
}
