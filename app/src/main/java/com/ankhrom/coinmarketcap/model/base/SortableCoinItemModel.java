package com.ankhrom.coinmarketcap.model.base;

import com.ankhrom.base.interfaces.OnItemSelectedListener;

/**
 * Created by R' on 2/10/2018.
 */

public abstract class SortableCoinItemModel extends AppSelectableItemModel {

    public boolean isSortable;

    public int itemRank;
    public double itemPrice;
    public double itemChange24h;

    public SortableCoinItemModel() {
    }

    public SortableCoinItemModel(OnItemSelectedListener itemSelectedListener) {
        super(itemSelectedListener);
    }
}
