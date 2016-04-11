package com.reed.tripnote.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.tools.CalculateTool;
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

    public TravelViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(TravelBean travelBean) {
        titleTV.setText(travelBean.getTitle());
        String date = FormatTool.transformToDateString(travelBean.getStartTime());
        if (travelBean.getEndTime() != null) {
            date += " / " + CalculateTool.calculateDay(travelBean.getStartTime(), travelBean.getEndTime()) + "天";
        }
        dateTV.setText(date);
        authorTV.setText(travelBean.getNickname());
        firstImageView.setImageResource(R.mipmap.background);
        if (!TextUtils.isEmpty(travelBean.getIntroduction())) {
            String introduction = "\u3000\u3000" + travelBean.getIntroduction();
            introductionTV.setText(introduction);
        }
    }
}
