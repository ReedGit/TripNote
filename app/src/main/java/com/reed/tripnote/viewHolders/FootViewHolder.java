package com.reed.tripnote.viewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.adapters.TravelAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FootViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.pb_foot)
    public ProgressBar footPB;
    @Bind(R.id.tv_foot)
    public TextView footTV;

    public FootViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(int status) {
        switch (status) {
            case TravelAdapter.NO_DATA:
                footPB.setVisibility(View.GONE);
                footTV.setVisibility(View.VISIBLE);
                footTV.setText(R.string.date_none);
                break;
            case TravelAdapter.LOADING_DATA:
                footPB.setVisibility(View.VISIBLE);
                footTV.setVisibility(View.VISIBLE);
                footTV.setText(R.string.loading);
                break;
            case TravelAdapter.FINISH_DATA:
                footPB.setVisibility(View.GONE);
                footTV.setVisibility(View.VISIBLE);
                footTV.setText(R.string.load_more);
                break;
        }
    }
}
