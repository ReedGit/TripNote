package com.reed.tripnote.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.CommentBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * comment's viewHolder
 * Created by 伟 on 2016/3/29.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.civ_comment)
    public CircleImageView commentCIV;
    @Bind(R.id.tv_comment_name)
    public TextView nameTV;
    @Bind(R.id.tv_comment_date)
    public TextView dateTV;
    @Bind(R.id.tv_comment_remark)
    public TextView remarkTV;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Context context, CommentBean comment) {
        nameTV.setText(comment.getNickname());
        dateTV.setText(FormatTool.simpleTransformToString(comment.getTime()));
        remarkTV.setText(comment.getRemark());
        if (!TextUtils.isEmpty(comment.getUserImage())) {
            Glide.with(context).load(ConstantTool.imageUrl + comment.getUserImage()).placeholder(R.mipmap.default_head).into(commentCIV);
        }
    }
}
