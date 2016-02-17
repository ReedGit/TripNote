package com.reed.tripnote.ViewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reed.tripnote.R;

public class FootViewHolder extends RecyclerView.ViewHolder {

    public static ProgressBar footPB;
    public static TextView footTV;

    public FootViewHolder(View itemView) {
        super(itemView);
        footPB = (ProgressBar) itemView.findViewById(R.id.pb_foot);
        footTV = (TextView) itemView.findViewById(R.id.tv_foot);
    }
}
