package com.reed.tripnote.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.R;
import com.reed.tripnote.activities.InformationActivity;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.tools.CalculateTool;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 游记的显示模型
 * Created by 伟 on 2016/2/14.
 */
public class TravelViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_travel_title)
    public TextView titleTV;
    @Bind(R.id.tv_travel_date)
    public TextView dateTV;
    @Bind(R.id.civ_travel_head)
    public CircleImageView headCIV;
    @Bind(R.id.tv_travel_author)
    public TextView authorTV;
    @Bind(R.id.iv_travel_first)
    public ImageView firstImageView;
    @Bind(R.id.tv_travel_introduction)
    public TextView introductionTV;
    @Bind(R.id.ll_travel_author)
    public LinearLayout authorLL;

    public TravelViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(Context context, final TravelBean travelBean) {
        titleTV.setText(travelBean.getTitle());

        String date = FormatTool.transformToDateString(travelBean.getStartTime());
        if (travelBean.getEndTime() != null) {
            date += " / " + CalculateTool.calculateDay(travelBean.getStartTime(), travelBean.getEndTime()) + "天";
        }
        dateTV.setText(date);

        authorTV.setText(travelBean.getNickname());

        if (!TextUtils.isEmpty(travelBean.getIntroduction())) {
            String introduction = "\u3000\u3000" + travelBean.getIntroduction();
            introductionTV.setText(introduction);
        }

        if (!TextUtils.isEmpty(travelBean.getUserImage())){
            Glide.with(context).load(ConstantTool.imageUrl + travelBean.getUserImage()).placeholder(R.mipmap.default_head).into(headCIV);
        }

        if (!TextUtils.isEmpty(travelBean.getCoverImage())) {
            Glide.with(context).load(ConstantTool.imageUrl + travelBean.getCoverImage()).placeholder(R.mipmap.background).into(firstImageView);
        }

        authorLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InformationActivity.class);
                intent.putExtra(ConstantTool.USER_ID, travelBean.getUserId());
                v.getContext().startActivity(intent);
            }
        });
    }
}
