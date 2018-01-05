package com.ankhrom.coinmarketcap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.listener.OnItemSwipeListener;

/**
 * Created by R' on 1/3/2018.
 */

public class ItemSwipeListener extends ItemTouchHelper.SimpleCallback {

    public boolean swipeBack;

    private final Context context;
    private final OnItemSwipeListener listener;
    private final int foregroundItem;

    public ItemSwipeListener(@NonNull Context context, @IdRes int foregroundItem, @Nullable OnItemSwipeListener listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        this.context = context;
        this.foregroundItem = foregroundItem;
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder == null || viewHolder.itemView == null) {
            if (listener != null) {
                listener.onSelectedItemChanged(-1);
            }
            return;
        }

        View view = viewHolder.itemView.findViewById(foregroundItem);

        if (view != null) {
            getDefaultUIUtil().onSelected(view);
        } else {
            super.onSelectedChanged(viewHolder, actionState);
        }

        if (listener != null) {
            listener.onSelectedItemChanged(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (viewHolder == null || viewHolder.itemView == null) {
            return;
        }

        float border = context.getResources().getDimension(R.dimen.toggle_item_action_border);
        float progress = Math.min(Math.abs(dX) / border, 1.0f);

        if (listener != null) {
            listener.onItemSwipeProgress(viewHolder.getAdapterPosition(), progress, dX < 0.0f);
        }

        View view = viewHolder.itemView.findViewById(foregroundItem);

        if (view != null) {
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (viewHolder == null || viewHolder.itemView == null) {
            return;
        }

        View view = viewHolder.itemView.findViewById(foregroundItem);

        if (view != null) {
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        if (swipeBack) {
            swipeBack = false;
            return 0;
        }

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
