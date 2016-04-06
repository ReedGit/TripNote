package com.reed.tripnote.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 头像伸缩
 * Created by 伟 on 2016/2/28.
 */
public class HeadImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private Rect mTmpRect;

    public HeadImageBehavior(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child,
                                          View dependency) {
        updateFabVisibility(parent, (AppBarLayout) dependency, child);
        return false;
    }

    private boolean updateFabVisibility(CoordinatorLayout parent,
                                        AppBarLayout appBarLayout, CircleImageView child) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != appBarLayout.getId()) {
            return false;
        }

        if (mTmpRect == null) {
            mTmpRect = new Rect();
        }

        final Rect rect = mTmpRect;
        rect.set(0, 0, appBarLayout.getWidth(), appBarLayout.getHeight());
        parent.offsetDescendantRectToMyCoords(appBarLayout, mTmpRect);
        mTmpRect.offset(appBarLayout.getScrollX(), appBarLayout.getScrollY());
        if (rect.bottom <= appBarLayout.getTotalScrollRange()) {
            child.setVisibility(View.GONE);
        } else {
            child.setVisibility(View.VISIBLE);
        }
        return true;
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, CircleImageView child,
                                 int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0, count = dependencies.size(); i < count; i++) {
            final View dependency = dependencies.get(i);
            if (dependency instanceof AppBarLayout
                    && updateFabVisibility(parent, (AppBarLayout) dependency, child)) {
                break;
            }
        }
        parent.onLayoutChild(child, layoutDirection);
        offsetIfNeeded(parent, child);
        return true;
    }

    /**
     * Pre-Lollipop we use padding so that the shadow has enough space to be drawn. This method
     * offsets our layout position so that we're positioned correctly if we're on one of
     * our parent's edges.
     */
    private void offsetIfNeeded(CoordinatorLayout parent, CircleImageView civ) {
        final Rect padding = new Rect();

        if (padding.centerX() > 0 && padding.centerY() > 0) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) civ.getLayoutParams();

            int offsetTB = 0, offsetLR = 0;

            if (civ.getRight() >= parent.getWidth() - lp.rightMargin) {
                // If we're on the left edge, shift it the right
                offsetLR = padding.right;
            } else if (civ.getLeft() <= lp.leftMargin) {
                // If we're on the left edge, shift it the left
                offsetLR = -padding.left;
            }
            if (civ.getBottom() >= parent.getBottom() - lp.bottomMargin) {
                // If we're on the bottom edge, shift it down
                offsetTB = padding.bottom;
            } else if (civ.getTop() <= lp.topMargin) {
                // If we're on the top edge, shift it up
                offsetTB = -padding.top;
            }

            civ.offsetTopAndBottom(offsetTB);
            civ.offsetLeftAndRight(offsetLR);
        }
    }

}
