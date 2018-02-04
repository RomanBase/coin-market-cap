package com.ankhrom.coinmarketcap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ankhrom.coinmarketcap.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by R' on 2/3/2018.
 */

public class IconStack extends View {

    private List<Drawable> icons = new ArrayList<>();
    private Drawable backgroundDrawable;

    public IconStack(Context context) {
        super(context);
    }

    public IconStack(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconStack(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public IconStack(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {

        backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.icon_round_background);
    }

    public void setIcons(Collection<Integer> resources) {

        icons.clear();

        for (int res : resources) {
            icons.add(ContextCompat.getDrawable(getContext(), res));
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int count = icons.size();
        float fCount = (float) count;

        if (count == 0) {
            return;
        }

        float size = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingTop() - getPaddingBottom();

        float offset = (height - (fCount * size)) / (fCount + 1.0f);
        float offsetSize = size;

        if (offset < 0.0f) {
            offsetSize = height / (fCount + 1.0f);
            offset = 0.0f;
        }

        for (int i = 0; i < count; i++) {

            Rect bounds = getBounds(getPaddingLeft(), getPaddingTop() + offset + (offset + offsetSize) * i, size, size);

            backgroundDrawable.setBounds(bounds);
            backgroundDrawable.draw(canvas);

            icons.get(i).setBounds(bounds);
            icons.get(i).draw(canvas);
        }

    }

    private Rect getBounds(float x, float y, float width, float height) {

        return new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));
    }
}
