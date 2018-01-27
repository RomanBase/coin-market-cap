package com.ankhrom.coinmarketcap.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ankhrom.base.animators.BaseAnim;
import com.ankhrom.base.common.statics.ScreenHelper;

/**
 * Created by R' on 1/27/2018.
 */

public class ExpandableFrameLayout extends FrameLayout {

    private int expandHeight;
    private boolean animate;

    private boolean isExpanded;

    public ExpandableFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ExpandableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ExpandableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setVisibility(GONE);

        expandHeight = (int) ScreenHelper.getPx(context, 192);
        animate = true;
    }

    public void setExpanded(boolean expand) {

        if (expand == isExpanded) {
            return;
        }

        if (expand) {
            expand();
        } else {
            collapse();
        }
    }

    public void expand() {

        isExpanded = true;

        if (animate) {
            BaseAnim.expand(this, expandHeight);
        } else {
            getLayoutParams().height = expandHeight;
            requestLayout();
            setVisibility(VISIBLE);
        }
    }

    public void collapse() {

        isExpanded = false;

        if (animate) {
            BaseAnim.collapse(this);
        } else {
            getLayoutParams().height = 0;
            requestLayout();
            setVisibility(GONE);
        }
    }
}
