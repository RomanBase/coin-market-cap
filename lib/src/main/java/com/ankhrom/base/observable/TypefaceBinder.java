package com.ankhrom.base.observable;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by R' on 6/3/2018.
 */
public final class TypefaceBinder {

    @android.databinding.BindingAdapter("android:typeface")
    public static void setTypeface(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }
}
