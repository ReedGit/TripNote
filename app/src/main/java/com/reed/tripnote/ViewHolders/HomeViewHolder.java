package com.reed.tripnote.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.beans.TravelBean;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 伟 on 2016/2/14.
 */
public class HomeViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTv;
    public TextView dateTv;
    public CircleImageView headCIV;
    public TextView authorTv;
    public ImageView firstImageView;

    public HomeViewHolder(View itemView) {
        super(itemView);
        titleTv = (TextView) itemView.findViewById(R.id.tv_home_title);
        dateTv = (TextView) itemView.findViewById(R.id.tv_home_date);
        headCIV = (CircleImageView) itemView.findViewById(R.id.civ_home_head);
        authorTv = (TextView) itemView.findViewById(R.id.tv_home_author);
        firstImageView = (ImageView) itemView.findViewById(R.id.iv_home_first);
    }

    public void bindData(TravelBean travelBean){

    }
}
