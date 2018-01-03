package com.ankhrom.coinmarketcap.listener;

/**
 * Created by R' on 1/3/2018.
 */

public interface OnItemSwipeListener {

    void onSelectedItemChanged(int index);

    void onItemSwipeProgress(int index, float progress, boolean directionToLeft);
}
