package com.ankhrom.coinmarketcap.model.coin;

import com.ankhrom.base.interfaces.OnItemSelectedListener;
import com.ankhrom.coinmarketcap.R;
import com.ankhrom.coinmarketcap.model.base.AppSelectableItemModel;

/**
 * Created by R' on 1/28/2018.
 */

public class CoinAdapterFooterItemModel extends AppSelectableItemModel {

    public CoinAdapterFooterItemModel() {
    }

    public CoinAdapterFooterItemModel(OnItemSelectedListener itemSelectedListener) {
        super(itemSelectedListener);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.market_footer_item;
    }
}
