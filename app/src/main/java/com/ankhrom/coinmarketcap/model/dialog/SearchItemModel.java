package com.ankhrom.coinmarketcap.model.dialog;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.entity.CoinItem;

/**
 * Created by R' on 1/7/2018.
 */

public class SearchItemModel extends SelectableItemModel {

    public final CoinItem coin;
    public final String fullName;

    public SearchItemModel(CoinItem coin) {
        this(coin, null);
    }

    public SearchItemModel(CoinItem coin, OnItemSelectedListener itemSelectedListener) {
        super(itemSelectedListener);

        this.coin = coin;
        fullName = coin.symbol + " - " + coin.name;
    }

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.search_item;
    }
}
