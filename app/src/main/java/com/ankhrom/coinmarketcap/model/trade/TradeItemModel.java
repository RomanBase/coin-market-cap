package com.ankhrom.coinmarketcap.model.trade;

import com.ankhrom.base.model.SelectableItemModel;
import com.ankhrom.base.observable.ObservableString;
import com.ankhrom.coinmarketcap.BR;
import com.ankhrom.coinmarketcap.R;

/**
 * Created by R' on 1/6/2018.
 */

public class TradeItemModel extends SelectableItemModel {

    public final ObservableString info = new ObservableString();

    @Override
    public int getVariableBindingResource() {
        return BR.M;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.trade_item;
    }
}
