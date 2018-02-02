package com.ankhrom.coinmarketcap.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.listener.SelectableItem;

/**
 * Created by R' on 2/2/2018.
 */

public class SelectableButton extends AppCompatButton implements SelectableItem {

    private boolean isSelected;

    public SelectableButton(Context context) {
        super(context);
        setItemSelected(false);
    }

    public SelectableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setItemSelected(false);
    }

    public SelectableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemSelected(false);
    }

    @Override
    public boolean isItemSelected() {
        return isSelected;
    }

    @Override
    public void setItemSelected(boolean selected) {
        isSelected = selected;

        if (isSelected) {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tint_light_color));
        } else {
            TypedValue theme = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, theme, true);
            setBackgroundResource(theme.resourceId);
        }
    }
}
