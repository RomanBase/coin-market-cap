package com.ankhrom.coinmarketcap.model;

import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.api.CoinItem;

/**
 * Created by R' on 12/30/2017.
 */

public class CoinItemModel extends SelectableItemModel {

    public final String name;

    public CoinItemModel(CoinItem item) {

        name = item.name;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.coin_item;
    }
}
