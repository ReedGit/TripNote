package com.reed.tripnote.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.beans.TravelBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 游记的显示模型
 * Created by 伟 on 2016/2/14.
 */
public class TravelViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_travel_title)
    public TextView titleTv;
    @Bind(R.id.tv_travel_date)
    public TextView dateTv;
    @Bind(R.id.civ_travel_head)
    public CircleImageView headCIV;
    @Bind(R.id.tv_travel_author)
    public TextView authorTV;
    @Bind(R.id.iv_travel_first)
    public ImageView firstImageView;

    public TravelViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(TravelBean travelBean) {

    }
}
