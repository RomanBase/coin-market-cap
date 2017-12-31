package com.ankhrom.coinmarketcap.view;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by R' on 12/31/2017.
 */

public class AppTextView extends AppCompatTextView {

    public AppTextView(Context context) {
        super(context);
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextColorResource(@ColorRes int resource) {

        setTextColor(ContextCompat.getColor(getContext(), resource));
    }
}
